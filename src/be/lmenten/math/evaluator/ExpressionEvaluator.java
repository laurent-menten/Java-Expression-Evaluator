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
import be.lmenten.math.evaluator.features.Accumulator;
import be.lmenten.math.evaluator.features.DiceRoll;
import be.lmenten.math.evaluator.grammar.lexer.Lexer;
import be.lmenten.math.evaluator.grammar.lexer.LexerException;
import be.lmenten.math.evaluator.grammar.node.Start;
import be.lmenten.math.evaluator.grammar.parser.Parser;
import be.lmenten.math.evaluator.grammar.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public final class ExpressionEvaluator
	extends ExpressionEvaluatorBase
{
	// ========================================================================
	// = BEGIN OF GENERATED VALUES - DO NOT EDIT ==============================
	// ========================================================================
	// Generation script: buildTools.ant.xml (task: "build number")

	private static final int VERSION_MAJOR = 1;
	private static final int VERSION_MINOR = 0;
	private static final int VERSION_SECURITY = 1;

	private static final String PRE_RELEASE_IDENTIFIER = "rc1";

	// ------------------------------------------------------------------------

	private static final int BUILD_NUMBER = 281;
	private static final String BUILD_DATE = "20240208";
	private static final String BUILD_TIME = "183635";

	// ========================================================================
	// = END OF GENERATED VALUES - DO NOT EDIT ================================
	// ========================================================================

	private static final String APPLICATION_VERSION_STRING
		= String.format("%d.%d.%d-%s-%d-%s.%s",
			VERSION_MAJOR, VERSION_MINOR, VERSION_SECURITY,
			PRE_RELEASE_IDENTIFIER,
			BUILD_NUMBER,
			BUILD_DATE, BUILD_TIME
	);

	public static final Runtime.Version APPLICATION_VERSION
		= Runtime.Version.parse( APPLICATION_VERSION_STRING );

	// ========================================================================
	// = Builtin ==============================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	// - Constants ------------------------------------------------------------
	// ------------------------------------------------------------------------

	private static final Constant[] builtinConstants =
	{
		new Constant( "false",			true, FALSE ),
		new Constant( "true",				true, TRUE ),
		new Constant( "NaN",				true, Double.NaN ),
		new Constant( "PositiveInfinity",	true, Double.POSITIVE_INFINITY ),
		new Constant( "NegativeInfinity",	true, Double.NEGATIVE_INFINITY ),
		new Constant( "pi",				true, Math.PI ),
		new Constant( "e",				true, Math.E ),
	};

	// ------------------------------------------------------------------------
	// - Variables ------------------------------------------------------------
	// ------------------------------------------------------------------------

	private final Variable ans
		= new Variable( ANS_VARIABLE_NAME, true );

	private final Variable tmp
		= new Variable( TMP_VARIABLE_NAME, true );

	private final Variable [] builtinVariables =
	{
		ans,
		tmp,
	};

	// ------------------------------------------------------------------------
	// - Named objects --------------------------------------------------------
	// ------------------------------------------------------------------------

	private final NamedObject acc
		= new NamedObject( ACCUMULATOR_NAME, true, new Accumulator() );

	private final NamedObject [] builtinNamedObjects =
	{
		acc
	};

	// ------------------------------------------------------------------------
	// - Functions ------------------------------------------------------------
	// ------------------------------------------------------------------------

	private static final Function [] builtinFunctions =
	{
		new Function0( "random",		true, Math::random ),

		new Function1( "abs",			true, Math::abs ),
		new Function1( "acos",		true, Math::acos ),
		new Function1( "asin",		true, Math::asin ),
		new Function1( "atan",		true, Math::atan ),
		new Function1( "cbrt",		true, Math::cbrt ),
		new Function1( "ceil",		true, Math::ceil ),
		new Function1( "cos",			true, Math::cos ),
		new Function1( "cosh",		true, Math::cosh ),
		new Function1( "exp",			true, Math::exp ),
		new Function1( "expm1",		true, Math::expm1 ),
		new Function1( "floor",		true, Math::floor ),
		new Function1( "exponent",    true, Math::getExponent ),
		new Function1( "log",			true, Math::log ),
		new Function1( "log10",		true, Math::log10 ),
		new Function1( "log1p",		true, Math::log1p ),
		new Function1( "nextdown",	true, Math::nextDown ),
		new Function1( "nextup",	    true, Math::nextUp ),
		new Function1( "rint",	    true, Math::rint ),
		new Function1( "signum",		true, Math::signum ),
		new Function1( "sin",			true, Math::sin ),
		new Function1( "sinh",		true, Math::sinh ),
		new Function1( "sqrt",		true, Math::sqrt ),
		new Function1( "tan",			true, Math::tan ),
		new Function1( "tanh",		true, Math::tanh ),
		new Function1( "ulp",		    true, Math::ulp ),

		new Function1( "finite",		true, (a) -> Double.isFinite( a ) ? TRUE : FALSE ),
		new Function1( "infinite",	true, (a) -> Double.isInfinite( a ) ? TRUE : FALSE ),
		new Function1( "nan",			true, (a) -> Double.isNaN( a ) ? TRUE : FALSE ),

		new Function1( "deg",			true, Math::toDegrees ),
		new Function1( "degrees",		true, Math::toDegrees ),
		new Function1( "rad",			true, Math::toRadians ),
		new Function1( "radians",		true, Math::toRadians ),

		new Function1( "cm",			true, (v) -> (v * 2.54D) ), // in -> cm
		new Function1( "centimeters",	true, (v) -> (v * 2.54D) ), // in -> cm
		new Function1( "in",			true, (v) -> (v / 2.54D) ), // cm -> m
		new Function1( "inches",		true, (v) -> (v / 2.54D) ), // cm -> m

		new Function1( "ft",			true, (v) -> (v * 3.328084) ), // m -> ft
		new Function1( "feet",		true, (v) -> (v * 3.328084) ), // m -> ft
		new Function1( "m",			true, (v) -> (v / 3.328084) ), // ft -> m
		new Function1( "meters",		true, (v) -> (v / 3.328084) ), // ft -> m

		new Function2( "atan2",		true, Math::atan2 ),
		new Function2( "hypot",		true, Math::hypot ),
		new Function2( "max",			true, Math::max ),
		new Function2( "min",			true, Math::min ),
		new Function2( "nextafter",	true, Math::nextAfter ),
		new Function2( "pow",			true, Math::pow ),
		new Function2( "scalb",		true, (d,s) -> Math.scalb( d, (int)s ) ),

		new Function3( "if",			true, (v1, v2, v3) -> (v1 == TRUE) ? v2 : v3 ),
		new Function3( "clamp",       true, Math::clamp ),
		new Function3( "fma",         true, Math::fma ),
	};

	// ========================================================================
	// = Internal state =======================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	// - Configuration --------------------------------------------------------
	// ------------------------------------------------------------------------

	private boolean exceptionOnDivideByZeroEnabled = false;
	private boolean assignationEnabled = true;
	private boolean autoCreateVariablesEnabled = false;

	// ------------------------------------------------------------------------
	// - Registered objects ---------------------------------------------------
	// ------------------------------------------------------------------------

	private final Map<String,ValueProvider> valueProviders
		= new HashMap<>();

	// ------------------------------------------------------------------------

	private final Map<String,Function0> functions0
		= new HashMap<>();

	private final Map<String,Function1> functions1
		= new HashMap<>();

	private final Map<String,Function2> functions2
		= new HashMap<>();

	private final Map<String,Function3> functions3
		= new HashMap<>();

	// ------------------------------------------------------------------------

	private final Map<String,NamedObject> namedObjects
		= new HashMap<>();

	// ------------------------------------------------------------------------
	// - Random number --------------------------------------------------------
	// ------------------------------------------------------------------------

	private final Random random = new Random();

	private final List<DiceRoll> rolls
			= new ArrayList<>();

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	/**
	 * Create a new expression evaluator.
	 */
	public ExpressionEvaluator()
	{
		for( ValueProvider valueProvider : builtinConstants )
		{
			addValueProvider( valueProvider );
		}

		for( ValueProvider valueProvider : builtinVariables )
		{
			addValueProvider( valueProvider );
		}

		//---------------------------------------------------------------------

		for( Function function : builtinFunctions )
		{
			addFunctions( function );
		}

		//---------------------------------------------------------------------

		for( NamedObject namedObject : builtinNamedObjects )
		{
			addNamedObjects( namedObject );
		}

		reset( false );
	}

	public static Runtime.Version getVersion()
	{
		return APPLICATION_VERSION;
	}

	// ========================================================================
	// = Configuration --------------------------------------------------------
	// ========================================================================

	/**
	 * Java does not throw an exception when dividing a floating point number
	 * by zero. This method enables or disables emulation of such behaviour for
	 * the evaluator.
	 *
	 * @param exceptionOnDivideByZeroEnabled true for exception on divide by zero.
	 */
	public void setExceptionOnDivideByZeroEnabled( boolean exceptionOnDivideByZeroEnabled )
	{
		log.log( Level.CONFIG, "Exception on divide by zero set to {0}", exceptionOnDivideByZeroEnabled  );

		this.exceptionOnDivideByZeroEnabled = exceptionOnDivideByZeroEnabled;
	}

	/**
	 * Checks if exception on divide by zero is enabled.
	 * @return true if evaluator throws an exception on divide by zero
	 */
	@Override
	public boolean isThrowsExceptionOnDivideByZero()
	{
		return exceptionOnDivideByZeroEnabled;
	}

	// ------------------------------------------------------------------------

	/**
	 * Enables or disables the assignation operator.
	 * @param assignationEnabled true if the assignation operator is enabled
	 */
	public void setAssignationEnabled( boolean assignationEnabled )
	{
		log.log( Level.CONFIG, "Assignation set to {0}", assignationEnabled  );

		this.assignationEnabled = assignationEnabled;
	}

	/**
	 * Checks if assignation operator is enabled.
	 * @return true if the assignation operator is enabled
	 */
	@Override
	public boolean isAssignationEnabled()
	{
		return assignationEnabled;
	}

	// ------------------------------------------------------------------------

	/**
	 * Enables or disables automatic creation of unknown variables.
	 * @param autoCreateVariablesEnabled true if automatic creation is enabled
	 */
	public void setAutoCreateVariablesEnabled( boolean autoCreateVariablesEnabled )
	{
		log.log( Level.CONFIG, "Auto create variables set to {0}", autoCreateVariablesEnabled  );

		this.autoCreateVariablesEnabled = autoCreateVariablesEnabled;
	}

	/**
	 * Checks is automatic creation of variables is enabled.
	 * @return true if automatic creation is enabled
	 */
	@Override
	public boolean isAutoCreateVariablesEnabled()
	{
		return autoCreateVariablesEnabled;
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the random number generator for this evaluator.
	 * @return the random generator instance
	 */
	@Override
	public Random getRandomNumberGenerator()
	{
		return random;
	}

	// ========================================================================
	// = State ================================================================
	// ========================================================================

	/**
	 * Reset the evaluator to its default state.
	 */
	public void reset()
	{
		reset( false );
	}

	/**
	 * Reset the evaluator to its default state. Eventually removing any
	 * variable, function or named object registered.
	 *
	 * @param hardReset also remove non-builtin objects
	 */
	public void reset( boolean hardReset )
	{
		setExceptionOnDivideByZeroEnabled( false );
		setAssignationEnabled( true );
		setAutoCreateVariablesEnabled( false );

		if( hardReset )
		{
			valueProviders.entrySet().removeIf( entry -> !((Value)entry.getValue()).isBuiltin() );

			functions0.entrySet().removeIf( entry -> !entry.getValue().isBuiltin() );
			functions1.entrySet().removeIf( entry -> !entry.getValue().isBuiltin() );
			functions2.entrySet().removeIf( entry -> !entry.getValue().isBuiltin() );
			functions3.entrySet().removeIf( entry -> !entry.getValue().isBuiltin() );

			namedObjects.entrySet().removeIf( entry -> !entry.getValue().isBuiltin() );
		}

		ans.setValue( Double.NaN );
		tmp.setValue( Double.NaN );

		((Accumulator)acc.getObject()).setReset( TRUE );

		rolls.clear();
	}

	// ------------------------------------------------------------------------

	/**
	 * Parse and execute a mathematical expression.
	 *
	 * @param expression the mathematical expression to evaluate
	 * @return the result
	 */
	public Double evaluate( String expression )
			throws EvaluateException
	{
		stack.clear();
		rolls.clear();

		try( StringReader sr = new StringReader( expression ) )
		{
			try( PushbackReader pr = new PushbackReader( sr ) )
			{
				Lexer lexer = new Lexer( pr );
				Parser parser = new Parser( lexer );
				Start ast = parser.parse();

				ast.apply( this );

				return ans.getValue();
			}
		}
		catch( IOException ex )
		{
			throw new EvaluateException( EvaluateException.Reason.IO_EXCEPTION, ex.getMessage(), ex );
		}
		catch( LexerException ex )
		{
			int pos = ex.getToken().getPos();
			int length = ex.getToken().getText().trim().length();

			EvaluateException ex2 = new EvaluateException( EvaluateException.Reason.LEXER_EXCEPTION, ex.getMessage(), ex );
			ex2.setErrorPosition( pos );
			ex2.setErrorLength( length );

			throw ex2;
		}
		catch( ParserException ex )
		{
			int pos = ex.getToken().getPos();
			int length = ex.getToken().getText().trim().length();

			EvaluateException ex2 = new EvaluateException( EvaluateException.Reason.PARSER_EXCEPTION, ex.getMessage(), ex );
			ex2.setErrorPosition( pos );
			ex2.setErrorLength( length );

			throw ex2;
		}
	}

	// ========================================================================
	// = Values management ====================================================
	// ========================================================================

	// ------------------------------------------------------------------------
	//region Constants --------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Add a constant to the evaluator. Constant can be queried for a value.
	 *
	 * @param constant the constant
	 */
	public void addConstant( Constant constant )
	{
		addValueProvider( constant );
	}

	/**
	 * Add one or more constants to the evaluator. Constant can be queried for a value.
	 *
	 * @param constants list of constants
	 */
	public void addConstants( Constant ... constants )
	{
		addValueProviders( constants );
	}

	/**
	 * Remove a constant by its name.
	 *
	 * @param name the constant name
	 */
	public void removeConstant( String name )
	{
		Constant constant = getConstant( name );
		if( constant != null )
		{
			removeValueProvider( constant );
		}
	}

	/**
	 * Remove a constant.
	 *
	 * @param constant the constant
	 */
	public void removeConstant( Constant constant )
	{
		removeValueProvider( constant );
	}

	/**
	 * Get a constant by its name.
	 *
	 * @param constantName the constant name
	 * @return the constant or null
	 */
	public Constant getConstant( String constantName )
	{
		if( getValueProvider( constantName ) instanceof Constant constant )
		{
			return constant;
		}

		return null;
	}

	/**
	 * Get a list of the constant names known by the evaluator.
	 *
	 * @return the list
	 */
	public List<String> getConstantsList()
	{
		return valueProviders.entrySet()
				.stream()
				.filter( entry -> entry.getValue() instanceof Constant )
				.map( Map.Entry::getKey )
				.collect( Collectors.toList() );
	}

	//endregion

	// ------------------------------------------------------------------------
	//region Variables --------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Add a variable to the evaluator. Variable can either be queried
	 * for a value or this value can be set.
	 *
	 * @param variable the variable
	 */
	public void addVariable( Variable variable )
	{
		addValueProvider( variable );
	}

	/**
	 * Add one or more variables to the evaluator. Variables can either be queried
	 * for a value or this value can be set.
	 *
	 * @param variables list of variables
	 */
	public void addVariables( Variable ... variables )
	{
		addValueProviders( variables );
	}

	/**
	 * Remove a variable by its name.
	 *
	 * @param name the variable name
	 */
	public void removeVariable( String name )
	{
		Variable variable = getVariable( name );
		if( variable != null )
		{
			removeValueProvider( variable );
		}
	}

	/**
	 * Remove a variable.
	 *
	 * @param variable the variable
	 */
	public void removeVariable( Variable variable )
	{
		removeValueProvider( variable );
	}

	/**
	 * Get a variable by its name.
	 *
	 * @param variableName the variable name
	 * @return the variable or null
	 */
	public Variable getVariable( String variableName )
	{
		if( getValueProvider( variableName ) instanceof Variable variable )
		{
			return variable;
		}

		return null;
	}

	/**
	 * Get a list of the variable names known by the evaluator.
	 *
	 * @return the list
	 */
	public List<String> getVariablesList()
	{
		return valueProviders.entrySet()
				.stream()
				.filter( entry -> entry.getValue() instanceof Variable )
				.map( Map.Entry::getKey )
				.collect( Collectors.toList() );
	}

	//endregion

	// ------------------------------------------------------------------------
	//region Named objects ----------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Add a named objects to the evaluator. Named object's properties
	 * can be accessed if they provide getters or setters. Getters and setters
	 * names should comply with the java naming conventions and receive or return
	 * either a double or a Double.
	 *
	 * <p/>
	 * Named object can also be used as a constant if they implement the
	 * {@link ValueProvider} interface and as a variable if they implement
	 * the {@link ValueProvider} interfaces as well.
	 *
	 * @param namedObject the named objects
	 */
	public void addNamedObject( NamedObject namedObject )
	{
		this.namedObjects.put( namedObject.getName(), namedObject );
	}

	/**
	 * Add one or more named objects to the evaluator. Named object's properties
	 * can be accessed if they provide getters or setters. Getters and setters
	 * names should comply with the java naming conventions and receive or return
	 * either a double or a Double.
	 *
	 * <p/>
	 * Named object can also be used as a constant if they implement the
	 * {@link ValueProvider} interface and as a variable if they implement
	 * the {@link ValueProvider} interfaces as well.
	 *
	 * @param namedObjects list of named objects
	 */
	public void addNamedObjects( NamedObject ... namedObjects )
	{
		for( NamedObject namedObject : namedObjects )
		{
			addNamedObject( namedObject );
		}
	}

	/**
	 * Remove a named object by name.
	 *
	 * @param objectName the named object name
	 */
	public void removeNamedObject( String objectName )
	{
		namedObjects.remove( objectName );
	}

	/**
	 * Remove a named object.
	 *
	 * @param namedObject the named object
	 */
	public void removeNamedObject( NamedObject namedObject )
	{
		namedObjects.remove( namedObject.getName() );
	}

	/**
	 * Get a named object by its name.
	 *
	 * @param objectName the name of the named object
	 * @return the named object or null
	 */
	public NamedObject getNamedObject( String objectName )
	{
		return namedObjects.get( objectName );
	}

	/**
	 * Get a list of the named objects known by the evaluator.
	 *
	 * @return the list
	 */
	public List<String> getNamedObjectsList()
	{
		List<String> list = new ArrayList<>();

		namedObjects.forEach( (k,v) -> list.add( v.getName() ) );

		return list;
	}

	//endregion

	// ------------------------------------------------------------------------
	//region Functions --------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Add a function to the evaluator. Functions should implement
	 * one of the {@link Function0}, {@link Function1}, {@link Function2} or
	 * {@link Function3} interfaces.
	 *
	 * @param function the functions
	 */
	public void addFunction( Function function )
	{
		if( function == null )
		{
			throw new NullPointerException( "Function is null" );
		}

		switch( function )
		{
			case Function0 function0 -> functions0.put( function.getName(), function0 );
			case Function1 function1 -> functions1.put( function.getName(), function1 );
			case Function2 function2 -> functions2.put( function.getName(), function2 );
			case Function3 function3 -> functions3.put( function.getName(), function3 );
			default ->
					throw new IllegalStateException( "Function of type '" + function.getClass().getName()
							+ "' unexpected" );
		}
	}

	/**
	 * Add one or more functions to the evaluator. Functions should implement
	 * one of the {@link Function0}, {@link Function1}, {@link Function2} or
	 * {@link Function3} interfaces.
	 *
	 * @param functions list of functions
	 */
	public void addFunctions( Function ... functions )
	{
		for( Function function : functions )
		{
			addFunction( function );
		}
	}

	/**
	 * Remove a function by name.
	 *
	 * @param functionName the function name
	 * @param clazz The class of the function
	 */
	public void removeFunction( String functionName, Class<? extends Function> clazz )
	{
		Function function = getFunction( functionName, clazz );
		if( function == null )
		{
			// ignored
			return;
		}

		switch( function )
		{
			case Function0 function0 -> functions0.remove( function0.getName() );
			case Function1 function1 -> functions1.remove( function1.getName() );
			case Function2 function2 -> functions2.remove( function2.getName() );
			case Function3 function3 -> functions3.remove( function3.getName() );
			default ->
					throw new IllegalStateException( "Function of type '" + function.getClass().getName()
							+ "' unexpected" );
		}
	}

	/**
	 * Remove a function.
	 *
	 * @param function the function
	 */
	public void removeFunction( Function function )
	{
		if( function == null )
		{
			// ignored
			return;
		}

		switch( function )
		{
			case Function0 function0 -> functions0.remove( function0.getName() );
			case Function1 function1 -> functions1.remove( function1.getName() );
			case Function2 function2 -> functions2.remove( function2.getName() );
			case Function3 function3 -> functions3.remove( function3.getName() );
			default ->
					throw new IllegalStateException( "Function of type '" + function.getClass().getName()
							+ "' unexpected" );
		}
	}

	/**
	 * Get a function by its name and its class.
	 *
	 * @param functionName the function name
	 * @param clazz the class of the function to find
	 * @return the function or null
	 */
	public Function getFunction( String functionName, Class<? extends Function> clazz )
	{
		if( clazz == Function0.class )
		{
			return functions0.get( functionName );
		}
		else if( clazz == Function1.class )
		{
			return functions1.get( functionName );
		}
		else if( clazz == Function2.class )
		{
			return functions2.get( functionName );
		}
		else if( clazz == Function3.class )
		{
			return functions3.get( functionName );
		}

		return null;
	}

	/**
	 * Get a list of the functions known by the evaluator. The
	 * functions are sorted by alphabetical order and their parameters
	 * count can be found with the suffix of the name. Constants
	 * exist for those suffixes in the {@link Function} interface:
	 * <ul>
	 *     <li>Function.SUFFIX_NO_PARAMETERS</li>
	 *     <li>Function.SUFFIX_1_PARAMETER</li>
	 *     <li>Function.SUFFIX_2_PARAMETERS</li>
	 *     <li>Function.SUFFIX_3_PARAMETERS</li>
	 * </ul>
	 *
	 * @return the list.
	 */
	public List<String> getFunctionsList()
	{
		List<String> list = new ArrayList<>();

		functions0.forEach( (k,v) -> list.add( v.toString() ) );
		functions1.forEach( (k,v) -> list.add( v.toString() ) );
		functions2.forEach( (k,v) -> list.add( v.toString() ) );
		functions3.forEach( (k,v) -> list.add( v.toString() ) );

		Collections.sort( list );

		return list;
	}

	// ------------------------------------------------------------------------

	@Override
	/*package*/ Map<String, Function0> getFunction0Map()
	{
		return functions0;
	}

	@Override
	/*package*/ Map<String, Function1> getFunction1Map()
	{
		return functions1;
	}
	@Override
	/*package*/ Map<String, Function2> getFunction2Map()
	{
		return functions2;
	}

	@Override
	/*package*/ Map<String, Function3> getFunction3Map()
	{
		return functions3;
	}

	//endregion

	// ========================================================================
	// = Value providers ======================================================
	// ========================================================================

	/**
	 * Add a value provider.
	 *
	 * @param valueProvider a value provider
	 */
	/*package*/ void addValueProvider( ValueProvider valueProvider )
	{
		if( ! valueProviders.containsKey( ((Value)valueProvider).getName() ) )
		{
			valueProviders.put( ((Value)valueProvider).getName(), valueProvider );
		}
	}

	/**
	 * Add a list of value providers.
	 *
	 * @param valueProviders list of value providers
	 */
	/*package*/ void addValueProviders( ValueProvider ... valueProviders )
	{
		for( ValueProvider valueProvider : valueProviders )
		{
			addValueProvider( valueProvider );
		}
	}

	/**
	 * Get a value provider by its name.
	 *
	 * @param providerName the name of the ValueProvider
	 * @return the ValueProvider
	 */
	/*package*/ ValueProvider getValueProvider( String providerName )
	{
		return valueProviders.get( providerName );
	}

	/**
	 * Remove a value provider.
	 *
	 * @param valueProvider the value provider to remove
	 */
	/*package*/ void removeValueProvider( ValueProvider valueProvider )
	{
		valueProviders.remove( ((Value)valueProvider).getName() );
	}

	// ========================================================================
	// = Last answer ==========================================================
	// ========================================================================

	/**
	 * Get the builtin "ans" variable.
	 *
	 * @return the "ans" variable
	 */
	public Variable getAns()
	{
		return ans;
	}

	// ========================================================================
	// = Dice rolls ===========================================================
	// ========================================================================

	/**
	 * Check if last evaluation returned one or more dice rolls.
	 *
	 * @return true is dice rolls are available
	 */
	public boolean hasDiceRolls()
	{
		return ! rolls.isEmpty();
	}

	/**
	 * Get the list of dice rolls from the last evaluated expression.
	 *
	 * @return the list of dice rolls
	 */
	public List<DiceRoll> getDiceRolls()
	{
		return rolls;
	}

	// ------------------------------------------------------------------------

	@Override
	/*package*/ void addDiceRoll( DiceRoll diceRoll )
	{
		rolls.add( diceRoll );
	}

	// ========================================================================
	// = Logger ===============================================================
	// ========================================================================

	private static final Logger log
		= Logger.getLogger( MethodHandles.lookup().lookupClass().getName() );
}
