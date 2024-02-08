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

public class Variable
	implements Value, ValueConsumer, ValueProvider
{
	private final String name;
	private final boolean builtin;

	// ------------------------------------------------------------------------

	private Double value;

	// ========================================================================
	// = Constructors =========================================================
	// ========================================================================

	public Variable( String name )
	{
		this( name, false, Double.NaN );
	}

	public Variable( String  name, Double value )
	{
		this( name, false, value );
	}

	/*package*/ Variable( String  name, boolean builtin )
	{
		this( name, builtin, Double.NaN );
	}

	/*package*/ Variable( String  name, boolean builtin, Double value )
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

	// ========================================================================
	// = ValueConsumer interface ==============================================
	// ========================================================================

	@Override
	public void setValue( Double value )
	{
		this.value = value;
	}
}
