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

import be.lmenten.math.evaluator.Variable;

import java.util.Arrays;

public class DiceRoll
	extends Variable
{
	protected int facesCount;
	protected int [] rolls;
	protected boolean [] keptValues;

	protected int keptValuesCount;

	// ========================================================================
	// =
	// ========================================================================

	public DiceRoll( String expression )
	{
		super( expression  );
	}

	// ========================================================================
	// =
	// ========================================================================

	public int getFacesCount()
	{
		return facesCount;
	}

	public int getRollsCount()
	{
		return rolls.length;
	}

	public int[] getRolls()
	{
		return rolls;
	}

	public boolean[] getKeptValues()
	{
		return keptValues;
	}

	public int getKeptValuesCount()
	{
		return keptValuesCount;
	}

	public int getResult()
	{
		return getValue().intValue();
	}

	// ========================================================================
	// =
	// ========================================================================

	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();

		s.append( getName() )
			.append( " : d" ). append( getFacesCount() )
			.append( " *" ).append( getRollsCount() )
			.append( " = " )
			.append( Arrays.toString( rolls ) )
			.append( " " )
			.append( Arrays.toString( keptValues ) )
			.append( " = " )
			.append( getResult() )
			;

		return s.toString();
	}
}
