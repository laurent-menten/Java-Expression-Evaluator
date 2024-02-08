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

import be.lmenten.math.evaluator.exceptions.EvaluateException;
import be.lmenten.math.evaluator.exceptions.IncompatibleNamedObjectException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class NamedObject
	implements Value, PropertyValueConsumer, PropertyValueProvider
{
	private final String name;
	private final boolean builtin;

	// ------------------------------------------------------------------------

	private final Object object;

	// ========================================================================
	// = Constructors =========================================================
	// ========================================================================

	/**
	 * Constuctor for a NamedObject. This constructor is intended to be
	 * used by thrid-party code and force non-builtin status.
	 *
	 * @param name the name of the NamedObject
	 * @param object the object held by the NamedObject
	 */
	public NamedObject( String name, Object object )
	{
		this( name, false, object );
	}

	/**
	 * Constructor for a builtin NamedObject. This constructor cannot
	 * be used outside package.
	 *
	 * @param name the name of the NamedObject
	 * @param builtin is this NamedObject builtin
	 * @param object the object held by the NamedObject
	 */
	/*package*/ NamedObject( String name, boolean builtin, Object object )
	{
		this.name = name;
		this.builtin = builtin;
		this.object = object;
	}

	// ========================================================================
	// = Value interface ======================================================
	// ========================================================================

	/**
	 * Get the name of this named object
	 *
	 * @return the name of the named object
	 */
	@Override
	public final String getName()
	{
		return name;
	}

	/**
	 * Is this named object a builtin object.
	 *
	 * @return is this named object builtin
	 */
	@Override
	public final boolean isBuiltin()
	{
		return builtin;
	}

	// ========================================================================
	// = Named object interface ===============================================
	// ========================================================================

	/**
	 * Get the object held by this named object.
	 *
	 * @return the object held by the named object
	 */
	public Object getObject()
	{
		return object;
	}

	// ========================================================================
	// = PropertyValueConsumer & PropertyValueProvider interfaces =============
	// ========================================================================

	@Override
	public Double getPropertyValue( String propertyName )
	{
		if( object instanceof PropertyValueProvider propertyValueProvider )
		{
			return propertyValueProvider.getPropertyValue( propertyName );
		}

		else
		{
			Double n;

			try
			{
				String propertyGetterName = "get"
					+ propertyName.substring( 0, 1 ).toUpperCase()
					+ propertyName.substring( 1 )
					;

				Method method = object.getClass().getMethod( propertyGetterName );
				if( method.getReturnType() != Double.class && method.getReturnType() != double.class)
				{
					throw new IncompatibleNamedObjectException( this, propertyName, false, 0, 0 );
				}

				n = (Double) method.invoke( object );
			}
			catch( NoSuchMethodException ex )
			{
				throw new IncompatibleNamedObjectException( this, propertyName, false, 0, 0 );
			}
			catch( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex )
			{
				throw new EvaluateException( EvaluateException.Reason.INVOCATION_ERROR,
						"Invocation of '" + object.getClass().getSimpleName()
								+ "." + propertyName + "()' failed", ex );
			}

			return n;
		}
	}

	@Override
	public void setPropertyValue( String propertyName, Double value )
	{
		if( object instanceof PropertyValueConsumer propertyValueConsumer )
		{
			propertyValueConsumer.setPropertyValue( propertyName, value );
		}

		else
		{
			String propertySetterName = "set"
				+ propertyName.substring( 0, 1 ).toUpperCase()
				+ propertyName.substring( 1 )
				;

			try
			{
				Method method = object.getClass().getMethod( propertySetterName, Double.TYPE );
				if( (method.getReturnType() != Void.class)
						&& (method.getReturnType() != void.class) )
				{
					throw new IncompatibleNamedObjectException( this, propertyName, true, 0, 0 );
				}

				method.invoke( object, value );
			}
			catch( NoSuchMethodException ex )
			{
				throw new IncompatibleNamedObjectException( this, propertyName, true, 0, 0 );
			}
			catch( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex )
			{
				throw new EvaluateException( EvaluateException.Reason.INVOCATION_ERROR,
					"Invocation of '" + object.getClass().getSimpleName()
						+ "." + propertySetterName + "()' failed", ex );
			}
		}
	}
}
