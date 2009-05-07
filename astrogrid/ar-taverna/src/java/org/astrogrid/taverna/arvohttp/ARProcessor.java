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

import org.astrogrid.acr.ivoa.resource.*;

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
	private String commonName;
	
	public ARProcessor(ScuflModel model, String name, String commonName) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		logger.debug("in ARProcessor name = " + name + " commonName = " + commonName);
		this.commonName = commonName;
		//this.name = name;
		try {
			if(commonName.equals("SIAP") || commonName.equals("CONE") || commonName.equals("SSAP")) {
				describeSearchPortList();
				describeRADECPort();
				describeSizePort();
				describeSavePort();
				describeIvornList();
				describeURLList();
				describeResultList();
				describeErrorList();
				
				//describeOutputAsInputPort();
				//describeExecutionPort();
				//describeOutputPort();
				
			}else if(commonName.equals("STAP")) {
				describeSearchPortList();
				describeDatePort();
				describeSavePort();
				describeIvornList();
				describeURLList();
				describeResultList();
				describeErrorList();

				//describeOutputAsInputPort();
				//describeExecutionPort();
				//describeOutputPort();
			}else if(commonName.indexOf("RegQuery") != -1) {
				describeSearchPort();
				describeIvornList();

			}
			/*
			OutputPort resultMap= new OutputPort(this,"result");
			describeMapPort(resultMap);
			this.addPort(resultMap);
			*/
			setDescription("VOHTTP");
			logger.debug("finished describing ports");
		}catch (PortCreationException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}catch (DuplicatePortNameException e) {
			e.printStackTrace();
			throw new ProcessorCreationException(e);
		}
		logger.info("done with constructor of ARProcessor");
	}
	
	public String getCommonName() {
		return this.commonName;
	}
	
	public void setChosenResources(String ivorns) {
		//this.chosenDirectoryURI = dirURI;
		InputPort []ips =  this.getInputPorts();
		for(int j = 0;j < ips.length;j++) {
			if(ips[j].getName().equals("Ivorns")) {
				logger.warn("try setting the new defaultvalue for input port");
				ips[j].setDefaultValue(ivorns);
				j = ips.length;
			}
		}
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
	
	private void describeIvornList() throws PortCreationException, DuplicatePortNameException {
		OutputPort ivornList= new OutputPort(this,"Ivorns");
		ivornList.getMetadata().setDescription("Seperate Ivorn List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		ivornList.getMetadata().setMIMETypes(mimes); 
		ivornList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(ivornList);
	}
	
	private void describeURLList() throws PortCreationException, DuplicatePortNameException {
		OutputPort urlList= new OutputPort(this,"URLs");
		urlList.getMetadata().setDescription("URLS called ordered same as IvornList");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		urlList.getMetadata().setMIMETypes(mimes); 
		urlList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(urlList);
	}
	
	private void describeErrorList() throws PortCreationException, DuplicatePortNameException {
		OutputPort errorList= new OutputPort(this,"ErrorList");
		errorList.getMetadata().setDescription("List of Errors that occurred trying to call a service");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		errorList.getMetadata().setMIMETypes(mimes); 
		errorList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(errorList);
	}	
	
	private void describeResultList() throws PortCreationException, DuplicatePortNameException {
		OutputPort resList = new OutputPort(this,"ResultList");
		resList.getMetadata().setDescription("XML Content as a List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		resList.getMetadata().setMIMETypes(mimes); 
		resList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(resList);
	}	
	
	
	
	private void describeSearchPortList() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Ivorns");
		List mimes = new ArrayList();
		//mimes.add("text/plain");
		mimes.add("java/"+java.util.List.class.getName());		
		input.getMetadata().setMIMETypes(mimes);
		//input.setSyntacticType(computeType(java.lang.String.class,mimes));
		input.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(input);
	}
	
	private void describeSearchPort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Ivorn or Registry Keywords");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
	}
	
	private void describeSavePort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Only URLS Needed");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.setDefaultValue("true");
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
		inputdec.getMetadata().setMIMETypes(mimes);
		inputdec.setSyntacticType(computeType(java.lang.Double.class,mimesdec));
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
