/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.astrogrid.acr.ui.WorkflowBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;

/**
 * @author Noel Winstanley
 * @since Jun 21, 20065:37:06 PM
 */
public class ViewWorkflowTranscriptSTA extends	AbstractSTA {

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ViewWorkflowTranscriptSTA(WorkflowBuilder builder) {
		super("Transcript Viewer","Display workflow transcript in viewer","tree.gif");
		this.builder = builder;
	}
	protected final WorkflowBuilder builder;

	protected boolean checkApplicability(PreferredTransferable atom) {
		return 
			atom.isDataFlavorSupported(VoDataFlavour.WORKFLOW_TRANSCRIPT)
		  || atom.isDataFlavorSupported(VoDataFlavour.WORKFLOW_TRANSCRIPT_STRING);
	}

	public void actionPerformed(ActionEvent arg0) {
            (new BackgroundWorker(getParent(),"Launching transcript viewer") {
                protected Object construct() throws Exception {
                	String transcript = null;
                	if (getAtom().isDataFlavorSupported(VoDataFlavour.WORKFLOW_TRANSCRIPT)) {
                		InputStream is = (InputStream)getAtom().getTransferData(VoDataFlavour.WORKFLOW_TRANSCRIPT);
                		ByteArrayOutputStream bos = new ByteArrayOutputStream();
                		Piper.pipe(is,bos);
                		transcript = bos.toString();
                		is.close();
                		bos.close();
                	} else {
                		transcript = (String)getAtom().getTransferData(VoDataFlavour.WORKFLOW_TRANSCRIPT_STRING);
                	}         	                	
                    return transcript;
                }
                protected void doFinished(Object result) {
                	builder.showTranscript((String)result);  
                }
            }).start();
        }
    
    
}
