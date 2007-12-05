/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

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
		return getElement(arpf.getName(),arpf.getCommonName());
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
		return getElement(arp.getName(),arp.getCommonName());
	}
	
	    
    private Element getElement(String procName, String commonName) {
		logger.warn("ARXmlHandler.getElement procname = " + procName + " commonName = " + commonName);

        Element spec = new Element("astroruntimevohttp", XScufl.XScuflNS);
       

        Element arNameElement = new Element("vohttpName",
                XScufl.XScuflNS);
        arNameElement.setText(commonName);
        spec.addContent(arNameElement);

        Element arProcNameElement = new Element("vohttpProcName",
                XScufl.XScuflNS);
        arProcNameElement.setText(procName);
        spec.addContent(arProcNameElement);        
        
        return spec;
    }
	
	
	/**
	 * Create a new factory that will produces processors of the supplied spec
	 * when it's invoked
	 */
	public ProcessorFactory getFactory(Element specElement) {
		logger.warn("start getFactory in ARXmlHandler");
		Element astroElem = specElement.getChild("astroruntimevohttp",
                XScufl.XScuflNS);
		Element arNameElement = astroElem.getChild("vohttpName",
	                XScufl.XScuflNS);
	    String name = arNameElement.getTextTrim();
	    
		Element arProcNameElement = astroElem.getChild("vohttpProcName",
                XScufl.XScuflNS);
		String procname = arProcNameElement.getTextTrim();
		return new ARProcessorFactory(procname,name);
	}
	/**
	 * Create a new processor from the given chunk of XML
	 */
	public Processor loadProcessorFromXML(Element specElement
				, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
		logger.warn("start loadProcessorFromXML in ARXmlHandler the name = " + name + " but look in element for the real vohttp name");

		//hmmm I wonder can I just take the name and split it
		//because I know it is unique with token '!!!!' seperating out the
		//appviorn||||interface||||serviceivorn oh well 
		//lets just get it from the processorNode should be more certain
		//check the logs later that will tell me the answer.
		
		Element astroElem = specElement.getChild("astroruntimevohttp",
                XScufl.XScuflNS);
		
		Element arNameElement = astroElem.getChild("vohttpName",
                XScufl.XScuflNS);
		Element arProcNameElement = astroElem.getChild("vohttpProcName",
                XScufl.XScuflNS);		
		
		String vohttpName = arNameElement.getTextTrim();
		String vohttpProcName = arProcNameElement.getTextTrim();
		logger.warn("the vohttpname in loadprocessorxml procName = " + vohttpProcName + " and httpName = " + vohttpName);
		ARProcessor theProcessor = new ARProcessor(model, name, vohttpName);
		logger.warn("end loadProcessorFromXML in ARXmlHandler");
		return theProcessor;
	}
}