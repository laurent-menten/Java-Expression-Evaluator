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

public sealed abstract class Function
	implements Value
	permits Function0, Function1, Function2, Function3
{
	private final String name;
	private final boolean builtin;

	// ------------------------------------------------------------------------

	public static final String SUFFIX_NO_PARAMETERS = "()";
	public static final String SUFFIX_1_PARAMETER = "(a)";
	public static final String SUFFIX_2_PARAMETERS = "(a,b)";
	public static final String SUFFIX_3_PARAMETERS = "(a,b,c)";

	// ========================================================================
	// = Constructors =========================================================
	// ========================================================================

	protected Function( String name, boolean builtin )
	{
		this.name = name;
		this.builtin = builtin;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	@Override
	public String getName()
	{
		return name;
	}

	public abstract String getNameSuffix();

	@Override
	public boolean isBuiltin()
	{
		return builtin;
	}

	// ========================================================================
	// = Object interface =====================================================
	// ========================================================================

	@Override
	public String toString()
	{
		return getName() + getNameSuffix();
	}
}
