package be.lmenten.math.demo.simple;

import be.lmenten.math.evaluator.Constant;
import be.lmenten.math.evaluator.ExpressionEvaluator;
import be.lmenten.math.evaluator.Function;
import be.lmenten.math.evaluator.Function0;
import be.lmenten.math.evaluator.Function1;
import be.lmenten.math.evaluator.Function2;
import be.lmenten.math.evaluator.Function3;
import be.lmenten.math.evaluator.NamedObject;
import be.lmenten.math.evaluator.Variable;
import be.lmenten.math.evaluator.exceptions.EvaluateException;
import be.lmenten.math.evaluator.exceptions.FunctionNotFoundException;
import be.lmenten.math.evaluator.exceptions.IncompatibleNamedObjectException;
import be.lmenten.math.evaluator.exceptions.IncompatibleValueException;
import be.lmenten.math.evaluator.exceptions.NamedObjectNotFoundException;
import be.lmenten.math.evaluator.exceptions.ValueNotFoundException;
import be.lmenten.math.evaluator.features.DiceRoll;

public class Test
{
	static final String [] expressions =
	{
		"# --- config ---------------------------",

		"?exception_divide_by_zero",
		"1/0",
		"?no_exception_divide_by_zero",
		"1/0",

		"#",

		"?disable_assignation",
		"1 -> v",
		"?enable_assignation",
		"1 -> v",

		"# --- named objects --------------------\n" +
		"# myLine = new Line( 110, 45 )\n" +
		"# myPoint = new Point( 11, 12 )",

		"myLine.length",					// call getter for property via reflection
		"1 -> myLine.length",				// call setter for property via reflection
		"myLine.angle",						// call getter for property via reflection
		"1 -> myLine.angle",				// call setter for property via reflection

		"#",

		"myPoint.x",                        // call getProperty via interface
		"myPoint.y",                        // call getProperty via interface
		"2 -> myPoint.y",                   // call setProperty via interface
		"myPoint.y",                        // call getProperty via interface

		"#",

		"myLine.foo",						// fail because private
		"1 -> myLine.foo",					// fail because not exists

		"myLine",							// fail because Line does not implement ValueProvider
		"1 -> myLine",						// fail because Line does not implement ValueConsumer

		"myLine.angle.length",				// fail because of format

		"# --- Standard dice notation -----------",

		"d8 + d12 + 2",                     // 1 d8 + 1 d12 + 2
		"2d20",                             // 2 d20
		"2d20-L + 2d12+H",                  // 2 d20 remove lower + 2 d20 keep higher
		"3d20-2H + 3d12+2L",                // 3 d20 remove 2 highers + 3 d20 keep 2 lowers
		"2d20+2",                           // fail because no space before +2

		"# --- builtin variables & constants ----",

		"ans",								// last value (NaN initially)
		"true",								// 1.0 : constant
		"ans",								// 1.0 : always last successful result

		"#",

		"0/0",								// NaN (evaluator config changed)
		"1/0",								// Infinity
		"-1/0",								// -Infinity

		"#",

		"1#1#2",							// 1 1/2 : Fraction
		"sqrt(4)#1#2",						// fail because fractions are numbers only
		"centimeters( 1#3#8 )",				// 1 3/8 inches -> cm
		"inches( 1 )",						// 1 cm -> inches

		"# --- user functions -------------------",

		"test()",							// same name 0 parameters instance of Function0
		"test( 1 )",						//   "    "  1 parameter instance of Function1
		"test( 1, 2 )",						//   "    "  2 parameters instance of Function2
		"test( 1, 2, 3 )",					//   "    "  2 parameters instance of Function3
		"unknown( 1 )",						// fail because function 'unknown(a)' does not exist

		"#",

		"cos( x )",							// ERROR: x not known, catch handler will add variable x
		"cos( x )",							// no more exception

		"#",

		"sin( cos( tan ( pi ) ) )",			// nesting

		"#",

		"log( e )",							//
		"log10( 10 )",						//
		"min( random(), random() )",		//

		"#",

		"1",								//
		"-1",								//
		"- 1",								// ERROR: no space allowed
		"x",								//
		"-x",								//
		"- x",								// ERROR: no space allowed

		"#",

		"1 -> x",							// assignation to variable
		"1 -> pi",							// ERROR: assignation to constant
		"x",								//
		"sin( 1 ) -> x",					// assignation to variable
		"x",								//

		"#",

		"if( true, 10, 20 )",				//
		"if( false, 10, 20 )",				//
		"if( 1 = 0, 0, 10 )",				//
		"if( 1 = 1, 0, 10 )",				//
		"if( 1 < 0, 0, 10 )",				//
		"if( 1 <= 2, 0, 10 )",				//
		"if( 1 <> 0, 0, 10 )",				//
		"if( 1 <> 1, 0, 10 )",				//
		"if( 1 > 0, 0, 10 )",				//
		"if( 1 >= 2, 0, 10 )",				//

		"#",

		"if( true & false, 0, 10 )",		//
		"if( true | false, 0, 10 )",		//

		"#",

		"if( true ^ true, 0, 10 )",			//
		"if( true ^ false, 0, 10 )",		//
		"if( false ^ true, 0, 10 )",		//
		"if( false ^ false, 0, 10 )",		//

		"#",

		"test( true, @1 ) -> x",			// lexer error
		"test( true, 1 0 ) -> x",			// parser error

		"#",

		"true -> acc.reset",				// accumulator object
		"acc.sum",							// = 0 as NamedObject property (getter)
		"1 -> acc.accumulate",				// += 1 as NamedObject property (setter)
		"2 -> acc.value",					// += 2 as ValueConsumer (explicit)
		"3 -> acc",							// += 3 as ValueConsumer (implicit)
		"acc",								// = 6 as ValueProvider (implicit)

		"#",

		"?constants",
		"?variables",
		"?functions",
		"?objects",
		"?exit"
	};

