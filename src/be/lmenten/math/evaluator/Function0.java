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

import java.util.function.DoubleSupplier;

public non-sealed class Function0
	extends Function
{
	private final DoubleSupplier supplier;

	protected Function0( String name )
	{
		this( name, false, null );
	}

	public Function0( String name, DoubleSupplier supplier )
	{
		this( name, false, supplier );
	}

	/*package*/ Function0( String name, boolean builtin, DoubleSupplier supplier )
	{
		super( name, builtin );

		this.supplier = supplier;
	}

	@Override
	public String getNameSuffix()
	{
		return Function.SUFFIX_NO_PARAMETERS;
	}

	public Double compute()
	{
		if( supplier == null )
		{
			throw new EvaluateException( EvaluateException.Reason.NO_COMPUTE_IMPLEMENTATION );
		}

		return supplier.getAsDouble();
	}
}
