package org.astrogrid.taverna.arcea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import java.net.URI;
import org.astrogrid.acr.ivoa.resource.*;

import org.astrogrid.acr.builtin.ACR;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scuflui.shared.ScuflModelSet;
import org.embl.ebi.escience.scuflui.spi.ProcessorActionSPI;
import org.embl.ebi.escience.scuflworkers.ProcessorHelper;
import org.astrogrid.acr.dialogs.RegistryGoogle;

public class ARRegistryAction implements ProcessorActionSPI
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
			ACR acr = SingletonACR.getACR();
			RegistryGoogle reg = (RegistryGoogle)acr.getService(RegistryGoogle.class);
			//Resource []res = reg.selectResourcesXQueryFilter("Find DSA", false, " //vor:Resource[applicationDefinition/parameters/parameterDefinition/@id='Query']");
			
			Resource []res = reg.selectResourcesXQueryFilter("Find DSA", false, " //vor:Resource[capability/@standardID='ivo://org.astrogrid/std/CEA/v1.0' and table]");
			if(res.length > 0) {
				
				//processor.setRegId(res[0].getId().toString());
				processor.setRegId(res[0].getContent().getRelationships()[0].getRelatedResources()[0].getValue());
			}
			//processor.setChosenDirectoryURI(chosenRes.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		

	public boolean canHandle(Processor processor) {
		return (processor instanceof ARProcessor);
	}

	public String getDescription() {
		return "Find Cea from Registry";
	}

	public ImageIcon getIcon() {
		return new ARProcessorInfoBean().icon();
	}

}