	public static void main( String[] args )
	{
		ExpressionEvaluator evaluator = new ExpressionEvaluator();

		// --------------------------------------------------------------------

		Constant cc = new Constant( "c", 1.0d );
		evaluator.addConstants( cc );

		// --------------------------------------------------------------------

		Variable vv = new Variable( "v", 2.0d );
		evaluator.addVariables( vv );

		// --------------------------------------------------------------------

		Function f0 = new Function0( "test", () -> 1 );
		Function f1 = new Function1( "test", ( a) -> 2 * a );
		Function f2 = new Function2( "test", Double::sum );
		Function f3 = new Function3( "test", ( a, b, c) -> a + b * c );
		evaluator.addFunctions( f0, f1, f2, f3 );

		// --------------------------------------------------------------------

		NamedObject myLine = new NamedObject( "myLine", new Line( 110, 45) );
		evaluator.addNamedObjects( myLine );

		NamedObject myPoint = new NamedObject( "myPoint", new Point( 11, 12 ) );
		evaluator.addNamedObject( myPoint );

		// --------------------------------------------------------------------

		for( String expression : expressions )
		{
			// ----------------------------------------------------------------
			// - Skip comments ------------------------------------------------
			// ----------------------------------------------------------------

			if( expression.trim().startsWith( "#" ) )
			{
				System.out.println( "\n" + expression + "\n" );
			}

			// ----------------------------------------------------------------
			// - Commands -----------------------------------------------------
			// ----------------------------------------------------------------

			else if( expression.trim().startsWith( "?" ) )
			{
				if(  expression.trim().equals( "?exit" ) )
				{
					System.out.println( ">>> exit <<<" );
					break;
				}

				else
				{
					System.out.println( "\n" + expression + "\n" );

					switch( expression.trim() )
					{
						case "?constants" -> System.out.println( evaluator.getConstantsList() );
						case "?variables" -> System.out.println( evaluator.getVariablesList() );
						case "?functions" -> System.out.println( evaluator.getFunctionsList() );
						case "?objects" -> System.out.println( evaluator.getNamedObjectsList() );

						case "?exception_divide_by_zero" -> evaluator.setExceptionOnDivideByZeroEnabled( true );
						case "?no_exception_divide_by_zero" -> evaluator.setExceptionOnDivideByZeroEnabled( false );

						case "?enable_assignation" -> evaluator.setAssignationEnabled( true );
						case "?disable_assignation" -> evaluator.setAssignationEnabled( false );

						default -> System.out.println( "UNKNOWN COMMAND" );
					}
				}
			}

			// ----------------------------------------------------------------
			// - Evaluate -----------------------------------------------------
			// ----------------------------------------------------------------

			else
			{
				try
				{
					System.out.println( expression + " ---> " + evaluator.evaluate( expression ) );

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

					System.out.println();
				}

				catch( ValueNotFoundException | IncompatibleValueException ex )
				{
					System.out.println( expression + " *** VALUE ERROR *** " + ex.getMessage() );
					System.out.println( ex.getIndicator() );

					if( evaluator.getVariable( "x" ) == null )
					{
						Variable x = new Variable( "x", 1d );
						evaluator.addVariables( x );
					}
				}

				catch( FunctionNotFoundException ex )
				{
					System.out.println( expression + " *** FUNCTION ERROR *** " + ex.getMessage() );
					System.out.println( ex.getIndicator() );
				}

				catch( NamedObjectNotFoundException | IncompatibleNamedObjectException ex )
				{
					System.out.println( expression + " *** OBJECT ERROR *** " + ex.getMessage() );
					System.out.println( ex.getIndicator() );
				}

				catch( EvaluateException ex )
				{
					switch( ex.getReason() )
					{
						case INVALID_STATE -> System.out.println( expression + " *** EVALUATOR ERROR *** " );

						case IO_EXCEPTION -> System.out.println( expression + " *** IO ERROR *** " + ex.getMessage() );
						case LEXER_EXCEPTION -> System.out.println( expression + " *** LEXER ERROR *** " + ex.getMessage() );
						case PARSER_EXCEPTION -> System.out.println( expression + " *** PARSER ERROR *** " + ex.getMessage() );

						case DIVIDE_BY_ZERO -> System.out.println( expression + " *** DIVIDE BY ZERO *** " );
						case ASSIGNATION_DISABLED -> System.out.println( expression + " *** ASSIGNATION DISABLED *** " );

						default -> System.out.println( expression + " *** ? ERROR *** " + ex.getMessage() );
					}

					System.out.println( ex.getIndicator() );
				}

				catch( Throwable ex )
				{
					System.out.println( expression + " ??? UNEXPECTED EXCEPTION !!!\n" );
					ex.printStackTrace();
				}
			}
		}
	}}
