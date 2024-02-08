package be.lmenten.math.demo.dnd;

import be.lmenten.math.evaluator.PropertyValueConsumer;
import be.lmenten.math.evaluator.PropertyValueProvider;

class PlayerCharacter
		implements PropertyValueConsumer, PropertyValueProvider
{
	private final String name;

	private int strength;
	private int dexterity;
	private int constitution;
	private int intelligence;
	private int wisdom;
	private int charisma;

	private int bonus;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public PlayerCharacter( String name, int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma )
	{
		this.name = name;

		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.intelligence = intelligence;
		this.wisdom = wisdom;
		this.charisma = charisma;

		this.bonus = 0;
	}

	// ========================================================================
	// = PropertyValueConsumer & PropertyValueConsumer interfaces =============
	// ========================================================================

	@Override
	public Double getPropertyValue( String propertyName )
	{
		return (Double) switch( propertyName.toLowerCase() )
		{
			case "str" -> getStrength();
			case "dex" -> getDexterity();
			case "con" -> getConstitution();
			case "int" -> getIntelligence();
			case "wis" -> getWisdom();
			case "cha" -> getCharisma();
			case "bonus" -> getBonus();
			default -> PropertyValueProvider.super.getPropertyValue( propertyName );
		};
	}

	@Override
	public void setPropertyValue( String propertyName, Double value )
	{
		switch( propertyName.toLowerCase() )
		{
			case "str" -> setStrength( value.intValue() );
			case "dex" -> setDexterity( value.intValue() );
			case "con" -> setConstitution( value.intValue() );
			case "int" -> setIntelligence( value.intValue() );
			case "wis" -> setWisdom( value.intValue() );
			case "cha" -> setCharisma( value.intValue() );
			case "bonus" -> setBonus( value.intValue() );
			default -> PropertyValueProvider.super.getPropertyValue( propertyName );
		}
	}

	// ========================================================================
	// = Getters / Setters ====================================================
	// ========================================================================

	public String getName()
	{
		return name;
	}

	// ------------------------------------------------------------------------

	public int getStrength()
	{
		return strength;
	}

	public void setStrength( int strength )
	{
		this.strength = strength;
	}

	public int getDexterity()
	{
		return dexterity;
	}

	public void setDexterity( int dexterity )
	{
		this.dexterity = dexterity;
	}

	public int getConstitution()
	{
		return constitution;
	}

	public void setConstitution( int constitution )
	{
		this.constitution = constitution;
	}

	public int getIntelligence()
	{
		return intelligence;
	}

	public void setIntelligence( int intelligence )
	{
		this.intelligence = intelligence;
	}

	public int getWisdom()
	{
		return wisdom;
	}

	public void setWisdom( int wisdom )
	{
		this.wisdom = wisdom;
	}

	public int getCharisma()
	{
		return charisma;
	}

	public void setCharisma( int charisma )
	{
		this.charisma = charisma;
	}

	// ------------------------------------------------------------------------

	public int getBonus()
	{
		return bonus;
	}

	public void setBonus( int bonus )
	{
		this.bonus = bonus;
	}

	// ========================================================================
	// = Object interface =====================================================
	// ========================================================================

	@Override
	public String toString()
	{
		return "PlayerCharacter{" + "name='" + getName() + '\'' +
				", strength=" + getStrength() +
				", dexterity=" + getDexterity() +
				", constitution=" + getConstitution() +
				", intelligence=" + getIntelligence() +
				", wisdom=" + getWisdom() +
				", charisma=" + getCharisma() +
				", bonus=" + getBonus() +
				'}';
	}
}
