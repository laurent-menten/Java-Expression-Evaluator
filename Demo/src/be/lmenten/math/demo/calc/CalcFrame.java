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

package be.lmenten.math.demo.calc;

import be.lmenten.math.evaluator.ExpressionEvaluator;
import be.lmenten.math.evaluator.exceptions.EvaluateException;
import be.lmenten.math.evaluator.exceptions.FunctionNotFoundException;
import be.lmenten.math.evaluator.exceptions.IncompatibleNamedObjectException;
import be.lmenten.math.evaluator.exceptions.IncompatibleValueException;
import be.lmenten.math.evaluator.exceptions.NamedObjectNotFoundException;
import be.lmenten.math.evaluator.exceptions.ValueNotFoundException;
import be.lmenten.math.evaluator.features.DiceRoll;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;

public class CalcFrame
	extends JFrame
{
	private static final int COLUMNS = 80;

	private static final Font font = new Font( "Courier New", Font.PLAIN, 16 );

	// ------------------------------------------------------------------------

	private final ExpressionEvaluator evaluator;

	private final JCheckBoxMenuItem autoCreateVarsEnabled;
	private final JCheckBoxMenuItem assignEnabled;
	private final JCheckBoxMenuItem dbzExceptionEnabled;

	private final JTextField input;
	private final JTextArea output;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	public CalcFrame( ExpressionEvaluator evaluator )
	{
		super( "Java Expression Evaluator - Version " + ExpressionEvaluator.getVersion() );

		this.evaluator = evaluator;

		// --------------------------------------------------------------------

		input = new JTextField( COLUMNS );
		input.setFont( font );
		input.addActionListener( (ev) ->
		{
			doEvaluate();
			input.setText( "" );
		} );

		output = new JTextArea();
		output.setFont( font );
		output.setEditable( false );

		JPanel content = new JPanel();
		content.setLayout( new BorderLayout() );

		content.add( input, BorderLayout.NORTH );
		content.add( new JScrollPane( output ), BorderLayout.CENTER );

		setContentPane( content );

		// --------------------------------------------------------------------

		JMenu optionsMenu = new JMenu( "Options" );

		autoCreateVarsEnabled = new JCheckBoxMenuItem( "Auto create variables" );
		autoCreateVarsEnabled.setSelected( evaluator.isAutoCreateVariablesEnabled() );
		autoCreateVarsEnabled.addItemListener( (ev) ->
		{
			evaluator.setAutoCreateVariablesEnabled( ev.getStateChange() == ItemEvent.SELECTED );
		} );
		optionsMenu.add( autoCreateVarsEnabled );

		assignEnabled = new JCheckBoxMenuItem( "Assignation enabled" );
		assignEnabled.setSelected( evaluator.isAssignationEnabled() );
		assignEnabled.addItemListener( (ev) ->
		{
			evaluator.setAssignationEnabled( ev.getStateChange() == ItemEvent.SELECTED );
		} );
		optionsMenu.add( assignEnabled );

		dbzExceptionEnabled = new JCheckBoxMenuItem( "Exception on divide by zero" );
		dbzExceptionEnabled.setSelected( evaluator.isThrowsExceptionOnDivideByZero() );
		dbzExceptionEnabled.addItemListener( (ev) ->
		{
			evaluator.setExceptionOnDivideByZeroEnabled( ev.getStateChange() == ItemEvent.SELECTED );
		} );
		optionsMenu.add( dbzExceptionEnabled );

		// --------------------------------------------------------------------

		JMenu commandsMenu = new JMenu( "Commands" );

		JMenuItem clearMenu = new JMenuItem( "Clear" );
		clearMenu.addActionListener( (ev) -> doClear() );
		commandsMenu.add( clearMenu );

		commandsMenu.addSeparator();

		JMenuItem resetMenu = new JMenuItem( "Reset" );
		resetMenu.addActionListener( (ev) -> doReset( false ) );
		commandsMenu.add( resetMenu );

		JMenuItem hardResetMenu = new JMenuItem( "Hard reset" );
		hardResetMenu.addActionListener( (ev) -> doReset( true ) );
		commandsMenu.add( hardResetMenu );

		// --------------------------------------------------------------------

		JMenu infosMenu = new JMenu( "Informations" );

		JMenuItem constantsMenu = new JMenuItem( "Constants" );
		constantsMenu.addActionListener( this::showInfos );
		infosMenu.add( constantsMenu );

		JMenuItem variablesMenu = new JMenuItem( "Variables" );
		variablesMenu.addActionListener( this::showInfos );
		infosMenu.add( variablesMenu );

		JMenuItem functionsMenu = new JMenuItem( "Functions" );
		functionsMenu.addActionListener( this::showInfos );
		infosMenu.add( functionsMenu );

		JMenuItem objectsMenu = new JMenuItem( "Named objects" );
		objectsMenu.addActionListener( this::showInfos );
		infosMenu.add( objectsMenu );

		// --------------------------------------------------------------------

		JMenuBar menuBar = new JMenuBar();
		menuBar.add( optionsMenu );
		menuBar.add( commandsMenu );
		menuBar.add( infosMenu );

		setJMenuBar( menuBar );

		// --------------------------------------------------------------------

		pack();

		Dimension dim = getSize();
		dim.height += 500;
		setSize( dim );

		setLocationRelativeTo( null );
	}

	private void doEvaluate()
	{
		String expression = input.getText();

		if( expression.isEmpty() || expression.isBlank() )
		{
			return;
		}

		try
		{
			output.append( expression );

			double result = evaluator.evaluate( expression );

			output.append( " --> " + result + "\n" );

			if( evaluator.hasDiceRolls() )
			{
				for( DiceRoll roll : evaluator.getDiceRolls() )
				{
					int [] values = roll.getRolls();
					boolean [] keptValues = roll.getKeptValues();

					output.append( "   \u2022 " + roll.getName() + ": " );

					for( int i = 0 ; i < roll.getRollsCount() ; i++ )
					{
						output.append( values[i] + (keptValues[i] ? " (kept)  " : " (discarded)  ") );
					}

					output.append( " = " + roll.getResult() );
					output.append( "\n" );
				}
			}
		}

		catch( ValueNotFoundException | IncompatibleValueException ex )
		{
			output.append( "      *** VALUE ERROR *** " + ex.getMessage() + "\n" );
			output.append( ex.getIndicator() + "\n" );
		}

		catch( FunctionNotFoundException ex )
		{
			output.append( "      *** FUNCTION ERROR *** " + ex.getMessage() + "\n" );
			output.append( ex.getIndicator() + "\n" );
		}

		catch( NamedObjectNotFoundException | IncompatibleNamedObjectException ex )
		{
			output.append( "      *** OBJECT ERROR *** " + ex.getMessage() + "\n" );
			output.append( ex.getIndicator() + "\n" );
		}

		catch( EvaluateException ex )
		{
			switch( ex.getReason() )
			{
				case DIVIDE_BY_ZERO ->
				{
					output.append( "      *** DIVIDE BY ZERO ***\n" );
					output.append( ex.getIndicator() + "\n" );
				}

				case ASSIGNATION_DISABLED ->
				{
					output.append( "      *** ASSIGNATION DISABLED ***\n" );
					output.append( ex.getIndicator() + "\n" );
				}

				default ->
				{
					String message = ex.getReason() + "\n" + ex.getMessage();

					JOptionPane.showMessageDialog(
							this,
							message,
							"Java Expression Evaluator console",
							JOptionPane.WARNING_MESSAGE
					);

					output.append( "\n" );
				}
			}
		}

		catch( Throwable ex )
		{
			JOptionPane.showMessageDialog(
					this,
					ex.getClass().getSimpleName() + "\n" + ex.getMessage(),
					"Unexpected exception !!!",
					JOptionPane.WARNING_MESSAGE
			);
		}

		output.append( "\n" );
	}

	private void doClear()
	{
		output.setText( "" );
	}

	private void doReset( boolean hardreset )
	{
		evaluator.reset( hardreset );

		autoCreateVarsEnabled.setSelected( evaluator.isAutoCreateVariablesEnabled() );
		assignEnabled.setSelected( evaluator.isAssignationEnabled() );
		dbzExceptionEnabled.setSelected( evaluator.isThrowsExceptionOnDivideByZero() );
	}

	private void showInfos( ActionEvent ev )
	{
		List<String> list = switch( ev.getActionCommand() )
		{
			case "Constants" -> evaluator.getConstantsList();
			case "Variables" -> evaluator.getVariablesList();
			case "Functions" -> evaluator.getFunctionsList();
			case "Named objects" -> evaluator.getNamedObjectsList();
			default -> null;
		};

		output.append( ev.getActionCommand() + ":\n\n" );

		int length = 0;
		for( String item : list )
		{
			if( item.length() > length )
			{
				length = item.length();
			}
		}

		int perRow = COLUMNS / (length+2);

		int count = 0;
		for( String item : list )
		{
			output.append( String.format( "%-" + ((COLUMNS - (perRow*2)) / perRow) + "s  ", item ) );

			if( (++count % perRow) == 0 )
			{
				output.append( "\n" );
			}
		}

		output.append( "\n\n" );
	}
}
