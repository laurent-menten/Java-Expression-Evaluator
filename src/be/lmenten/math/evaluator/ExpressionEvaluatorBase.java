/* ************************************************************************* *
 * lib-lmenten-math : Expression evaluator   -   (c)2022+ Laurent DAM Menten *
 * ------------------------------------------------------------------------- *
 * This program is free software: you can redistribute it and/or modify it   *
 * under the terms of the GNU General Public License as published by the     *
 * Free Software Foundation, either version 3 of the License, or (at your    *
 * option) any later version.                                                *
 *                                                                           *
 * This program is distributed in the hope that it will be useful, but       *
 * WITHOUT ANY WARRANTY; without even the implied warranty of                *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General *
 * Public License for more details.                                          *
 *                                                                           *
 * You should have received a copy of the GNU General Public License along   *
 * with this program. If not, see <https://www.gnu.org/licenses/>.           *
 * ************************************************************************* */

package be.lmenten.math.evaluator;

import be.lmenten.math.evaluator.exceptions.EvaluateException;
import be.lmenten.math.evaluator.exceptions.FunctionNotFoundException;
import be.lmenten.math.evaluator.exceptions.IncompatibleNamedObjectException;
import be.lmenten.math.evaluator.exceptions.IncompatibleValueException;
import be.lmenten.math.evaluator.exceptions.NamedObjectNotFoundException;
import be.lmenten.math.evaluator.exceptions.ValueNotFoundException;
import be.lmenten.math.evaluator.features.DiceRoll;
import be.lmenten.math.evaluator.grammar.analysis.DepthFirstAdapter;
import be.lmenten.math.evaluator.grammar.node.AAddExp;
import be.lmenten.math.evaluator.grammar.node.AAndExp;
import be.lmenten.math.evaluator.grammar.node.AAssignObjExp;
import be.lmenten.math.evaluator.grammar.node.AAssignVarExp;
import be.lmenten.math.evaluator.grammar.node.ADiceExp;
import be.lmenten.math.evaluator.grammar.node.ADivExp;
import be.lmenten.math.evaluator.grammar.node.AEqualExp;
import be.lmenten.math.evaluator.grammar.node.AFloatExp;
import be.lmenten.math.evaluator.grammar.node.AFractionExp;
import be.lmenten.math.evaluator.grammar.node.AFunc0Exp;
import be.lmenten.math.evaluator.grammar.node.AFunc1Exp;
import be.lmenten.math.evaluator.grammar.node.AFunc2Exp;
import be.lmenten.math.evaluator.grammar.node.AFunc3Exp;
import be.lmenten.math.evaluator.grammar.node.AGEqualExp;
import be.lmenten.math.evaluator.grammar.node.AGThanExp;
import be.lmenten.math.evaluator.grammar.node.AGetterExp;
import be.lmenten.math.evaluator.grammar.node.AInteger10Exp;
import be.lmenten.math.evaluator.grammar.node.AInteger16Exp;
import be.lmenten.math.evaluator.grammar.node.AInteger2Exp;
import be.lmenten.math.evaluator.grammar.node.AInteger8Exp;
import be.lmenten.math.evaluator.grammar.node.ALEqualExp;
import be.lmenten.math.evaluator.grammar.node.ALThanExp;
import be.lmenten.math.evaluator.grammar.node.AModExp;
import be.lmenten.math.evaluator.grammar.node.AMulExp;
import be.lmenten.math.evaluator.grammar.node.ANotEqualExp;
import be.lmenten.math.evaluator.grammar.node.AOrExp;
import be.lmenten.math.evaluator.grammar.node.ASubExp;
import be.lmenten.math.evaluator.grammar.node.AValueExp;
import be.lmenten.math.evaluator.grammar.node.AXorExp;
import be.lmenten.math.evaluator.grammar.node.Start;
import be.lmenten.math.evaluator.utils.ParseUtils;

import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Separate class for the DepthFirstAdapter.
 */
