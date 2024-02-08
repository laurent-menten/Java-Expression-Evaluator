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

package be.lmenten.math.evaluator.utils;

import be.lmenten.math.evaluator.features.DiceRoll;

/*package*/ class DiceRollWriter
	extends DiceRoll
{
	public DiceRollWriter( String expression )
	{
		super( expression );
	}

	public void setFacesCount( int facesCount )
	{
		this.facesCount =facesCount;
	}

	public void setRolls( int [] rolls )
	{
		this.rolls = rolls;
	}

	public void setKeptValues( boolean [] keptValues )
	{
		this.keptValues = keptValues;

		this.keptValuesCount = 0;
		for( boolean keptValue : keptValues )
		{
			if( keptValue )
			{
				this.keptValuesCount++;
			}
		}
	}

	public void setResult( int result )
	{
		setValue( (double) result );
	}
}