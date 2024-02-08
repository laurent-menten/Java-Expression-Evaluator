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

import java.util.function.DoubleUnaryOperator;

public non-sealed class Function1
	extends Function
{
	private final DoubleUnaryOperator operator;

	protected Function1( String name )
	{
		this( name, false, null );
	}

	public Function1( String name, DoubleUnaryOperator operator )
	{
		this( name, false, operator );
	}

	/*package*/ Function1( String name, boolean builtin, DoubleUnaryOperator operator )
	{
		super( name, builtin );

		this.operator = operator;
	}

	@Override
	public String getNameSuffix()
	{
		return Function.SUFFIX_1_PARAMETER;
	}

	public Double compute( Double value )
	{
		if( operator == null )
		{
			throw new EvaluateException( EvaluateException.Reason.NO_COMPUTE_IMPLEMENTATION );
		}

		return operator.applyAsDouble( value );
	}
}
