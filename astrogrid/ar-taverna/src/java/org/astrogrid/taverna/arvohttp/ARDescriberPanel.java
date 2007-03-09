/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.embl.ebi.escience.scufl.Processor;
//import org.embl.ebi.escience.scuflui.processoractions.AbstractProcessorAction;
import org.embl.ebi.escience.scuflui.actions.AbstractProcessorAction;

/** Shows documentation about an AR method.
 * based on org.embl.ebi.escience.scuflworkers.soaplab.SoaplabDescriberPanel
 * @author Noel Winstanley
 * @since May 30, 20063:37:41 PM
 */
public class ARDescriberPanel extends AbstractProcessorAction {

	public JComponent getComponent(Processor arg0) {
		return null;
	}

	public boolean canHandle(Processor arg0) {
		return false;
	}

	public String getDescription() {
		return null;
	}

	public ImageIcon getIcon() {
		return null;
	}

}
