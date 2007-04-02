/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorEditor;
import org.apache.log4j.Logger;

/** dont' think this is necessary.
 * @author Noel Winstanley
 * @since May 24, 20065:28:54 PM
 */
public class AREditor implements ProcessorEditor {
	private static Logger logger = Logger.getLogger(AREditor.class);

	public String getEditorDescription() {
		logger.warn("AREDITOR");
		return "Configure AR..";
	}

	public ActionListener getListener(Processor arg0) {
		logger.warn("AREDITOR2");

		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//@todo
			}
		};
	}

}
