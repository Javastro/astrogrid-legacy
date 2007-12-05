/**
 * 
 */
package org.astrogrid.taverna.arcea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorEditor;

/** dont' think this is necessary.
 * @author Noel Winstanley
 * @since May 24, 20065:28:54 PM
 */
public class AREditor implements ProcessorEditor {

	public String getEditorDescription() {
		return "Configure AR_CEA..";
	}

	public ActionListener getListener(Processor arg0) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//@todo
			}
		};
	}

}
