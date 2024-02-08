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

/**
 * Immutable value provider.
 *
 * @Since 1.0
 * @Author <a href=mailto:laurent.menten@gmail.com>Laurent Menten</a>
 */
public final class Constant
	implements Value, ValueProvider
{
	private final String name;

	private final boolean builtin;

	// ------------------------------------------------------------------------

	private final Double value;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Constant( String name, Double value )
	{
		this( name, false, value );
	}

	/*package*/ Constant( String name, boolean builtin, Double value )
	{
		this.name = name;
		this.builtin = builtin;
		this.value = value;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final boolean isBuiltin()
	{
		return builtin;
	}

	// ========================================================================
	// = ValueProvider interface ==============================================
	// ========================================================================

	@Override
	public Double getValue()
	{
		return value;
	}
}
