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
		
	public ARProcessor(ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		logger.warn("in ARProcessor constructor and doing setDescription");
		try {

			if(name.equals("DSA")) {
				describeDSAInputs();
				describeDSAOutputs();
			}
			
			setDescription("Cea");
		}catch(NotFoundException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (PortCreationException e) {
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
	
	private void describeDSAOutputs() throws PortCreationException, DuplicatePortNameException {
		OutputPort resList = new OutputPort(this,"ResultListName");
		resList.getMetadata().setDescription("XML Content as a List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		resList.getMetadata().setMIMETypes(mimes); 
		resList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(resList);
	}	
	
	
	private void describeDSAInputs() throws PortCreationException, DuplicatePortNameException {
		List mimesText = new ArrayList();
		mimesText.add("text/plain");
		InputPort inputIvorn = new InputPort(this,"Ivorn");
		inputIvorn.getMetadata().setMIMETypes(mimesText);
		inputIvorn.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputIvorn);
		
		
		InputPort input = new InputPort(this,"ADQL Queries");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(input);

		InputPort inputFormat = new InputPort(this,"Format");
		inputFormat.getMetadata().setMIMETypes(mimesText);
		inputFormat.setSyntacticType(computeType(java.lang.String.class,mimesText));
		this.addPort(inputFormat);
	}
	
	
	/**
	 * computes the correct type denotation for taverna.
	 * First looks whether the parameter is an array.
	 * 
	 * luckily, the number of different return types is quite limited.
	 * @param vd
	 * @return
	 */
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
		if (type.isArray()) {
			sb.append("l(");
		}
		sb.append("'");
		for (Iterator i = mimes.iterator(); i.hasNext(); ) {
			String m = (String)i.next();
			sb.append(m);
			if (i.hasNext()) {
				sb.append(',');
			}
		}
		sb.append("'");
		if (type.isArray()) {
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
			/*
			 * else if(javaType.equals("binary")) {
			
				}
			 */
		}
		else {
			 mimes.add("text/plain");
			// Fallback for types we don't understand, use 'java/full.class.name'
			mimes.add("java/"+javaType);
		}
		return mimes;
	    }
	
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("Method name",getMethodName());
		return props;
	}
	
	public String toString() {
		return getMethodName();
	}
	
	// allows up to 10 concurrent threads.
	public int getMaximumWorkers() {
		return 10;
	}

	
}
