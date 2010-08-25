package org.astrogrid.taverna.astrogrid_taverna_suite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.astrogrid.acr.builtin.ValueDescriptor;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.system.ApiHelp;


import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class ARActivity extends
		AbstractAsynchronousActivity<ARActivityConfigurationBean>
		implements AsynchronousActivity<ARActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private ARActivityConfigurationBean configBean;

	@Override
	public void configure(ARActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();
		// FIXME: Replace with your input and output port definitions

		

		Class type = null;
		ValueDescriptor vd = configBean.getMethodDescriptor().getReturnValue();
		if (! vd.getClass().equals(Void.class)) {
			type = vd.getType();
			if(type.isArray()) {
				addOutput("result",1);
			}else {
				addOutput("result",0);
			}
		}
		
		ValueDescriptor[] params = configBean.getMethodDescriptor().getParameters();
		for (int i = 0; i <params.length; i++) {
		 type = params[i].getType();
			if(type.isArray()) {
				addInput(params[i].getName(),1,true,null,List.class);
			}else {
				addInput(params[i].getName(),0,true,null,String.class);
			}
		}
	}
	
	
	public String getMethodName() {
		//logger.info("start getMethodName in ARProcessor");

		StringBuffer sb  = new StringBuffer(configBean.getModuleDescriptor().getName())
			.append('.')
			.append(configBean.getComponentDescriptor().getName())
			.append('.')
			.append(configBean.getMethodDescriptor().getName());
		//logger.info("end getMethodName value = " + sb.toString());

		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public void run() {
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
				// Resolve inputs 		
				try {
					ACR acr =  SingletonACR.getACR();
					ApiHelp api = (ApiHelp)acr.getService(ApiHelp.class);
					
					Class returnClass = configBean.getMethodDescriptor().getReturnValue().getType();
					while (returnClass.isArray()) {
						returnClass = returnClass.getComponentType();
					}
					
					int returnKind = isBean(returnClass) ? ApiHelp.ORIGINAL : ApiHelp.DATASTRUCTURE;
					Object result = null;
					if(!returnClass.getName().equals("java.lang.Void")) {
						//logger.warn("calling with returnkind");
						result = api.callFunction(getMethodName(),returnKind,inputObjects(inputs,context));
					}else {
						//logger.warn("try orig");
						api.callFunction(getMethodName(),ApiHelp.ORIGINAL,inputObjects(inputs,context));
					}
					
					Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
					ValueDescriptor vd = configBean.getMethodDescriptor().getReturnValue();
					if (! vd.getClass().equals(Void.class)) {
						if(vd.getType().isArray()) {
							T2Reference resultListRef = referenceService.register(result, 1, true, context);
							outputs.put("result", resultListRef);
						}else {
							T2Reference resultSimpleRef = referenceService.register(result, 0, true, context);
							outputs.put("result", resultSimpleRef);
						}
					}
					callback.receiveResult(outputs, new int[0]);
				}catch(Exception e) {
					String tmp = e.toString();
					e.printStackTrace();
					
				}
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				
			}
		});
	}
	
	//returns true if c is some kind of AR bean - various heuristics used.
	// pity we've not got a marker interface.
	private boolean isBean(Class c) {
		String n = c.getName();
		return  (! c.isInterface()) 
		&& (n.endsWith("Information") || n.endsWith("Descriptor") || n.endsWith("Bean"));
	}
		
	private Object[] inputObjects(Map<String, T2Reference> inputs,InvocationContext context)  {
		MethodDescriptor md = configBean.getMethodDescriptor();
		ValueDescriptor[] parameters = md.getParameters();	    	
		Object[] inputObjects = new Object[parameters.length];
		ReferenceService referenceService = context.getReferenceService();
		 
		for (int i = 0; i <parameters.length; i++) {
			if(parameters[i].getType().isArray()) {
				inputObjects[i] = (List) referenceService.renderIdentifier(inputs.get(parameters[i].getName()), List.class, context);
			}else {
				inputObjects[i] = (String) referenceService.renderIdentifier(inputs.get(parameters[i].getName()), String.class, context);
			}
			/*
			 * 		    if (parameterThing == null) {
			throw new Exception("Requires input name "+parameters[i].getName());
		    }

			 */
		}
		return inputObjects;	
	 }

	@Override
	public ARActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
