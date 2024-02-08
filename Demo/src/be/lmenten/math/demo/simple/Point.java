package be.lmenten.math.demo.simple;

import be.lmenten.math.evaluator.PropertyValueConsumer;
import be.lmenten.math.evaluator.PropertyValueProvider;
import be.lmenten.math.evaluator.exceptions.PropertyNotFoundException;

public class Point
	implements PropertyValueConsumer, PropertyValueProvider
{
	private double x;
	private double y;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public Point( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	// ========================================================================
	// = PropertyValueConsumer & PropertyValueProvider interfaces =============
	// ========================================================================

	@Override
	public Double getPropertyValue( String propertyName )
	{
		return switch( propertyName )
		{
			case "x" -> getX();
			case "y" -> getY();
			default -> throw new PropertyNotFoundException();
		};
	}

	@Override
	public void setPropertyValue( String propertyName, Double value )
	{
		switch( propertyName )
		{
			case "x" -> setX( value );
			case "y" -> setY( value );
			default -> throw new PropertyNotFoundException();
		}
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
}
