/**
 * 
 */
package org.astrogrid.taverna.armyspace;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;
import org.embl.ebi.escience.scuflui.workbench.ScavengerTree;
import org.embl.ebi.escience.scuflworkers.ScavengerHelper;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scufl.ScuflModel;

import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;


/**
 * @author Noel Winstanley
 * @since May 31, 20063:26:15 PM
 */
public class ARScavengerHelper implements ScavengerHelper {
	
	private static Logger logger = Logger.getLogger(ARScavengerHelper.class);

	public ActionListener getListener(ScavengerTree arg0) {
		final ScavengerTree s = arg0;
		logger.warn("ARScavengerhelper.getListener");
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					s.addScavenger(new ARScavenger());
				} catch (ScavengerCreationException x) {
					JOptionPane.showMessageDialog(null,
							"Unable to connect to AR (for Myspace)!\n" + x.getMessage()
							, "Failure"
							,JOptionPane.ERROR_MESSAGE);
				}
			}
			
		};
	}

	public String getScavengerDescription() {
		logger.warn("ARScavengerhelper.getScavengerDescription");

		return "Connect to AR (for Myspace)";
	}
	
	/**
	 * Returns the icon for this scavenger
	 */
	public ImageIcon getIcon() {
		logger.warn("ARScavengerhelper.getIcon");
		return new ARProcessorInfoBean().icon();
	}	
	
	
    public Set<Scavenger> getDefaults() {
    	logger.warn("ARScavengerhelper.getDefaults");
		Set<Scavenger> result = new HashSet<Scavenger>();
		try {
			result.add(new ARScavenger());
		} catch (ScavengerCreationException x) {
			JOptionPane.showMessageDialog(null,
					"Unable to connect to AR (for Myspace)!\n" + x.getMessage()
					, "Failure"
					,JOptionPane.ERROR_MESSAGE);
		}
			
		return result;
	}	
    
	public Set<Scavenger> getFromModel(ScuflModel model) {
		logger.warn("ARScavengerhelper.getFromModel");
		return new HashSet<Scavenger>();
	}    
	

}
