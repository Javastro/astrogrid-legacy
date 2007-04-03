/**
 * 
 */
package org.astrogrid.taverna.armyspace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;

import java.util.Iterator;

import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.XScufl;
import org.embl.ebi.escience.scufl.parser.XScuflFormatException;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;
import org.embl.ebi.escience.scuflworkers.XMLHandler;

import org.apache.log4j.Logger;
//import org.embl.ebi.escience.scuflworkers.soaplab.SoaplabProcessor;
import org.jdom.Element;

import java.net.URI;

/**
 * @author Noel Winstanley
 * @since May 24, 20065:35:01 PM
 */
public class ARXmlHandler implements XMLHandler {
	
	private static Logger logger = Logger.getLogger(ARXmlHandler.class);
	/**
	 * Return the spec element, that is to say the processor specific portion of
	 * the processor element. For example, the soaplab implementation of this
	 * method returns the element rooted at the 'soaplabwsdl' element.
	 */
	public Element elementForFactory(ProcessorFactory arg0) {
		logger.warn("ARXmlHandler.elementForFactory");
		ARProcessorFactory arpf = (ARProcessorFactory)arg0;		
		return getElement(arpf.getName());
	}
	
	/**
	 * Return the spec element for a given ProcessorFactory. In reality each XML
	 * handler will be given only a particular subclass of the ProcessorFactory
	 * to deal with so you can reasonably cast it to your specific
	 * implementation straight off to get factory specific data out.
	 */
	public Element elementForProcessor(Processor arg0) {
		logger.warn("start elementForProcessor in ARXmlHandler");
		ARProcessor arp = (ARProcessor)arg0;
		return getElement(arp.getName());
	}
	
	    
    private Element getElement(String name) {
		logger.warn("ARXmlHandler.getElement name = " + name);

        Element spec = new Element("astroruntimemyspace", XScufl.XScuflNS);
       

        Element arNameElement = new Element("myspaceName",
                XScufl.XScuflNS);
        arNameElement.setText(name);
        spec.addContent(arNameElement);
        
        return spec;
    }
	
	
	/**
	 * Create a new factory that will produces processors of the supplied spec
	 * when it's invoked
	 */
	public ProcessorFactory getFactory(Element specElement) {
		logger.warn("start getFactory in ARXmlHandler");
		Element astroElem = specElement.getChild("astroruntimemyspace",
                XScufl.XScuflNS);
		Element arNameElement = astroElem.getChild("myspaceName",
	                XScufl.XScuflNS);
	    String name = arNameElement.getTextTrim();
		return new ARProcessorFactory(name);
	}
	/**
	 * Create a new processor from the given chunk of XML
	 */
	public Processor loadProcessorFromXML(Element specElement
				, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
		logger.warn("start loadProcessorFromXML in ARXmlHandler the name = " + name + " but look in element for the real myspace name");

		//hmmm I wonder can I just take the name and split it
		//because I know it is unique with token '!!!!' seperating out the
		//appviorn||||interface||||serviceivorn oh well 
		//lets just get it from the processorNode should be more certain
		//check the logs later that will tell me the answer.
		
		Element astroElem = specElement.getChild("astroruntimemyspace",
                XScufl.XScuflNS);
		Element arNameElement = astroElem.getChild("myspaceName",
                XScufl.XScuflNS);
		
		String myspaceName = arNameElement.getTextTrim();
		logger.warn("the myspaceName in loadprocessorxml = " + myspaceName);
		ARProcessor theProcessor = new ARProcessor(model, myspaceName);
		logger.warn("end loadProcessorFromXML in ARXmlHandler");
		return theProcessor;
	}
}