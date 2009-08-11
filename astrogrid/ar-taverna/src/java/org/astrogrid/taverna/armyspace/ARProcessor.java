/**
 * 
 */
package org.astrogrid.taverna.armyspace;

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
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

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
	
	private String commonName;
	private Myspace myspace;
	
	public ARProcessor(ScuflModel model, String name, String commonName) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		logger.warn("in ARProcessor constructor and doing setDescription");
		this.commonName = commonName;
		try {
			ACR acr = SingletonACR.getACR();
			if(myspace == null) {
				myspace = (Myspace)acr.getService(Myspace.class);
			}
			

			if(commonName.equals("VOSpace-UserInfo")) {
				//describeVospaceuUserPort();
				describeVospaceUserOutputPort();
			}else if(commonName.equals("Save")) {
				describeDirectoryPort();
				describeMainListPort();
				describeSaveOutput();
			}else if(commonName.equals("Save_For_VOTables")) {
				describeDirectoryPort();
				describeMainListPort();
				describeVOTableGrabForURLS();
				describeVOTableGrab();
				describeSaveOutput();
				describeSaveVOTableOutput();
			}else if(commonName.equals("Fetch")) {
				describeDirectoryPort();
				describeRecurseDirectories();
				describeVOTableFetch();
				//describeMapPort();
				describeResultList();
				describeResultListValue();
			}else if(commonName.equals("Fetch_String_Content")) {
				describeDirectoryPort();
				describeRecurseDirectories();
				describeVOTableFetch();
				//describeMapPort();
				describeResultList();
				describeResultListValue();
			}
			setDescription("Myspace Save or Fetch");
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
		}catch(ACRException ae) {
			ae.printStackTrace();
			throw new ProcessorCreationException(ae);
		}
		logger.info("done with constructor of ARProcessor");
	}
	
	public String getCommonName() {
		return this.commonName;
	}
	
	private String chosenDirectoryURI = null;
	
	public String getChosenDirectoryURI() {
		return this.chosenDirectoryURI;
	}
	
	public void setChosenDirectoryURI(String dirURI) {
		this.chosenDirectoryURI = dirURI;
		InputPort []ips =  this.getInputPorts();
		for(int j = 0;j < ips.length;j++) {
			if(ips[j].getName().equals("Myspace Directory or File")) {
				logger.warn("try setting the new defaultvalue for input port");
				ips[j].setDefaultValue(chosenDirectoryURI);
				j = ips.length;
			}
		}
	}
	
	private void describeMainListPort() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Object or List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(input);
	}
	
	
	private void describeRecurseDirectories() throws PortCreationException, DuplicatePortNameException, SecurityException, ServiceException, NotFoundException {
		InputPort input = new InputPort(this,"Recurse Directories");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.setDefaultValue("false");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
	}
	
	private void describeVOTableFetch() throws PortCreationException, DuplicatePortNameException, SecurityException, ServiceException, NotFoundException {
		InputPort input = new InputPort(this,"Only VOTables");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.setDefaultValue("false");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		this.addPort(input);
	}	
	
	
	private void describeDirectoryPort() throws PortCreationException, DuplicatePortNameException, SecurityException, ServiceException, NotFoundException {
		InputPort input = new InputPort(this,"Myspace Directory or File");
		List mimes = new ArrayList();
		mimes.add("text/plain");
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.lang.String.class,mimes));
		input.setDefaultValue(myspace.getHome().toString());
		this.addPort(input);
	}	
	
	private void describeVOTableGrabForURLS() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Get Column/UCD Values for URLS to Save in Myspace");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(input);
	}	
	
	private void describeVOTableGrab() throws PortCreationException, DuplicatePortNameException {
		InputPort input = new InputPort(this,"Get Column/UCD Values for Saved Myspace Names");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		input.getMetadata().setMIMETypes(mimes);
		input.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(input);
	}	
	
	private void describeSaveOutput() throws PortCreationException, DuplicatePortNameException {
		OutputPort saveList= new OutputPort(this,"Myspace URI List");
		saveList.getMetadata().setDescription("URI List of Myspace files just saved, can be used for Fetch Processor");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		saveList.getMetadata().setMIMETypes(mimes); 
		saveList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(saveList);
	}
	
	private void describeVospaceUserOutputPort() throws PortCreationException, DuplicatePortNameException {
		OutputPort homeP = new OutputPort(this,"Home");
		homeP.getMetadata().setDescription("Vospace Home");
		List mimeID = new ArrayList();
		mimeID.add("text/plain");
		homeP.getMetadata().setMIMETypes(mimeID);
		homeP.setSyntacticType(computeType(java.lang.String.class,mimeID));
		this.addPort(homeP);
		
		OutputPort commInfo = new OutputPort(this,"Community");
		commInfo.getMetadata().setDescription("Community");		
		commInfo.getMetadata().setMIMETypes(mimeID);
		commInfo.setSyntacticType(computeType(java.lang.String.class,mimeID));
		this.addPort(commInfo);		
		
		OutputPort userName = new OutputPort(this,"User Name");
		userName.getMetadata().setDescription("User Name");		
		userName.getMetadata().setMIMETypes(mimeID);
		userName.setSyntacticType(computeType(java.lang.String.class,mimeID));
		this.addPort(userName);		
	}
	
	
	private void describeSaveVOTableOutput() throws PortCreationException, DuplicatePortNameException {
		OutputPort saveList= new OutputPort(this,"Myspace URI List of VOTables");
		saveList.getMetadata().setDescription("URI List of Myspace files just saved, can be used for Fetch Processor");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		saveList.getMetadata().setMIMETypes(mimes); 
		saveList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(saveList);
	}	

	
	private void describeMapPort() throws PortCreationException, DuplicatePortNameException {
		OutputPort fetchMap= new OutputPort(this,"Map of Results");
		fetchMap.getMetadata().setDescription("Mapping of Objects/Strings fetched from myspace.");
		List mimes = computeMimes(java.util.Map.class);
		fetchMap.getMetadata().setMIMETypes(mimes); 
		fetchMap.setSyntacticType(computeType(java.util.Map.class,mimes));
		this.addPort(fetchMap);
	}
	
	private void describeResultList() throws PortCreationException, DuplicatePortNameException {
		OutputPort resList = new OutputPort(this,"ResultListName");
		resList.getMetadata().setDescription("XML Content as a List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		resList.getMetadata().setMIMETypes(mimes); 
		resList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(resList);
	}	

	private void describeResultListValue() throws PortCreationException, DuplicatePortNameException {
		OutputPort resList = new OutputPort(this,"ResultListValue");
		resList.getMetadata().setDescription("XML Content as a List");
		List mimes = new ArrayList();
		mimes.add("java/"+java.util.List.class.getName());
		resList.getMetadata().setMIMETypes(mimes); 
		resList.setSyntacticType(computeType(java.util.List.class,mimes));
		this.addPort(resList);
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
