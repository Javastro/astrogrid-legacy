package org.astrogrid.activities.ar;

import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;


import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class AstrogridRuntimeActivity extends AbstractAsynchronousActivity<AstrogridRuntimeActivityConfigurationBean> {

	AstrogridRuntimeActivityConfigurationBean configurationBean;	
	
	@Override
	public void configure(AstrogridRuntimeActivityConfigurationBean configurationBean)
			throws ActivityConfigurationException {
		this.configurationBean = configurationBean;
		generatePorts();
	}
	
	@Override
	public AstrogridRuntimeActivityConfigurationBean getConfiguration() {
		return configurationBean;
	}
	
	//current samples from soaplabactivity
	public void executeAsynch(final Map<String, T2Reference> data,
			final AsynchronousActivityCallback callback) {
		
	}
		
	private void generatePorts() throws ActivityConfigurationException {
		
		/*
		DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(modules[i].getName());
		ComponentDescriptor[] components = modules[i].getComponents();
				for (int j = 0; j < components.length; j++) {
					DefaultMutableTreeNode componentNode = new DefaultMutableTreeNode(components[j].getName());
					logger.warn("2222adding component name = " + components[j].getName());
					moduleNode.add(componentNode);
					MethodDescriptor[] methods = components[j].getMethods();
					for (int k = 0; k < methods.length; k++) {
						DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(
								new ARProcessorFactory(modules[i], components[j],methods[k])
								);
						componentNode.add(methodNode);
					}								
				}
		ValueDescriptor vd = methD.getReturnValue();
		if (! vd.getClass().equals(Void.class)) {
		OutputPort result = new OutputPort(this,"result");
		describePort(result,vd);
		this.addPort(result);
		}
		ValueDescriptor[] params = methD.getParameters();
		for (int i = 0; i <params.length; i++) {
			InputPort input = new InputPort(this,params[i].getName());
			describePort(input,params[i]);
			this.addPort(input);
		}
									
			*/

		
	}
	
	

	
	
	
}
