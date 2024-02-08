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

package be.lmenten.math.demo.simple;

public class Line
{
	private double x;
	private double y;
	private double length;
	private double angle;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Line( double angle, double length )
	{
		this( 0, 0, angle, length );
	}

	public Line( int x, int y, double length, double angle )
	{
		this.x = x;
		this.y = y;
		this.length = length;
		this.angle = angle;
	}

	// ========================================================================
	// = Getters / Setters ====================================================
	// ========================================================================

	public double getX()
	{
		return x;
	}

	public void setX( double x )
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY( double y )
	{
		this.y = y;
	}

	// ------------------------------------------------------------------------

	public double getLength()
	{
		return length;
	}

	public void setLength( double length )
	{
		this.length = length;
	}

	public double getAngle()
	{
		return angle;
	}

	public void setAngle( double angle )
	{
		this.angle = angle;
	}

	// ------------------------------------------------------------------------

	private Double getFoo()
	{
		return 0.0d;
	}

	private void setFoo( Double value )
	{
	}
}