public abstract sealed class ExpressionEvaluatorBase
	extends DepthFirstAdapter
	permits ExpressionEvaluator
{
	public static final Double FALSE = 0.0D;
	public static final Double TRUE = 1.0D;

	// ------------------------------------------------------------------------

	public static final String ANS_VARIABLE_NAME = "ans";
	public static final String TMP_VARIABLE_NAME = "tmp";

	public static final String ACCUMULATOR_NAME = "acc";

	// ------------------------------------------------------------------------

	public abstract boolean isThrowsExceptionOnDivideByZero();
	public abstract boolean isAssignationEnabled();
	public abstract boolean isAutoCreateVariablesEnabled();

	// ------------------------------------------------------------------------

	public abstract void addVariable( Variable variable );
	public abstract Variable getVariable( String variableName );

	public abstract NamedObject getNamedObject( String objectName );

	// ------------------------------------------------------------------------

	/*package*/  abstract ValueProvider getValueProvider( String providerName );

	/*package*/ abstract Map<String,Function0> getFunction0Map();
	/*package*/ abstract Map<String,Function1> getFunction1Map();
	/*package*/ abstract Map<String,Function2> getFunction2Map();
	/*package*/ abstract Map<String,Function3> getFunction3Map();

	/*package*/ abstract void addDiceRoll( DiceRoll diceRoll );

	// ------------------------------------------------------------------------

	public abstract Random getRandomNumberGenerator();

	// ------------------------------------------------------------------------
	// - Evaluation stack -----------------------------------------------------
	// ------------------------------------------------------------------------

	Stack<Double> stack = new Stack<>();

	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXXXXXXXX SableCC grammar support XXXXXXXXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	@Override
	public void outStart( Start node )
	{
		if( stack.size() != 1 )
		{
			throw new EvaluateException( EvaluateException.Reason.INVALID_STATE );
		}

		getVariable( ANS_VARIABLE_NAME ).setValue( stack.pop() );
	}

	// ========================================================================
	// = Assignation ==========================================================
	// ========================================================================

	/*
	 * Assignation to a variable
	 */
	@Override
	public void outAAssignVarExp( AAssignVarExp node )
	{
		if( ! isAssignationEnabled() )
		{
			throw new EvaluateException( EvaluateException.Reason.ASSIGNATION_DISABLED );
		}

		String variableName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = variableName.length();

		Double n = stack.peek();

		ValueProvider provider = getValueProvider( variableName );
		if( provider == null )
		{
			NamedObject namedObject = getNamedObject( variableName );
			if( namedObject != null )
			{
				if( namedObject.getObject() instanceof ValueConsumer objectConsumer )
				{
					objectConsumer.setValue( n );
				}
				else
				{
					throw new IncompatibleNamedObjectException( namedObject, true, pos, length );
				}
			}
			else
			{
				if( isAutoCreateVariablesEnabled() )
				{
					addVariable( new Variable( variableName, n ) );
				}
				else
				{
					throw new ValueNotFoundException( variableName, pos, length );
				}
			}
		}
		else if( provider instanceof Variable variable )
		{
			variable.setValue( n );
		}
		else
		{
			throw new IncompatibleValueException( ((Value)provider), true, pos, length );
		}
	}

	/*
	 * Assignation to a named object
	 */
	@Override
	public void outAAssignObjExp( AAssignObjExp node )
	{
		if( ! isAssignationEnabled() )
		{
			throw new EvaluateException( EvaluateException.Reason.ASSIGNATION_DISABLED );
		}

		String reference = node.getObjIdentifier().getText().trim();
		int pos = node.getObjIdentifier().getPos();
		int length = reference.length();

		String [] splitReference = reference.split( "\\." );
		String objectName = splitReference[0];
		String propertyName = splitReference[1];

		NamedObject namedObject = getNamedObject( objectName );
		if( namedObject == null )
		{
			throw new NamedObjectNotFoundException( objectName, pos, length );
		}

		try
		{
			namedObject.setPropertyValue( propertyName, stack.peek() );
		}
		catch( IncompatibleNamedObjectException ex )
		{
			throw new IncompatibleNamedObjectException( namedObject, propertyName, true, pos, length );
		}
	}

	// ========================================================================
	// = Associative operators ================================================
	// ========================================================================

	@Override
	public void outAMulExp( AMulExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( n1 * n2 );
	}

	@Override
	public void outAAddExp( AAddExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( n1 + n2 );
	}

	// ========================================================================
	// = Non-associative operators ============================================
	// ========================================================================

	@Override
	public void outADivExp( ADivExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		if( n1.equals( 0.0d ) && isThrowsExceptionOnDivideByZero() )
		{
			throw new EvaluateException( EvaluateException.Reason.DIVIDE_BY_ZERO );
		}

		stack.push( n2 / n1 );
	}

	@Override
	public void outAModExp( AModExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( n1 % n2 );
	}

	@Override
	public void outASubExp( ASubExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( n2 - n1 );
	}

	// ========================================================================
	// = Logical operators ====================================================
	// ========================================================================

	@Override
	public void outAEqualExp( AEqualExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2.equals( n1 )) ? TRUE : FALSE );
	}

	@Override
	public void outANotEqualExp( ANotEqualExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( n2.equals(n1) ? FALSE : TRUE );
	}

	@Override
	public void outALThanExp( ALThanExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2 < n1) ? TRUE : FALSE );
	}

	@Override
	public void outALEqualExp( ALEqualExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2 <= n1) ? TRUE : FALSE );
	}

	@Override
	public void outAGThanExp( AGThanExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2 > n1) ? TRUE : FALSE );
	}

	@Override
	public void outAGEqualExp( AGEqualExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2 >= n1) ? TRUE : FALSE );
	}

	@Override
	public void outAAndExp( AAndExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2.equals(TRUE) && n1.equals(TRUE)) ? TRUE : FALSE );
	}

	@Override
	public void outAOrExp( AOrExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2.equals(TRUE) || n1.equals(TRUE)) ? TRUE : FALSE );
	}

	@Override
	public void outAXorExp( AXorExp node )
	{
		Double n1 = stack.pop();
		Double n2 = stack.pop();

		stack.push( (n2.equals(TRUE) ^ n1.equals(TRUE)) ? TRUE : FALSE );
	}

	// ========================================================================
	// = Unary ================================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	// - Integers -------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAInteger2Exp( AInteger2Exp node )
	{
		String number = node.getInteger2().getText();

		Double n = (double) ParseUtils.parseBinary( number );

		stack.push( n );
	}

	@Override
	public void inAInteger8Exp( AInteger8Exp node )
	{
		String number = node.getInteger8().getText().trim();

		Double n = (double) ParseUtils.parseOctal( number );

		stack.push( n );
	}

	@Override
	public void inAInteger10Exp( AInteger10Exp node )
	{
		String number = node.getInteger10().getText().trim();

		Double n = (double) ParseUtils.parseDecimal( number );

		stack.push( n );
	}

	@Override
	public void inAInteger16Exp( AInteger16Exp node )
	{
		String number = node.getInteger16().getText().trim();

		Double n = (double) ParseUtils.parseHexadecimal( number );

		stack.push( n );
	}

	@Override
	public void inADiceExp( ADiceExp node )
	{
		String die = node.getDice().getText().trim();

		DiceRoll dieRoll = ParseUtils.parseDie( die,  (ExpressionEvaluator) this );

		addDiceRoll( dieRoll );
		stack.push( dieRoll.getValue() );
	}

	// ------------------------------------------------------------------------
	// - Floats ---------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAFloatExp( AFloatExp node )
	{
		String number = node.getFloat().getText().trim();

		Double n = ParseUtils.parseFloatingPoint( number );

		stack.push( n );
	}

	// ------------------------------------------------------------------------
	// - Fractions ------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAFractionExp( AFractionExp node )
	{
		String number = node.getFraction().getText().trim();

		Double n = ParseUtils.parseFraction( number );

		stack.push( n );
	}

	// ------------------------------------------------------------------------
	// - Functions ------------------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void outAFunc0Exp( AFunc0Exp node )
	{
		String functionName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = functionName.length();

		Function0 function = getFunction0Map().get( functionName );
		if( function != null )
		{
			stack.push( function.compute() );
		}
		else
		{
			throw new FunctionNotFoundException( functionName, Function.SUFFIX_NO_PARAMETERS, pos, length );
		}
	}

	@Override
	public void outAFunc1Exp( AFunc1Exp node )
	{
		String functionName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = functionName.length();

		Double n = stack.pop();

		Function1 function = getFunction1Map().get( functionName );
		if( function != null )
		{
			stack.push( function.compute( n ) );
		}
		else
		{
			throw new FunctionNotFoundException( functionName, Function.SUFFIX_1_PARAMETER, pos, length );
		}
	}

	@Override
	public void outAFunc2Exp( AFunc2Exp node )
	{
		String functionName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = functionName.length();

		Double n1 = stack.pop();
		Double n2 = stack.pop();

		Function2 function = getFunction2Map().get( functionName );
		if( function != null )
		{
			stack.push( function.compute( n1, n2 ) );
		}
		else
		{
			throw new FunctionNotFoundException( functionName, Function.SUFFIX_2_PARAMETERS, pos, length );
		}
	}

	@Override
	public void outAFunc3Exp( AFunc3Exp node )
	{
		String functionName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = functionName.length();

		Double n1 = stack.pop();
		Double n2 = stack.pop();
		Double n3 = stack.pop();

		Function3 function = getFunction3Map().get( functionName );
		if( function != null )
		{
			stack.push( function.compute( n3, n2, n1 ) );
		}
		else
		{
			throw new FunctionNotFoundException( functionName, Function.SUFFIX_3_PARAMETERS, pos, length );
		}
	}

	// ------------------------------------------------------------------------
	// - Variable and getters -------------------------------------------------
	// ------------------------------------------------------------------------

	@Override
	public void inAValueExp( AValueExp node )
	{
		String providerName = node.getIdentifier().getText().trim();
		int pos = node.getIdentifier().getPos();
		int length = providerName.length();

		double sign = 1.0D;
		if( providerName.startsWith( "-" ) )
		{
			providerName = providerName.substring( 1 );
			sign = -1.0D;
		}

		ValueProvider variable = getValueProvider( providerName );
		if( variable != null )
		{
			stack.push( sign * variable.getValue() );
		}
		else
		{
			NamedObject namedObject = getNamedObject( providerName );
			if( namedObject != null  )
			{
				if( namedObject.getObject() instanceof ValueProvider provider )
				{
					stack.push( sign * provider.getValue() );
				}
				else
				{
					throw new IncompatibleNamedObjectException( namedObject, false, pos, length );
				}
			}
			else
			{
				throw new ValueNotFoundException( providerName, pos, length );
			}
		}
	}

	@Override
	public void inAGetterExp( AGetterExp node )
	{
		String reference = node.getObjIdentifier().getText().trim();
		int pos = node.getObjIdentifier().getPos();
		int length = reference.length();

		double sign = 1.0D;
		if( reference.startsWith( "-" ) )
		{
			reference = reference.substring( 1 );
			sign = -1.0D;
		}

		String [] splitReference = reference.split( "\\." );
		String objectName = splitReference[0];
		String propertyName = splitReference[1];

		NamedObject namedObject = getNamedObject( objectName );
		if( namedObject == null )
		{
			throw new NamedObjectNotFoundException( objectName, pos, length );
		}

		try
		{
			Double n = namedObject.getPropertyValue( propertyName );
			stack.push( sign * n );
		}
		catch( IncompatibleNamedObjectException ex )
		{
			throw new IncompatibleNamedObjectException( namedObject, propertyName, false, pos, length );
		}
	}
}
