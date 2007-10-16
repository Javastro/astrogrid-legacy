/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map;

import org.apache.log4j.Logger;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;


import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.MinorScuflModelEvent;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.Port;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.ScuflModelEvent;
import org.w3c.dom.Document;

/**	A processor that invokes a function of the Astro Runtime.
 * @todo add serialUID to each class later.
 * @author Noel Winstanley
 * @since May 24, 20065:29:54 PM
 */
public class ARProcessor extends Processor implements Serializable {

	private static Logger logger = Logger.getLogger(ARProcessor.class);
	
	private String interfaceName;
	private String ivorn;
	
	public String getInterfaceName() {
		return this.interfaceName;
	}
	
	public String getIvorn() {
		return this.ivorn;
	}
		
	public ARProcessor(ScuflModel model,String name, String ivorn, String interfaceName) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		this.interfaceName = interfaceName;
		this.ivorn = ivorn;
		logger.warn("cea_app2 in ARProcessor constructor and doing setDescription");
		try {
			ACR acr = SingletonACR.getACR();
			Applications apps = (Applications)acr.getService(Applications.class);
			logger.warn("got acr and Applications ready lets get the CeaApplication.");
			List mimesText = new ArrayList();
			mimesText.add("text/plain");
			
			List mimes = new ArrayList();
			mimes.add("java/"+java.util.List.class.getName());			
			
			CeaApplication ai = apps.getCeaApplication(new URI(ivorn));
			logger.warn("Got it lets start filling out parameters.");
			ParameterBean []params = ai.getParameters();
		    ParameterBean pb = params[0];
			InterfaceBean []interfaceBean = ai.getInterfaces();
			for(int i = 0;i < interfaceBean.length;i++ ) {
				logger.warn("Trying ot find right interface bean = " + interfaceBean[i].getName());
				if(interfaceBean[i].getName().equals(interfaceName)) {
					logger.warn("found");
					ParameterReferenceBean []pbi = interfaceBean[i].getInputs();
					ParameterReferenceBean []pbo = interfaceBean[i].getOutputs();
					i = interfaceBean.length;
					logger.warn("found it now get ParameterReferenceBeans and try to add inputs that are used for this interface.");
					for(int j = 0; j < pbi.length; j++) {
						for(int m = 0;m < params.length;m++) {
							if(params[m].getName().equals(pbi[j].getRef())) {
								pb = params[m];
							}
						}//for
						InputPort paramInput = new InputPort(this,pb.getName());
						logger.warn("the pbi getMax = " + pbi[j].getMax());
						if(pbi[j].getMax() == 0 || pbi[j].getMax() > 1) {
							logger.warn("yes put it as a list");
							//lists
							paramInput.getMetadata().setMIMETypes(mimes);
							paramInput.setSyntacticType(computeType(java.util.List.class,mimes));
							
						}else {
							//regular mimestext
							paramInput.getMetadata().setMIMETypes(mimesText);
							paramInput.setSyntacticType(computeType(java.lang.String.class,mimesText));
							
						}
						if(pbi[j].getMin() == 0) {
							paramInput.setOptional(true);
						}
						logger.warn("the default value name = " + pb.getName() + " value = " + pb.getDefaultValue());
						if(pb.getDefaultValue() != null && pb.getDefaultValue().trim().length() > 0) {
							paramInput.setDefaultValue(pb.getDefaultValue());
						}
						this.addPort(paramInput);
					}//for
					logger.warn("add optional CeaService ivorn");
					InputPort inputIvornCeaService = new InputPort(this,"Optional CeaService Ivorn");
					inputIvornCeaService.setOptional(true);
					inputIvornCeaService.getMetadata().setMIMETypes(mimesText);
					inputIvornCeaService.setSyntacticType(computeType(java.lang.String.class,mimesText));
					this.addPort(inputIvornCeaService);		
					logger.warn("each output has the option of being saved to myspace or somewhere else so describe each output as a input reference");
					for(int j = 0; j < pbo.length; j++) {
						for(int m = 0;m < params.length;m++) {
							if(params[m].getName().equals(pbo[j].getRef())) {
								pb = params[m];
							}
						}//for
						InputPort paramInput = new InputPort(this,pb.getName() + " - Ref");						
						paramInput.getMetadata().setMIMETypes(mimesText);
						paramInput.setSyntacticType(computeType(java.lang.String.class,mimesText));
						this.addPort(paramInput);
						/*
						OutputPort paramOutput = new OutputPort(this,pb.getName());
						
						//resList.getMetadata().setDescription("Results as a List, cannot be viewed");
						if(pbo[j].getMax() == 0 || pbo[j].getMax() > 1) {
							//lists
							paramOutput.getMetadata().setMIMETypes(mimes);
							paramOutput.setSyntacticType(computeType(java.util.List.class,mimes));
							
						}else {
							//regular mimestext
							paramOutput.getMetadata().setMIMETypes(mimesText);
							paramOutput.setSyntacticType(computeType(java.lang.String.class,mimesText));
							
						}
						if(pbo[j].getMin() == 0) {
							paramOutput.setOptional(true);
						}						
						this.addPort(paramOutput);		
						*/				
					}//for
				}//if
			}//for
		
			logger.warn("Done with all inputs just put ResultList and ExecutionID for outputs");
			OutputPort resList = new OutputPort(this,"ResultList");
			resList.getMetadata().setDescription("Output as a List");
			resList.getMetadata().setMIMETypes(mimes); 
			resList.setSyntacticType(computeType(java.util.List.class,mimes));
			this.addPort(resList);		
		
			OutputPort resListID = new OutputPort(this,"ExecutionID");
			resListID.getMetadata().setDescription("Execution ID");
			resListID.getMetadata().setMIMETypes(mimesText);
			resListID.setSyntacticType(computeType(java.lang.String.class,mimesText));
			this.addPort(resListID);
			
			OutputPort statusOutput = new OutputPort(this,"ExecutionInformation");
			statusOutput.getMetadata().setDescription("A description of the CEA execution such as status, time, description");
			statusOutput.getMetadata().setMIMETypes(mimesText);
			statusOutput.setSyntacticType(computeType(java.lang.String.class,mimesText));
			this.addPort(statusOutput);			

			setDescription("CEA Application");
		}/*catch(NotFoundException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}*/catch (PortCreationException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (DuplicatePortNameException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (java.net.URISyntaxException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch(ACRException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}
		logger.info("done with constructor of ARProcessor");
	}
	
	
	public void setChosenDirectoryURIADQL(String dirURI) {
		InputPort []ips =  this.getInputPorts();
		for(int j = 0;j < ips.length;j++) {
			if(ips[j].getName().equals("Query")) {
				logger.warn("try setting the new defaultvalue for input port");
				ips[j].setDefaultValue(dirURI);
				j = ips.length;
			}
		}
	}
	
	public void setChosenDirectoryURIResult(String dirURI) {
		InputPort []ips =  this.getInputPorts();
		for(int j = 0;j < ips.length;j++) {
			if(ips[j].getName().equals("Optional Result Saved")) {
				logger.warn("try setting the new defaultvalue for input port");
				ips[j].setDefaultValue(dirURI);
				j = ips.length;
			}
		}
	}	
	
	/**
	 * computes the correct type denotation for taverna.
	 * First looks whether the parameter is an array.
	 * 
	 * luckily, the number of different return types is quite limited.
	 * @param vd
	 * @return
	 */
	private String computeType(Class type, List mimes) {
		StringBuffer sb = new StringBuffer();
		if (type.isArray() || type.equals(java.util.List.class)) {
			sb.append("l(");
		}
		sb.append("'text/plain");
		/*
		for (Iterator i = mimes.iterator(); i.hasNext(); ) {
			String m = (String)i.next();
			sb.append(m);
			if (i.hasNext()) {
				sb.append(',');
			}
		}
		*/
		sb.append("'");
		if (type.isArray() || type.equals(java.util.List.class)) {
			sb.append(")");
		}
		return sb.toString();
		
	}
	
	private List computeMimes(Class javaType) {
		// Primitive types are all single strings as far as we're concerned...
		List mimes = new ArrayList();
		while(javaType.isArray()) {
			javaType = javaType.getComponentType();
		}
		if (javaType.isPrimitive()) {
		    mimes.add("text/plain");
		} 
		// Strings should be strings, oddly enough
		if (javaType.equals(String.class)) {
		    mimes.add("text/plain");
		} 
		
		if (javaType.equals(URI.class) || javaType.equals(URL.class)) {
			mimes.add("text/plain");
			mimes.add("text/x-taverna-web-url");
		}
				
		// Handle XML types
		if (javaType.equals(Document.class) || Document.class.isAssignableFrom(javaType)) {
		    mimes.add("text/xml");
		    //@todo find correct mime type here
		    mimes.add("text/votable"); // probably.
		   
		} 
		// Fallback for types we don't understand, use 'java/full.class.name'
		mimes.add("java/"+javaType.getName());
		return mimes;
	    }


	
	/*
	private List computeMimes(ParameterBean pb) {
		// Primitive types are all single strings as far as we're concerned...
		List mimes = new ArrayList();
		//Class javaType = v.getType();
		String javaType = pb.getType().toLowerCase();
		// Strings should be strings, oddly enough
		if (javaType.equals("text")) {
			 mimes.add("text/plain");
		} 
		if (javaType.equals("uri")) {
			mimes.add("text/plain");
			mimes.add("text/x-taverna-web-url");
		}
		if (javaType.equals("adql") || javaType.equals("votable")) {
		    mimes.add("text/xml");
		    //@todo find correct mime type here
		    if(javaType.equals("votable"))
		    	mimes.add("text/votable"); // probably.		   
		}else if(javaType.equals("fits")) {
			mimes.add("image/fits");
		
		}
		else {
			 mimes.add("text/plain");
			// Fallback for types we don't understand, use 'java/full.class.name'
			mimes.add("java/"+javaType);
		}
		return mimes;
	    }
	    */
	
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("Method name",getName());
		return props;
	}
	
	public String toString() {
		return getName();
	}
	
	// allows up to 10 concurrent threads.
	public int getMaximumWorkers() {
		return 10;
	}

	
}
