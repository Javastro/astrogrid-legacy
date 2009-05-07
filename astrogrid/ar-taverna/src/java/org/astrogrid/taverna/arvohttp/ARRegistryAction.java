package org.astrogrid.taverna.arvohttp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.annotation.Resource;
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
			Resource []res = null;
			if(processor.getCommonName().equals("SIAP")) {
				res = reg.selectResourcesXQueryFilter("Find SIAP Resources", true, " //vor:Resource[((capability/@xsi:type &= '*SimpleImageAccess') or (capability/@standardID = 'ivo://ivoa.net/std/SIA' )) and ( not ( @status = 'inactive' or @status='deleted'))]");
			}else if(processor.getCommonName().equals("CONE")) {
				res = reg.selectResourcesXQueryFilter("Find Cone Resources", true, "//vor:Resource[((capability/@xsi:type &= '*ConeSearch')  or (capability/@standardID = 'ivo://ivoa.net/std/ConeSearch')) and ( not ( @status = 'inactive' or @status='deleted'))]");
			}else if(processor.getCommonName().equals("STAP")) {
				res = reg.selectResourcesXQueryFilter("Find Stap Resources", true, "//vor:Resource[((capability/@xsi:type &= '*SimpleTimeAccess') or  (capability/@standardID = 'ivo://org.astrogrid/std/STAP/v1.0')) and ( not ( @status = 'inactive' or @status='deleted'))]");
			}else if(processor.getCommonName().equals("SSAP")) {
				res = reg.selectResourcesXQueryFilter("Find Spectral Resoruces", true, "//vor:Resource[((capability/@xsi:type &= '*SimpleSpectralAccess') or  (capability/@standardID = 'ivo://ivoa.net/std/SSA' ) ) and ( not ( @status = 'inactive' or @status='deleted'))]");	
			}else {
				return;
			}
			
			
			if(res.length > 0) {
				String ivorns = "";
				for(int i = 0;i < (res.length-1);i++) {
					//ivorns[i] = res[i].getId().toString();
					ivorns += res[i].getId().toString() + ",";
				}
				ivorns += res[(res.length-1)].getId().toString();
				processor.setChosenResources(ivorns);
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
		return "Find Service from Registry";
	}

	public ImageIcon getIcon() {
		return new ARProcessorInfoBean().icon();
	}

}
