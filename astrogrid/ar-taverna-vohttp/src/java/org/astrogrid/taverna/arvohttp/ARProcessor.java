/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

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
	
	private String name;
	
	public ARProcessor(ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		logger.warn("in ARProcessor constructor and doing setDescription");
		this.name = name;
		try {
			ACR acr = SingletonACR.getACR();			
			if(name.equals("SIAP") || name.equals("CONE")) {
				describeSearchPort();
				describeRADECPort();
				describeSizePort();
				//describeOutputAsInputPort();
				//describeExecutionPort();
				//describeOutputPort();
				
			} else if(name.equals("STAP")) {
				describeSearchPort();
				describeDatePort();
				//describeOutputAsInputPort();
				//describeExecutionPort();
				//describeOutputPort();
			}
			
			OutputPort resultMap= new OutputPort(this,"result");
			describeMapPort(resultMap);
			this.addPort(resultMap);
				
			setDescription("VOHTTP");
			logger.warn("finished describing ports");
		}catch(NotFoundException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (PortCreationException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (DuplicatePortNameException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch(ACRException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}
		logger.info("done with constructor of ARProcessor");
	}
	
	public String getName() {
		return this.name;
	}
	
	private void describeOutputPort() throws PortCreationException, DuplicatePortNameException {
		OutputPort output = new OutputPort(this,"result");
		List mimes = new ArrayList();
		mimes.add("text/xml");
		mimes.add("text/votable");
		output.getMetadata().setMIMETypes(mimes);
		output.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(output);
	}
	
	private void describeSearchPort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Ivorn or Registry Keywords");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
	}
		
	private void describeOutputAsInputPort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"OutputLocation");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);

		/*
		InputPort inputProcess = new InputPort(this,"ProcessUntilFinished");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
		*/
		
	}

	private void describeRADECPort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"RA");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		mimes.add("java/java.lang.Double");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.Double.class,mimes));
		this.addPort(input);
		
		InputPort inputdec = new InputPort(this,"DEC");
		List mimesdec = new ArrayList();
		mimesdec.add("text/plain");
		mimesdec.add("java/java.lang.Double");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.Double.class,mimes));
		this.addPort(inputdec);
	}
	
	private void describeSizePort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"SIZE");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		mimes.add("java/java.lang.Double");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.Double.class,mimes));
		this.addPort(input);
	}
	
	private void describeDatePort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Start");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		mimes.add("java/java.util.Date");		
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
		
		InputPort inputEnd = new InputPort(this,"End");
		List mimesEnd = new ArrayList();
		mimes.add("text/plain");
		mimes.add("java/java.util.Date");
		inputEnd.getMetadata().setMIMETypes(mimesEnd);
		inputEnd.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(inputEnd);		
	}
	
	private void describeMapPort(Port p) {
		logger.info("start describeMapPort in ARProcessor");
		p.getMetadata().setDescription("Result Map");
		List mimes = computeMimes(java.util.Map.class);
		p.getMetadata().setMIMETypes(mimes); 
		p.setSyntacticType(computeType(java.util.Map.class,mimes));
		logger.info("end describeMapPort in ARProcessor");
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
