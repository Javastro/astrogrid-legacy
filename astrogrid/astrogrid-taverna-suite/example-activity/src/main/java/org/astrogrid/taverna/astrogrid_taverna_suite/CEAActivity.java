package org.astrogrid.taverna.astrogrid_taverna_suite;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.taverna.astrogrid_taverna_suite.SingletonACR;


public class CEAActivity extends
		AbstractAsynchronousActivity<CEAActivityConfigurationBean>
		implements AsynchronousActivity<CEAActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String IN_FIRST_INPUT = "firstInput";
	private static final String IN_EXTRA_DATA = "extraData";
	private static final String OUT_MORE_OUTPUTS = "moreOutputs";
	private static final String OUT_SIMPLE_OUTPUT = "simpleOutput";
	private static final String OUT_REPORT = "report";
	
	private CEAActivityConfigurationBean configBean;

	@Override
	public void configure(CEAActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();

		// FIXME: Replace with your input and output port definitions
		
		try { 
			ACR acr = SingletonACR.getACR();
			Applications apps = (Applications)acr.getService(Applications.class);
	
			CeaApplication ai = apps.getCeaApplication(new URI(configBean.getCeaIvorn()));
			ParameterBean []params = ai.getParameters();
		    ParameterBean pb = params[0];
			InterfaceBean []interfaceBean = ai.getInterfaces();
			for(int i = 0;i < interfaceBean.length;i++ ) {
				if(interfaceBean[i].getName().equals(configBean.getCeaInterfaceName())) {
					//logger.warn("found");
					ParameterReferenceBean []pbi = interfaceBean[i].getInputs();
					ParameterReferenceBean []pbo = interfaceBean[i].getOutputs();
					i = interfaceBean.length;
					//logger.warn("found it now get ParameterReferenceBeans and try to add inputs that are used for this interface.");
					for(int j = 0; j < pbi.length; j++) {
						for(int m = 0;m < params.length;m++) {
							if(params[m].getName().equals(pbi[j].getRef())) {
								pb = params[m];
							}
						}//for
						if(pbi[j].getMax() == 0 || pbi[j].getMax() > 1) {
							addInput(pb.getName(), 1, true, null, List.class);
						}else {
							addInput(pb.getName(), 0, true, null, String.class);
						}
					}
					addInput("Optional Abort in Minutes", 0, true, null, Integer.class);
					addInput("Optional CeaService Ivorn", 0, true, null, String.class);
					
					for(int j = 0; j < pbo.length; j++) {
						for(int m = 0;m < params.length;m++) {
							if(params[m].getName().equals(pbo[j].getRef())) {
								pb = params[m];
							}
						}//for
						addInput("Output Ref " + pb.getName(), 0, true, null, String.class);
					}//for
				}//if
	
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}

			addOutput("ExecutionID", 0);
			
			addOutput("ExecutionInformation", 0);
			
			addOutput("ResultList", 1);

	}
	
	private void describeDSAInputs()  {

		addInput("CeaApp Ivorn",0,true,null,String.class);
		addInput("Optional CeaService Ivorn",0,true,null,String.class);
		
		addInput("Interface Name",0,true,null,String.class);
		//inputInterface.setDefaultValue("ADQL");

		addInput("Query",0,true,null,String.class);
		
		addInput("Format",0,true,null,String.class);
		//inputFormat.setDefaultValue("VOTABLE");
		
		addInput("Optional Result Saved",0,true,null,Boolean.class);
		//be good to set to true
		
	}
	
	
	private void describeDSAOutputs(){	
		addOutput("ExecutionID", 0);
		
		addOutput("ExecutionInformation", 0);
		
		addOutput("ResultList", 1);
		
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
				String firstInput = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT), 
						String.class, context);
				String ceaAppIvorn;
				String ceaInterfaceName;
				String optCeaServiceIvorn;
				
				
				// Support our configuration-dependendent input
				//boolean optionalPorts = configBean.getExampleString().equals("specialCase"); 
				
				List<byte[]> special = null;
				// We'll also allow IN_EXTRA_DATA to be optionally not provided
				/*
				if (optionalPorts && inputs.containsKey(IN_EXTRA_DATA)) {
					// Resolve as a list of byte[]
					special = (List<byte[]>) referenceService.renderIdentifier(
							inputs.get(IN_EXTRA_DATA), byte[].class, context);
				}
				*/
				

				// TODO: Do the actual service invocation
//				try {
//					results = this.service.invoke(firstInput, special)
//				} catch (ServiceException ex) {
//					callback.fail("Could not invoke Example service " + configBean.getExampleUri(),
//							ex);
//					// Make sure we don't call callback.receiveResult later 
//					return;
//				}

				// Register outputs
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				String simpleValue = "simple";
				T2Reference simpleRef = referenceService.register(simpleValue, 0, true, context);
				outputs.put(OUT_SIMPLE_OUTPUT, simpleRef);

				// For list outputs, only need to register the top level list
				List<String> moreValues = new ArrayList<String>();
				moreValues.add("Value 1");
				moreValues.add("Value 2");
				T2Reference moreRef = referenceService.register(moreValues, 1, true, context);
				outputs.put(OUT_MORE_OUTPUTS, moreRef);

				/*
				if (optionalPorts) {
					// Populate our optional output port					
					// NOTE: Need to return output values for all defined output ports
					String report = "Everything OK";
					outputs.put(OUT_REPORT, referenceService.register(report,
							0, true, context));
				}
				*/
				
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public CEAActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
