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

package be.lmenten.math.evaluator.features;

import be.lmenten.math.evaluator.ExpressionEvaluator;
import be.lmenten.math.evaluator.Value;
import be.lmenten.math.evaluator.ValueConsumer;
import be.lmenten.math.evaluator.ValueProvider;

public class Accumulator
	implements Value, ValueProvider, ValueConsumer
{
	private Double value;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Accumulator()
	{
		value = 0.0D;
	}

	// ========================================================================
	// =
	// ========================================================================

	public void setReset( double value )
	{
		if( value == ExpressionEvaluator.TRUE )
		{
			this.value = 0.0D;
		}
	}

	@Deprecated
	public void setAccumulate( double value )
	{
		this.value += value;
	}

	@Deprecated
	public double getSum()
	{
		return value;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public boolean isBuiltin()
	{
		return false;
	}

	// ========================================================================
	// = ValueProvider interface ==============================================
	// ========================================================================

	@Override
	public Double getValue()
	{
		return value;
	}

	// ========================================================================
	// = ValueConsumer interface ==============================================
	// ========================================================================

	@Override
	public void setValue( Double value )
	{
		this.value += value;
	}
}
