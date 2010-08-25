package org.astrogrid.taverna.astrogrid_taverna_suite;

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

public class DSAActivity extends
		AbstractAsynchronousActivity<DSAActivityConfigurationBean>
		implements AsynchronousActivity<DSAActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private DSAActivityConfigurationBean configBean;

	@Override
	public void configure(DSAActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		/*
		if (configBean.getExampleString().equals("invalidExample")) {
			throw new ActivityConfigurationException(
					"Example string can't be 'invalidExample'");
		}
		*/
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// myClient = new MyClient(configBean.getExampleUri());
		// this.service = myClient.getService(configBean.getExampleString());

		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();
		// FIXME: Replace with your input and output port definitions
		describeDSAInputs();
		describeDSAOutputs();

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
				InvocationContext context = callback.getContext();
				ReferenceService referenceService = context.getReferenceService();
				//String.class, context);
				// Resolve inputs 
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				CEATask dt = new CEATask();
				dt.execute(inputs, context, outputs);
				// return map of output data, with empty index array as this is
				// the only and final result (this index parameter is used if
				// pipelining output)
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public DSAActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
