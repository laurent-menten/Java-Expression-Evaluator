/*
 * ============================================================================
 * =- jDungeonMaster -=- A D&D toolbox for DMs  -=- (c) 2024+ Laurent Menten -=
 * ============================================================================
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <https://www.gnu.org/licenses/>.
 * ============================================================================
 * jDungeonMaster is unofficial Fan Content permitted under the Fan Content
 * Policy. Not approved/endorsed by Wizards. Portions of the materials used
 * are property of Wizards of the Coast. ©Wizards of the Coast LLC.
 * See <https://company.wizards.com/en/legal/fancontentpolicy> for details.
 * ============================================================================
 */

package be.lmenten.math.evaluator.exceptions;
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

import be.lmenten.math.evaluator.Value;

public class IncompatibleValueException
	extends EvaluateException
{
	private final Value value;
	private final boolean wasWriteOperation;

	public IncompatibleValueException( Value value, boolean write, int pos, int length )
	{
		super( Reason.INCOMPATIBLE_VALUE );

		setErrorPosition( pos );
		setErrorLength( length );

		this.value = value;
		this.wasWriteOperation = write;
	}

	public Value getValue()
	{
		return value;
	}
}
