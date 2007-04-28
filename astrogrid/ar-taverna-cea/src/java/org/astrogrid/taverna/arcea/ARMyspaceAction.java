package org.astrogrid.taverna.arcea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import java.net.URI;

import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scuflui.shared.ScuflModelSet;
import org.embl.ebi.escience.scuflui.spi.ProcessorActionSPI;
import org.embl.ebi.escience.scuflworkers.ProcessorHelper;

public class ARMyspaceAction implements ProcessorActionSPI
{

	ARProcessor processor;
	
	public ActionListener getListener(Processor processor) {
		this.processor=(ARProcessor)processor;
	return new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    handleAction();
		}
	    };	
	}

	private void handleAction() {
		//ScuflModel model= processor.getInternalModel();				
		//ScuflModelSet.getInstance().addModel(model);
		try {
			URI chosenRes = SingletonACR.getURI("Choose Resource");
			processor.setChosenDirectoryURIADQL(chosenRes.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		

	public boolean canHandle(Processor processor) {
		return (processor instanceof ARProcessor);
	}

	public String getDescription() {
		return "Set the Myspace ADQL ID";
	}

	public ImageIcon getIcon() {
		return new ARProcessorInfoBean().icon();
	}

}
