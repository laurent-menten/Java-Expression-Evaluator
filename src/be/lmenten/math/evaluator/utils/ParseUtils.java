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

import be.lmenten.math.evaluator.ExpressionEvaluator;
import be.lmenten.math.evaluator.exceptions.EvaluateException;
import be.lmenten.math.evaluator.features.DiceRoll;

import java.text.MessageFormat;
import java.util.Arrays;

public interface ParseUtils
{
	// ------------------------------------------------------------------------
	// - Integer number -------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 * Convert a binary number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('b'|'B') ('_' | digit2*)* digit2</li>
	 *     <li>digit2 ('_' | digit2*)* digit2 ('b'|'B')</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseBinary( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		if( string.toLowerCase().startsWith( "0b" ) )
		{
			offset = 2;
		}
		else if( string.toLowerCase().endsWith( "b" ) )
		{
			limit--;
		}

		return parseInteger( string, offset, limit,2 );
	}

	/**
	 * Convert an octal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('_' | digit8*)* digit8+</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseOctal( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		return parseInteger( string, offset, limit,8 );
	}

	/**
	 * Convert a decimal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0'</li>
	 *     <li>digit10_not0</li>
	 *     <li>digit10_not0 ('_' | digit10*)* digit10+</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	public static long parseDecimal( String string )
		throws NumberFormatException
	{
		int offset = 0;
		int limit = string.length();

		return parseInteger( string, offset, limit,10 );
	}

	/**
	 * Convert a hexadecimal number representation to its value.
	 * <p>
	 * Supported formats:
	 * <ul>
	 *     <li>'0' ('x'|'X') ('_' | digit16*)* digit16</li>
	 *     <li>'$' ('_' | digit16*)* digit16</li>
	 *     <li>'#' ('_' | digit16*)* digit16</li>
	 *     <li>digit10 ('h'|'H')</li>
	 *     <li>digit10 ('_' | digit16*)* digit16+ ('h'|'H')</li>
	 * </ul>
	 *
	 * @param string
	 * @return
	 * @throws NumberFormatException
	 */
	static long parseHexadecimal( String string )
		throws NumberFormatException
	{
		String stringLower = string.toLowerCase();

		int offset = 0;
		int limit = stringLower.length();

		if( stringLower.startsWith( "$" )
			|| stringLower.startsWith( "#" ) )
		{
			offset = 1;
		}
		else if( stringLower.startsWith( "0x" ) )
		{
			offset = 2;
		}
		else if( stringLower.endsWith( "h" ) )
		{
			limit--;
		}

		return parseInteger( string, offset, limit,16 );
	}

	// ------------------------------------------------------------------------

	/**
	 * Convert number in a substring delimited by offset and limit to its
	 * integer value according to the required radix.
	 * <p>
	 * NOTE: As this method is a support for SabledCC generated parsers, the
	 * number is supposed to be correctly formatted and no check is made to
	 * detect radix/content related problems.
	 *
	 * @param string the string containing the number representation
	 * @param offset offset of number in string (after its prefix)
	 * @param radix radix of the number representation
	 * @return the number value
	 * @throws NumberFormatException if the number representation is invalid
	 */
	private static long parseInteger( String string, int offset, int limit, int radix )
		throws NumberFormatException
	{
		long value = 0L;
		long sign = 1;

		if( radix == 10 && string.startsWith( "-" ) )
		{
			offset++;
			sign = -1;
		}

		for( int i = offset ; i < limit ; i++ )
		{
			char c = string.charAt( i );
			switch( c )
			{
				case '_':
				{
					// Ignore cosmetic separator
					break;
				}

				case '0', '1', '2', '3', '4', '5', '6', '7','8', '9':
				{
					value = (value * radix) + (c - '0');
					break;
				}

				case 'a', 'b','c','d','e','f':
				{
					value = (value * radix) + (10 + (c - 'a'));
					break;
				}

				case 'A', 'B','C','D','E','F':
				{
					value = (value * radix) + (10 + (c - 'A'));
					break;
				}

				default:
				{
					throw new NumberFormatException();
				}
			}

		}

		return sign * value;
	}

	// ------------------------------------------------------------------------
	// - Floating point number ------------------------------------------------
	// ------------------------------------------------------------------------

