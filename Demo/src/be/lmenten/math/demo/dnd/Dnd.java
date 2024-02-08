package be.lmenten.math.demo.dnd;

import be.lmenten.math.evaluator.ExpressionEvaluator;
import be.lmenten.math.evaluator.NamedObject;
import be.lmenten.math.evaluator.features.DiceRoll;

// This is a simplistic sample of how to integrate Java Expression Evaluator
// in you software.

public class Dnd
{
	private static final String [] expressions =
	{
		// CON ability check against DC 15
		"1d20 + currentCharacter.CON >= 15",

		// CON ability check against DC 15 with bonus
		"1d20 + currentCharacter.CON + currentCharacter.bonus >= 15",

			// Update Ability
		"currentCharacter.CON + 2 -> currentCharacter.CON",

		// 2d8+H keeps higher roll = advantage
		// 2d12-H discards higher = disadvantage
		"2d8+H + 2d12-H + currentCharacter.DEX",
	};

	public static void main( String[] args )
	{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		evaluator.setAutoCreateVariablesEnabled( false );

		// Two dummy characters
		// PlayerCharacter implements both PropertyValue interfaces

		PlayerCharacter pc1 = new PlayerCharacter( "Leroy", -1, 0, +1, +1, +2, +2 );

		PlayerCharacter pc2 = new PlayerCharacter( "Jenkins", +2, +2, +1, +1, 0, -1 );
		pc2.setBonus( 4 );

		// The proxy also implements both PropertyValue interfaces.
		// It holds a list of all the characters and redirect get/set property calls transparently
		// to the current player. Use next() to change character.

		PlayerCharacterProxy pcx = new PlayerCharacterProxy();
		pcx.addCharacters( pc1, pc2 );

		evaluator.addNamedObject( new NamedObject( "currentCharacter", pcx ) );

		// Evaluate some expressions ...

		for( String expression : expressions )
		{
			System.out.println( "\n" );
			System.out.println( pcx.getCurrentCharacter() );

			double result = evaluator.evaluate( expression );

			System.out.println( expression + " ---> " + result );

			if( evaluator.hasDiceRolls() )
			{
				for( DiceRoll roll : evaluator.getDiceRolls() )
				{
					int [] values = roll.getRolls();
					boolean [] keptValues = roll.getKeptValues();

					System.out.print( " * Roll d" + roll.getFacesCount() + ": ");

					for( int i = 0 ; i < roll.getRollsCount() ; i++ )
					{
						System.out.print( values[i] + (keptValues[i] ? " (kept) " : " (discarded) ") );
					}

					System.out.println( " = " + roll.getResult() );
				}
			}

			System.out.println( pcx.getCurrentCharacter() );
			pcx.next();
		}
	}
}
