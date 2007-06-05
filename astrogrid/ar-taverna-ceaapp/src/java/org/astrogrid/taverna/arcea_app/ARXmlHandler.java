/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import org.apache.log4j.Logger;

import org.astrogrid.acr.ACRException;

import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.XScufl;
import org.embl.ebi.escience.scufl.parser.XScuflFormatException;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;
import org.embl.ebi.escience.scuflworkers.XMLHandler;
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
		logger.warn("cea_app elementForFactory");
		ARProcessorFactory arpf = (ARProcessorFactory)arg0;		
		return getElement(arpf.getName(), arpf.getIvorn(),arpf.getInterfaceName());
	}
	
	/**
	 * Return the spec element for a given ProcessorFactory. In reality each XML
	 * handler will be given only a particular subclass of the ProcessorFactory
	 * to deal with so you can reasonably cast it to your specific
	 * implementation straight off to get factory specific data out.
	 */
	public Element elementForProcessor(Processor arg0) {
		logger.warn("cea_app start elementForProcessor in ARXmlHandler");
		ARProcessor arp = (ARProcessor)arg0;
		return getElement(arp.getName(), arp.getIvorn(),arp.getInterfaceName());
	}
	
    private Element getElement(String name, String ivorn,String interfaceName) {
		logger.warn("cea_app ARXmlHandler.getElement name = " + name + " interfaceName = " + interfaceName);

        Element spec = new Element("astroruntimecea_app", XScufl.XScuflNS);
        Element arNameElement = new Element("ceaivorn",
                XScufl.XScuflNS);
        arNameElement.setText(ivorn);
        spec.addContent(arNameElement);
        
        Element arInterfaceElement = new Element("ceaInterface",
                XScufl.XScuflNS);
        arInterfaceElement.setText(interfaceName);
        spec.addContent(arInterfaceElement);
        
        Element arProcNameElem = new Element("name",
                XScufl.XScuflNS);
        arProcNameElem.setText(name);
        spec.addContent(arProcNameElem);        
        
        return spec;
    }	
	
	
    /*
     * 
    
    private Element getElement(URI ceaAppIvorn, String ceaInterface) {
    	logger.warn("cea_app getElement uris");
    	return getElement(ceaAppIvorn.toString(), ceaInterface);
    	
    }	
     */
	
	/**
	 * Create a new factory that will produces processors of the supplied spec
	 * when it's invoked
	 */
	public ProcessorFactory getFactory(Element specElement) {
		logger.warn("cea_app start getFactory in ARXmlHandler");
		Element astroElement = specElement.getChild("astroruntimecea_app",
                XScufl.XScuflNS);
		
		Element ceaAppIvornElement = astroElement.getChild("ceaivorn",
	                XScufl.XScuflNS);
	    String ceaAppIvorn = ceaAppIvornElement.getTextTrim();
	    
		Element ceaInterfaceElement = astroElement.getChild("ceaInterface",
                XScufl.XScuflNS);
		String ceaInterface = ceaInterfaceElement.getTextTrim();
		
		Element nameElement = astroElement.getChild("name",
                XScufl.XScuflNS);
		String name = nameElement.getTextTrim();
		
		logger.warn("ivorn found = " + ceaAppIvorn + " interface = " + ceaInterface + " name = " + name);
		//@todo add array bounds checking.		
		return new ARProcessorFactory(name, ceaAppIvorn,ceaInterface);
	}
	/**
	 * Create a new processor from the given chunk of XML
	 */
	public Processor loadProcessorFromXML(Element specElement
				, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
		logger.warn("cea start loadProcessorFromXML in ARXmlHandler the name = " + name);

		Element astroElement = specElement.getChild("astroruntimecea_app",
                XScufl.XScuflNS);
		
		Element ceaAppIvornElement = astroElement.getChild("ceaivorn",
	                XScufl.XScuflNS);
	    String ceaAppIvorn = ceaAppIvornElement.getTextTrim();
	    logger.warn("appivorn in loadprocessorfromxml = " + ceaAppIvorn);
	    
		Element ceaInterfaceElement = astroElement.getChild("ceaInterface",
                XScufl.XScuflNS);
		String ceaInterface = ceaInterfaceElement.getTextTrim();
		logger.warn("instantiating ARProcessor from loadProcessorFromXML appivorn = " + ceaAppIvorn + " interface = " + ceaInterface);
		ARProcessor theProcessor = new ARProcessor(model, name, ceaAppIvorn, ceaInterface);
		logger.info("end loadProcessorFromXML in ARXmlHandler");
		return theProcessor;
	}
}