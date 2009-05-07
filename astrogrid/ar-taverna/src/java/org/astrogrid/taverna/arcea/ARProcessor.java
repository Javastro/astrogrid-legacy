/**
 * 
 */
package org.astrogrid.taverna.arcea;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
	
	public String commonName;
	
	public String getCommonName() {
		return this.commonName;
	}
		
	public ARProcessor(ScuflModel model, String name, String commonName) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		this.commonName = commonName;
		logger.warn("cea in ARProcessor constructor and doing setDescription");
		try {

			if(commonName.equals("DSA")) {
				describeDSAInputs();
				describeResultOutput();
				describeDSAOutputs();
				setDescription("DSA");
			}else if(commonName.equals("VOTABLE_Fetch_Field")) {
				describeVotableInputs();
				describeResultOutput();
				setDescription("Fetch a List of Values from a particular Field gien a column or UCD");
			}
			
			
		}/*catch(NotFoundException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}*/catch (PortCreationException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (DuplicatePortNameException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}/*catch (java.net.URISyntaxException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch(ACRException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}*/
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
	
	public void setRegId(String id) {
		logger.warn("inside setRegId the id = " + id);
		InputPort []ips =  this.getInputPorts();
		for(int j = 0;j < ips.length;j++) {
			if(ips[j].getName().equals("CeaApp Ivorn")) {
				logger.warn("try setting the new defaultvalue for input port");
				ips[j].setDefaultValue(id);
				j = ips.length;
			}//if
		}//for
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
	

	private void describeResultOutput() throws PortCreationException, DuplicatePortNameException {
		OutputPort resList = new OutputPort(this,"ResultList");
		resList.getMetadata().setDescription("XML Content as a List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		resList.getMetadata().setMIMETypes(mimes); 
		resList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(resList);
	}
	
	private void describeDSAOutputs() throws PortCreationException, DuplicatePortNameException {		
		OutputPort resListID = new OutputPort(this,"ExecutionID");
		resListID.getMetadata().setDescription("Execution ID");
		List mimeID = new ArrayList();
		mimeID.add("text/plain");
		resListID.getMetadata().setMIMETypes(mimeID);
		resListID.setSyntacticType(computeType(java.lang.String.class,mimeID));
		this.addPort(resListID);
		
		OutputPort execInfo = new OutputPort(this,"ExecutionInformation");
		execInfo.getMetadata().setDescription("Execution Information");		
		execInfo.getMetadata().setMIMETypes(mimeID);
		execInfo.setSyntacticType(computeType(java.lang.String.class,mimeID));
		this.addPort(execInfo);		
	}
	
	private void describeVotableInputs() throws PortCreationException, DuplicatePortNameException {
		List mimesText = new ArrayList();
		mimesText.add("text/plain");
		InputPort votableInput = new InputPort(this,"VOTABLE");
		votableInput.getMetadata().setMIMETypes(mimesText);
		votableInput.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(votableInput);
		
		InputPort colInput = new InputPort(this,"Column or UCD");
		colInput.getMetadata().setMIMETypes(mimesText);
		colInput.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(colInput);
		
	}	
	
	
	private void describeDSAInputs() throws PortCreationException, DuplicatePortNameException {
		List mimesText = new ArrayList();
		mimesText.add("text/plain");
		InputPort inputIvorn = new InputPort(this,"CeaApp Ivorn");
		inputIvorn.getMetadata().setMIMETypes(mimesText);
		inputIvorn.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputIvorn);
		
		InputPort inputIvornCeaService = new InputPort(this,"Optional CeaService Ivorn");
		inputIvornCeaService.setOptional(true);
		inputIvornCeaService.getMetadata().setMIMETypes(mimesText);
		inputIvornCeaService.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputIvornCeaService);
		

		InputPort inputInterface = new InputPort(this,"Interface Name");
		inputInterface.setOptional(true);
		inputInterface.getMetadata().setMIMETypes(mimesText);
		inputInterface.setSyntacticType(computeType(java.lang.String.class,mimesText));
		inputInterface.setDefaultValue("ADQL");
		this.addPort(inputInterface);		
		
		InputPort input = new InputPort(this,"Query");
		input.getMetadata().setMIMETypes(mimesText);
		input.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(input);

		InputPort inputFormat = new InputPort(this,"Format");
		inputFormat.getMetadata().setMIMETypes(mimesText);
		inputFormat.setDefaultValue("VOTABLE");
		inputFormat.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputFormat);
		
		InputPort inputCeaResult = new InputPort(this,"Optional Result Saved");
		inputCeaResult.setOptional(true);
		inputCeaResult.getMetadata().setMIMETypes(mimesText);
		inputCeaResult.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputCeaResult);		
	}
	
	
	/**
	 * computes the correct type denotation for taverna.
	 * First looks whether the parameter is an array.
	 * 
	 * luckily, the number of different return types is quite limited.
	 * @param vd
	 * @return
	
	private String computeType(ParameterBean pb, List mimes) {
		//Class type = pb.getType();
		String type = pb.getType();
		StringBuffer sb = new StringBuffer();
		//@todo for momnet, am assuming we never get multi-arrays
		sb.append("'");
		for (Iterator i = mimes.iterator(); i.hasNext(); ) {
			String m = (String)i.next();
			sb.append(m);
			if (i.hasNext()) {
				sb.append(',');
			}
		}
		sb.append("'");		
		return sb.toString();
	}
	 */
	
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
