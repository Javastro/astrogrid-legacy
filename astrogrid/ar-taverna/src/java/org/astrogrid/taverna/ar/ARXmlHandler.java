/**
 * 
 */
package org.astrogrid.taverna.ar;

import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.XScufl;
import org.embl.ebi.escience.scufl.parser.XScuflFormatException;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;
import org.embl.ebi.escience.scuflworkers.XMLHandler;
import org.embl.ebi.escience.scuflworkers.soaplab.SoaplabProcessor;
import org.jdom.Element;

/**
 * @author Noel Winstanley
 * @since May 24, 20065:35:01 PM
 */
public class ARXmlHandler implements XMLHandler {
	/**
	 * Return the spec element, that is to say the processor specific portion of
	 * the processor element. For example, the soaplab implementation of this
	 * method returns the element rooted at the 'soaplabwsdl' element.
	 */
	public Element elementForFactory(ProcessorFactory arg0) {
		ARProcessorFactory arpf = (ARProcessorFactory)arg0;
		//@todo break into subelements.
		Element spec = new Element("astroruntime",XScufl.XScuflNS);
		spec.setText(arpf.getMethodName());
		return spec;
	}
	/**
	 * Return the spec element for a given ProcessorFactory. In reality each XML
	 * handler will be given only a particular subclass of the ProcessorFactory
	 * to deal with so you can reasonably cast it to your specific
	 * implementation straight off to get factory specific data out.
	 */
	public Element elementForProcessor(Processor arg0) {
		ARProcessor arp = (ARProcessor)arg0;
		Element spec = new Element("astroruntime",XScufl.XScuflNS);
		spec.setText(arp.getMethodName());
		// add as attributes any other execution-specific details.
		return spec;
	}
	/**
	 * Create a new factory that will produces processors of the supplied spec
	 * when it's invoked
	 */
	public ProcessorFactory getFactory(Element arg0) {
		String method = arg0.getTextTrim();
		//@todo add array bounds checking.
		String[] names = method.split("\\.");
		ModuleDescriptor[] modules;
		try {
			modules = SingletonACR.listModules();
		} catch (ACRException x) {
			// all goes tits up.
			return null;
		}
		ModuleDescriptor md = null;
		for (int i = 0; i < modules.length; i++) {
			if (modules[i].getName().equals(names[0])) {
				md = modules[i];
				break;
			}
		}
		ComponentDescriptor cd= md.getComponent(names[1]);
		MethodDescriptor methD = cd.getMethod(names[2]);
		return new ARProcessorFactory(md,cd,methD);
	}
	/**
	 * Create a new processor from the given chunk of XML
	 */
	public Processor loadProcessorFromXML(Element processorNode
				, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
		Element soaplab = processorNode
		.getChild("astroruntime", XScufl.XScuflNS);
String methodName = soaplab.getTextTrim();
//@todo add array bounds checking.
String[] names = methodName.split("\\.");
ModuleDescriptor[] modules;
try {
	modules = SingletonACR.listModules();
} catch (ACRException x) {
	throw new ProcessorCreationException(x);
}
ModuleDescriptor md = null;
for (int i = 0; i < modules.length; i++) {
	if (modules[i].getName().equals(names[0])) {
		md = modules[i];
		break;
	}
}
ComponentDescriptor cd= md.getComponent(names[1]);
MethodDescriptor methD = cd.getMethod(names[2]);
ARProcessor theProcessor = new ARProcessor(model, name,md,cd,methD);

return theProcessor;
	}


}
