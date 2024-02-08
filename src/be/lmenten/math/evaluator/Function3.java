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

public non-sealed class Function3
	extends Function
{
	private final DoubleTrinaryOperator operator;

	protected Function3( String name )
	{
		this( name, false, null );
	}

	public Function3( String name, DoubleTrinaryOperator operator )
	{
		this( name, false, operator );
	}

	/*package*/ Function3( String name, boolean builtin, DoubleTrinaryOperator operator )
	{
		super( name, builtin );

		this.operator = operator;
	}

	@Override
	public String getNameSuffix()
	{
		return Function.SUFFIX_3_PARAMETERS;
	}

	public Double compute( Double value1, Double value2, Double value3 )
	{
		if( operator == null )
		{
			throw new EvaluateException( EvaluateException.Reason.NO_COMPUTE_IMPLEMENTATION );
		}

		return operator.applyAsDouble( value1, value2, value3 );
	}
}
