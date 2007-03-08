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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	private static final Log logger = LogFactory.getLog(ARProcessor.class);	
	
	private String methodName;
	private URI ceaServiceIvorn;
	private URI ceaAppIvorn;
	private String ceaInterface;
	
	public ARProcessor(ScuflModel model, String ceaAppIvorn, String ceaServiceIvorn, String ceaInterface) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,(ceaAppIvorn.substring(6) + "!!!!"  + ceaInterface + "!!!!" + ceaServiceIvorn));
		logger.info("in ARProcessor constructor and doing setDescription");
		this.methodName = (ceaAppIvorn.substring(6) + "-"  + ceaInterface);
		try {
			if(ceaServiceIvorn != null) {
				this.ceaServiceIvorn = new URI(ceaServiceIvorn);
			}
			
			this.ceaAppIvorn = new URI(ceaAppIvorn);
			this.ceaInterface = ceaInterface;
			ACR acr = SingletonACR.getACR();			
			Applications apps = (Applications)acr.getService(Applications.class);
			CeaApplication cea = apps.getCeaApplication(this.ceaAppIvorn);
			InterfaceBean []ib = cea.getInterfaces();
			InterfaceBean ceaInterfaceBean = null;
			for(int i = 0;i < ib.length;i++) {
				if(ib[i].getName().equals(ceaInterface)) {
					ceaInterfaceBean = ib[i];
					i = ib.length;
				}//if
			}//for
			if(ceaInterfaceBean == null) {
				throw new NotFoundException("Canoot find Interface for cea app");
			}
			
			ParameterReferenceBean []prb = ceaInterfaceBean.getOutputs();
			ParameterBean []pb = cea.getParameters();
			for(int i = 0;i < prb.length;i++) {
				for(int j = 0;j < pb.length ;j++) {
					if(prb[i].getRef().equals(pb[j].getName())) {
						//OutputPort result = new OutputPort(this,pb[j].getUiName());
						InputPort resultO = new InputPort(this,pb[j].getUiName());
						
						describePort(resultO,pb[j]);
						this.addPort(resultO);
					}
				}//for
			}//for
	
			OutputPort resultMap= new OutputPort(this,"resultMap");
			describeMapPort(resultMap);
			this.addPort(resultMap);
			
			ParameterBean executionBean = new ParameterBean("executionID","Execution ID","ID number given for the execution and can be used in the AR for lookups or other tasks",
					null,null,null,"integer",null,null);
			OutputPort result = new OutputPort(this,executionBean.getUiName());
			describePort(result,executionBean);
			this.addPort(result);
			
			ParameterReferenceBean []prbInputs = ceaInterfaceBean.getInputs();
			ParameterBean []pbInputs = cea.getParameters();
			for(int i = 0;i < prbInputs.length;i++) {
				for(int j = 0;j < pbInputs.length ;j++) {
					if(prb[i].getRef().equals(pbInputs[j].getName())) {
						InputPort input = new InputPort(this,pb[j].getUiName());
						describePort(input,pb[j]);
						this.addPort(input);
					}
				}//for
			}//for
			ParameterBean processBean = new ParameterBean("processUntilFinished","ProcessUntilFinished","Default True waits till the application is done and returns outputs, setting to false will have all outputs as null except Execution ID and requires the user to deal with everything via the ID",
					null,"true",null,"boolean",null,null);
			InputPort input = new InputPort(this,processBean.getUiName());
			describePort(input,processBean);
			this.addPort(input);	
			
			ParameterBean processBean = new ParameterBean("OutputLocation","OutputLocation","Optional Outputlocation you can place in typically an ivo:// myspace location.",
					null,"",null,"string",null,null);
			InputPort input = new InputPort(this,processBean.getUiName());
			describePort(input,processBean);
			this.addPort(input);	
			
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
	
	public URI getCeaServiceIvorn() {
		return this.ceaServiceIvorn;
	}
	
	public URI getCeaAppIvorn() {
		return this.ceaAppIvorn;
	}
	
	public String getCeaInterface() {
		return this.ceaInterface;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
	private void describePort(Port p,ParameterBean pb) {
		logger.info("start describePort in ARProcessor");
		p.getMetadata().setDescription((pb.getName() + ":" + pb.getDescription() + " --Units: " + pb.getUnits()));
		List mimes = computeMimes(pb);
		p.getMetadata().setMIMETypes(mimes); 
		p.setSyntacticType(computeType(pb,mimes));
		logger.info("end describePort in ARProcessor");
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
