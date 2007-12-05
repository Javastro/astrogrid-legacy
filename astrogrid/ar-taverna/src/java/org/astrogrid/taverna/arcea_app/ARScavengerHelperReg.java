/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.resource.Resource;
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
public class ARScavengerHelperReg implements ScavengerHelper {

	public ActionListener getListener(ScavengerTree arg0) {
		final ScavengerTree s = arg0;
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					ACR acr = SingletonACR.getACR();
					RegistryGoogle reg = (RegistryGoogle)acr.getService(RegistryGoogle.class);
					Resource []res = reg.selectResourcesXQueryFilter("Find CeaApplication (Shows on restart of Taverna)", true, " near(@xsi:type,'*CeaApplicationType')");
					
					if(res.length > 0) {
						String []ivorns = new String[res.length];
						for(int i = 0;i < res.length;i++) {
							ivorns[i] = res[i].getId().toString();
						}
						ARScavenger.saveProperties(ivorns);
						s.addScavenger(new ARScavenger());
						//processor.setRegId(res[0].getId().toString());
					}
					//processor.setChosenDirectoryURI(chosenRes.toString());
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
		};
	}

	public String getScavengerDescription() {
		return "Find Cea Application";
	}
	
	/**
	 * Returns the icon for this scavenger
	 */
	public ImageIcon getIcon() {
		return new ARProcessorInfoBean().icon();
	}	
	
    public Set<Scavenger> getDefaults() {
		Set<Scavenger> result = new HashSet<Scavenger>();
		try {
			result.add(new ARScavenger());
		} catch (ScavengerCreationException x) {
			JOptionPane.showMessageDialog(null,
					"Unable to connect to AR-CEA!\n" + x.getMessage()
					, "Failure"
					,JOptionPane.ERROR_MESSAGE);
		}
		return result;
	}	
    
	public Set<Scavenger> getFromModel(ScuflModel model) {
		return new HashSet<Scavenger>();
	}    
	

}
