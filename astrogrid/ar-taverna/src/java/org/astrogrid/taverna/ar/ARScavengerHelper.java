/**
 * 
 */
package org.astrogrid.taverna.ar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;
import org.embl.ebi.escience.scuflui.workbench.ScavengerTree;
import org.embl.ebi.escience.scuflworkers.ScavengerHelper;

/**
 * @author Noel Winstanley
 * @since May 31, 20063:26:15 PM
 */
public class ARScavengerHelper implements ScavengerHelper {

	public ActionListener getListener(ScavengerTree arg0) {
		final ScavengerTree s = arg0;
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					s.addScavenger(new ARScavenger());
				} catch (ScavengerCreationException x) {
					JOptionPane.showMessageDialog(null,
							"Unable to connect to AR!\n" + x.getMessage()
							, "Failure"
							,JOptionPane.ERROR_MESSAGE);
				}
			}
			
		};
	}

	public String getScavengerDescription() {
		return "Connect to AR";
	}

}
