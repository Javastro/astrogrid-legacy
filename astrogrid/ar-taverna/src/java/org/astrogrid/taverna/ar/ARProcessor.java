/**
 * 
 */
package org.astrogrid.taverna.ar;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.builtin.ValueDescriptor;
import org.astrogrid.acr.system.ApiHelp;
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
	
	public ARProcessor(ScuflModel model, String name, ModuleDescriptor md, ComponentDescriptor cd, MethodDescriptor methD) throws ProcessorCreationException, DuplicateProcessorNameException {
		super(model,name);
		this.md = md;
		this.cd = cd;
		this.methD = methD;
		logger.info("in ARProcessor constructor and doing setDescription");
		setDescription(summarize(methD.getDescription()));
try{
		ValueDescriptor vd = methD.getReturnValue();
		if (! vd.getClass().equals(Void.class)) {
		OutputPort result = new OutputPort(this,"result");
		describePort(result,vd);
		this.addPort(result);
		}
		ValueDescriptor[] params = methD.getParameters();
		for (int i = 0; i <params.length; i++) {
			InputPort input = new InputPort(this,params[i].getName());
			describePort(input,params[i]);
			this.addPort(input);
		}
		logger.info("finished setting Outoput and Inputports successfully");
} catch (PortCreationException e) {
	e.printStackTrace();
	logger.error(e);
	throw new ProcessorCreationException(e);
} catch (DuplicatePortNameException e) {
	e.printStackTrace();
	logger.error(e);
	throw new ProcessorCreationException(e);
}
	logger.info("done with constructor of ARProcessor");
	}
	
	private void describePort(Port p,ValueDescriptor v) {
		logger.info("start describePort in ARProcessor");
		p.getMetadata().setDescription(v.getUitype() + " : " +v.getDescription());
		List mimes = computeMimes(v);
		p.getMetadata().setMIMETypes(mimes); 
		p.setSyntacticType(computeType(v,mimes));
		logger.info("end describePort in ARProcessor");		
	}
	
	/**
	 * computes the correct type denotation for taverna.
	 * First looks whether the parameter is an array.
	 * 
	 * luckily, the number of different return types is quite limited.
	 * @param vd
	 * @return
	 */
	private String computeType(ValueDescriptor vd, List mimes) {
		Class type = vd.getType();
		StringBuffer sb = new StringBuffer();
		if (type.isArray()) {
			sb.append("l(");
		}
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
		if (type.isArray()) {
			sb.append(")");
		}
		return sb.toString();
		
	}
	private List computeMimes(ValueDescriptor v) {
		// Primitive types are all single strings as far as we're concerned...
		List mimes = new ArrayList();
		Class javaType = v.getType();
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




	// cut at first '.' or CR
	private final String summarize(String s) {
		s = s.trim();
		int pPos = s.indexOf('.');
		int rPos = s.indexOf('\n');
		if (rPos < 0 && pPos < 0) {
			return s;
		}
		return s.substring(0,Math.min(pPos < 0 ? Integer.MAX_VALUE : pPos,
													rPos < 0 ? Integer.MAX_VALUE : rPos));
	}
	
	private final ModuleDescriptor md;
	private final ComponentDescriptor cd;
	private final MethodDescriptor methD;
	
	public Properties getProperties() {
		Properties props = new Properties();
		props.put("Method name",getMethodName());
		return props;
	}
	
	public MethodDescriptor getMethodDescriptor() {
		return methD;
	}
	
	public String getMethodName() {
		logger.info("start getMethodName in ARProcessor");

		StringBuffer sb  = new StringBuffer(md.getName())
			.append('.')
			.append(cd.getName())
			.append('.')
			.append(methD.getName());
		logger.info("end getMethodName value = " + sb.toString());

		return sb.toString();
	}
	
	public String toString() {
		return getMethodName();
	}
	
	// allows up to 10 concurrent threads.
	public int getMaximumWorkers() {
		return 10;
	}

	
}