	static double parseFloatingPoint( String string )
		throws NumberFormatException
	{
		return Double.parseDouble( string );
	}

	static double parseFraction( String string )
	{
		double value = 0.;
		double sign = 1.;

		String [] parts = string.split( "#" );

		if( parts[0].toLowerCase().startsWith( "-" ) )
		{
			sign = -1;
			value = parseDecimal( parts[ 0 ].substring( 1 ) );
		}
		else
		{
			value = parseDecimal( parts[ 0 ] );
		}

		if( parts.length == 2 )
		{
			value = value / parseDecimal( parts[ 1 ] );
		}
		else // parts.length == 3
		{
			double frac = parseBinary( parts[ 1 ] );
			frac = frac / parseDecimal( parts[ 2 ] );

			value = value + frac;
		}

		return sign * value;
	}

	// ------------------------------------------------------------------------
	// - Dice notation --------------------------------------------------------
	// ------------------------------------------------------------------------

	static DiceRoll parseDie( String string, ExpressionEvaluator evaluator )
	{
		String stringLower = string.toLowerCase();
		int rollsCount = 1;
		int facesCount;
		int savesCount = 1;

		// --------------------------------------------------------------------

		int indexD = stringLower.indexOf( 'd' );

		int indexMinus = stringLower.indexOf( '-' );
		int indexPlus = stringLower.indexOf( '+' );

		int facesEndIndex = string.length();
		if( indexMinus != -1 || indexPlus != -1 )
		{
			facesEndIndex = Math.max( indexMinus, indexPlus );
		}

		int indexL = stringLower.indexOf( 'l' );
		int indexH = stringLower.indexOf( 'h' );
		int saveEndIndex = -1;
		if( indexL != -1 || indexH != -1 )
		{
			saveEndIndex = Math.max( indexL, indexH );
		}

		// --------------------------------------------------------------------

		if( indexD > 0) // multiple rolls
		{
			rollsCount = (int) parseInteger( string, 0, indexD, 10 );
		}

		facesCount = (int) parseInteger( string, indexD+1, facesEndIndex, 10 );

		if( indexMinus != -1 ) // drop lowest / highest
		{
			if( indexMinus + 1 != saveEndIndex ) // count specified
			{
				savesCount = (int) parseInteger( string, indexMinus + 1, saveEndIndex, 10 );
			}
		}
		else if( indexPlus != -1 ) // keep lowest / highest
		{
			if( indexPlus + 1 != saveEndIndex ) // count specified
			{
				savesCount = (int) parseInteger( string, indexPlus + 1, saveEndIndex, 10 );
			}
		}

		// --------------------------------------------------------------------

		if( (saveEndIndex != -1) && (savesCount > rollsCount) )
		{
			String message = MessageFormat.format( "saveCount {0} >= rolls count {1} in \"{2}\"",
				savesCount, rollsCount, string );

			EvaluateException ex = new EvaluateException( EvaluateException.Reason.ILLEGAL_VALUE, message );
			throw ex;
		}

		// --------------------------------------------------------------------

		int [] rolls = new int [rollsCount];

		for( int i = 0 ; i < rollsCount ; i++ )
		{
			rolls[i] = 1 + evaluator.getRandomNumberGenerator().nextInt( facesCount );
		}

		Arrays.sort( rolls );

		// --------------------------------------------------------------------

		int from = 0;
		int to = rolls.length;

		if( indexMinus != -1 )
		{
			if( indexL != -1 )
			{
				from += savesCount;
			}
			else if( indexH != -1 )
			{
				to -= savesCount;
			}
		}
		else if( indexPlus != -1)
		{
			if( indexL != -1 )
			{
				to = savesCount;
			}
			else if( indexH != -1 )
			{
				from += savesCount;
			}
		}

		// --------------------------------------------------------------------

		boolean [] keptValues = new boolean [rollsCount];
		int sum = 0;

		for( int i = from ; i < to ; i++ )
		{
			sum += rolls[i];
			keptValues[i] = true;
		}

		// --------------------------------------------------------------------

		DiceRollWriter diceRollWriter = new DiceRollWriter( string );
		diceRollWriter.setFacesCount( facesCount );
		diceRollWriter.setRolls( rolls );
		diceRollWriter.setKeptValues( keptValues );
		diceRollWriter.setResult( sum );

		return diceRollWriter;
	}
}
