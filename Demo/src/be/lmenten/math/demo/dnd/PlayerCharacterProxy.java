package be.lmenten.math.demo.dnd;

import be.lmenten.math.evaluator.PropertyValueConsumer;
import be.lmenten.math.evaluator.PropertyValueProvider;

import java.util.ArrayList;
import java.util.List;

class PlayerCharacterProxy
		implements PropertyValueConsumer, PropertyValueProvider
{
	private final List<PlayerCharacter> characters
		= new ArrayList<>();

	private PlayerCharacter currentCharacter;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	PlayerCharacterProxy()
	{
	}

	// ========================================================================
	// = Characters management ================================================
	// ========================================================================

	public void addCharacters( PlayerCharacter ... characters )
	{
		for( PlayerCharacter character : characters )
		{
			if( ! this.characters.contains( character ) )
			{
				this.characters.add( character );
			}
		}

		this.currentCharacter = this.characters.getFirst();
	}

	public void next()
	{
		int x = characters.indexOf( currentCharacter );
		if( ++x >= characters.size() )
		{
			x = 0;
		}

		currentCharacter = characters.get( x );
	}

	// ========================================================================
	// = Getters / Setters ====================================================
	// ========================================================================

	public PlayerCharacter getCurrentCharacter()
	{
		return currentCharacter;
	}

	// ========================================================================
	// = PropertyValueConsumer & PropertyValueConsumer interfaces =============
	// ========================================================================

	@Override
	public Double getPropertyValue( String propertyName )
	{
		return currentCharacter.getPropertyValue( propertyName );
	}

	@Override
	public void setPropertyValue( String propertyName, Double value )
	{
		currentCharacter.setPropertyValue( propertyName, value );
	}
}
