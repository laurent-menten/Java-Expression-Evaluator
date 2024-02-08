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

import java.lang.invoke.MethodHandles;
import java.util.ResourceBundle;

public class EvaluateException
	extends RuntimeException
{
	public enum Reason
	{
		INVALID_STATE,

		IO_EXCEPTION,
		LEXER_EXCEPTION,
		PARSER_EXCEPTION,

		VALUE_NOT_FOUND,
		INCOMPATIBLE_VALUE,
		FUNCTION_NOT_FOUND,
		OBJECT_NOT_FOUND,
		INCOMPATIBLE_TARGET,

		NO_COMPUTE_IMPLEMENTATION,

		ILLEGAL_VALUE,

		DIVIDE_BY_ZERO,
		ASSIGNATION_DISABLED,

		INVOCATION_ERROR
	}

	private final Reason reason;

	private int errorPosition;
	private int errorLength;

	public EvaluateException( Reason reason )
	{
		this( reason, null, null );
	}

	public EvaluateException( Reason reason, String message )
	{
		this( reason, message, null );
	}

	public EvaluateException( Reason reason, String message, Throwable cause )
	{
		super( message, cause );

		this.reason = reason;

		errorPosition = 0;
		errorLength = 0;
	}

	public Reason getReason()
	{
		return reason;
	}

	public void setErrorPosition( int errorPosition )
	{
		this.errorPosition = errorPosition;
	}

	public int getErrorPosition()
	{
		return errorPosition;
	}

	public void setErrorLength( int errorLength )
	{
		this.errorLength = errorLength;
	}

	public int getErrorLength()
	{
		return errorLength;
	}

	public String getIndicator()
	{
		int pos = Integer.max( getErrorPosition()-1, 0 );

		if( getErrorLength() > 1 )
		{
			return " ".repeat( pos ) + "~".repeat( getErrorLength() );
		}
		else
		{
			return " ".repeat( pos ) + "^";
		}
	}


	// ========================================================================
	// = i18n =================================================================
	// ========================================================================

	private static final ResourceBundle rs
		= ResourceBundle.getBundle( MethodHandles.lookup().lookupClass().getName() );

	protected static String $( String key )
	{
		return rs.getString( key );
	}
}
