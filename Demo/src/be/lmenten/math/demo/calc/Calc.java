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

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Calc
{
	public static void main( String[] args )
	{
		// --------------------------------------------------------------------
		// - Set look & feel --------------------------------------------------
		// --------------------------------------------------------------------

		UIManager.put( "control", new Color( 128, 128, 128) );
		UIManager.put( "info", new Color(128,128,128) );
		UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
		UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
		UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
		UIManager.put( "nimbusFocus", new Color(115,164,209) );
		UIManager.put( "nimbusGreen", new Color(176,179,50) );
		UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
		UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
		UIManager.put( "nimbusOrange", new Color(191,98,4) );
		UIManager.put( "nimbusRed", new Color(169,46,34) );
		UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
		UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
		UIManager.put( "text", new Color( 230, 230, 230) );

		try
		{
			for( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
			{
				if( "Nimbus".equals( info.getName() ) )
				{
					UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}

		catch( Exception ex1 )
		{
			try
			{
				UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			}
			catch( Exception ex2 )
			{
				// ignore
			}
		}

		// --------------------------------------------------------------------
		// - Expression evaluator ---------------------------------------------
		// --------------------------------------------------------------------

		ExpressionEvaluator evaluator = new ExpressionEvaluator();

		// --------------------------------------------------------------------
		// - UI ---------------------------------------------------------------
		// --------------------------------------------------------------------

		CalcFrame frame = new CalcFrame( evaluator );
		frame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		frame.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				int rc = JOptionPane.showConfirmDialog(
					frame,
					"Are you sure that you want to quit?",
					"Java Expression Evaluator console",
					JOptionPane.OK_CANCEL_OPTION
				);

				if( rc == JOptionPane.OK_OPTION )
				{
					frame.dispose();
				}
			}
		} );

		frame.setVisible( true );
	}
}
