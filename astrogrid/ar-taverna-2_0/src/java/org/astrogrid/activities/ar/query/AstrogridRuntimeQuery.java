package org.astrogrid.activities.ar.query;

import net.sf.taverna.t2.partition.ActivityQuery;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;


public class AstrogridRuntimeQuery extends ActivityQuery {

	public AstrogridRuntimeQuery() {
		super("");
		
	}
	
	@Override
	public void doQuery() {
		try {
			ModuleDescriptor []md  = org.astrogrid.ar.SingletonACR.listModules();
			String opName = null;
			for(int i = 0;i < md.length;i++) {
				opName = null;
				AstrogridRuntimeActivityItem ai = new AstrogridRuntimeActivityItem(md[i]);
				ai.setCategory(md[i].getName());
				ComponentDescriptor[] components = md[i].getComponents();
				for (int j = 0; j < components.length; j++) {
					opName = components[j].getName();
					MethodDescriptor[] methods = components[j].getMethods();
					for (int k = 0; k < methods.length; k++) {
							opName += "." + methods[k].getName();
					}
					
				}
				
				/*
				ComponentDescriptor[] components = modules[i].getComponents();
						for (int j = 0; j < components.length; j++) {
							opName = components[j].getName();
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
				
				add(ai);
			}			
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		
		
		
	}
	
	
	
	
}
