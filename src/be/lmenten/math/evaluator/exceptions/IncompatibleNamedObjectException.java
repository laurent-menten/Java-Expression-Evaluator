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

package be.lmenten.math.evaluator.exceptions;

import be.lmenten.math.evaluator.NamedObject;

public class IncompatibleNamedObjectException
	extends EvaluateException
{
	private final NamedObject namedObject;
	private final String propertyName;
	private final boolean wasWriteOperation;

	public IncompatibleNamedObjectException( NamedObject namedObject, String property, boolean write, int pos, int length )
	{
		super( Reason.INCOMPATIBLE_TARGET );

		setErrorPosition( pos );
		setErrorLength( length );

		this.namedObject = namedObject;
		this.propertyName = property;
		this.wasWriteOperation = write;
	}

	public IncompatibleNamedObjectException( NamedObject namedObject, boolean write, int pos, int length )
	{
		super( Reason.INCOMPATIBLE_TARGET );

		setErrorPosition( pos );
		setErrorLength( length );

		this.namedObject = namedObject;
		this.propertyName = null;
		this.wasWriteOperation = write;
	}

	public NamedObject getNamedObject()
	{
		return namedObject;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public boolean wasWriteOperation()
	{
		return wasWriteOperation;
	}

	@Override
	public String getMessage()
	{
		if( getPropertyName() != null )
		{
			return (wasWriteOperation ? "S" : "G" ) + "etter for property '" + propertyName
					+ "' of object '" + namedObject.getName()
					+ "' of type '"  + namedObject.getObject().getClass().getSimpleName()
					+ "' not found";
		}
		else
		{
			String name = namedObject != null ? namedObject.getName() : "?" ;

			if( wasWriteOperation )
			{
				return "'" + name + "' is not a value consumer";
			}
			else
			{
				return "'" + name + "' is not a not a value provider";

			}
		}
	}
}
