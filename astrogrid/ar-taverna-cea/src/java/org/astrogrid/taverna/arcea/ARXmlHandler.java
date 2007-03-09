/**
 * 
 */
package org.astrogrid.taverna.arcea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	
	private static final Log logger = LogFactory.getLog(ARXmlHandler.class);
	/**
	 * Return the spec element, that is to say the processor specific portion of
	 * the processor element. For example, the soaplab implementation of this
	 * method returns the element rooted at the 'soaplabwsdl' element.
	 */
	public Element elementForFactory(ProcessorFactory arg0) {
		ARProcessorFactory arpf = (ARProcessorFactory)arg0;		
		return getElement(arpf.getCeaAppIvorn(),arpf.getCeaServiceIvorn(),arpf.getCeaInterface());
	}
	
	/**
	 * Return the spec element for a given ProcessorFactory. In reality each XML
	 * handler will be given only a particular subclass of the ProcessorFactory
	 * to deal with so you can reasonably cast it to your specific
	 * implementation straight off to get factory specific data out.
	 */
	public Element elementForProcessor(Processor arg0) {
		logger.info("start elementForProcessor in ARXmlHandler");
		ARProcessor arp = (ARProcessor)arg0;
		return getElement(arp.getCeaAppIvorn(),arp.getCeaServiceIvorn(),arp.getCeaInterface());
	}
	
	
    /*
     * 
     */
    private Element getElement(URI ceaAppIvorn, URI ceaServiceIvorn, String ceaInterface) {
    	return getElement(ceaAppIvorn.toString(), ceaServiceIvorn.toString(),ceaInterface);
    	
    }
    
    private Element getElement(String ceaAppIvorn, String ceaServiceIvorn, String ceaInterface) {
        Element spec = new Element("astroruntimecea", XScufl.XScuflNS);
        
        /*
        Element ceaMethodName = new Element("ceaMethodName,
                XScufl.XScuflNS);
        ceaMethodName.setText(ceaMethodName);
        spec.addContent(ceaMethodName);
        */

        Element ceaAppIvornElement = new Element("ceaAppIvorn",
                XScufl.XScuflNS);
        ceaAppIvornElement.setText(ceaAppIvorn);
        spec.addContent(ceaAppIvornElement);
        
        Element ceaServiceIvornElement = new Element("ceaServiceIvorn",
                XScufl.XScuflNS);
        ceaServiceIvornElement.setText(ceaServiceIvorn);
        spec.addContent(ceaServiceIvornElement); 
        
        Element ceaInterfaceElement = new Element("ceaInterface",
                XScufl.XScuflNS);
        ceaInterfaceElement.setText(ceaInterface);
        spec.addContent(ceaInterfaceElement);  

        return spec;
    }
	
	
	/**
	 * Create a new factory that will produces processors of the supplied spec
	 * when it's invoked
	 */
	public ProcessorFactory getFactory(Element specElement) {
		logger.info("start getFactory in ARXmlHandler");
		Element astroElement = specElement.getChild("astroruntimecea",
                XScufl.XScuflNS);
		
		Element ceaAppIvornElement = astroElement.getChild("ceaAppIvorn",
	                XScufl.XScuflNS);
	    String ceaAppIvorn = ceaAppIvornElement.getTextTrim();
	    
		Element ceaServiceElement = astroElement.getChild("ceaServiceIvorn",
                XScufl.XScuflNS);
		String ceaServiceIvorn = ceaServiceElement.getTextTrim();		
		Element ceaInterfaceElement = astroElement.getChild("ceaInterface",
                XScufl.XScuflNS);
		String ceaInterface = ceaInterfaceElement.getTextTrim();		
		//@todo add array bounds checking.
		try {
			//hmm I suspect i could just call getACR() I just want to make sure
			//acr is running (I think)
			//or should I actually below really check it is still there and did not become
			//inactive or deleted
			SingletonACR.listApps();
		} catch (ACRException x) {
			// all goes tits up.
			return null;
		}
		
		return new ARProcessorFactory(ceaAppIvorn,ceaServiceIvorn,ceaInterface);
	}
	/**
	 * Create a new processor from the given chunk of XML
	 */
	public Processor loadProcessorFromXML(Element specElement
				, ScuflModel model, String name) throws ProcessorCreationException, DuplicateProcessorNameException, XScuflFormatException {
		logger.info("start loadProcessorFromXML in ARXmlHandler the name = " + name);

		//hmmm I wonder can I just take the name and split it
		//because I know it is unique with token '!!!!' seperating out the
		//appviorn||||interface||||serviceivorn oh well 
		//lets just get it from the processorNode should be more certain
		//check the logs later that will tell me the answer.
		Element astroElement = specElement.getChild("astroruntimecea",
                XScufl.XScuflNS);
		Element ceaAppIvornElement = astroElement.getChild("ceaAppIvorn",
	                XScufl.XScuflNS);
	    String ceaAppIvorn = ceaAppIvornElement.getTextTrim();
	    
		Element ceaServiceElement = astroElement.getChild("ceaServiceIvorn",
	            XScufl.XScuflNS);
		String ceaServiceIvorn = ceaServiceElement.getTextTrim();
		
		Element ceaInterfaceElement = astroElement.getChild("ceaInterface",
	            XScufl.XScuflNS);
		String ceaInterface = ceaInterfaceElement.getTextTrim();		
			
		ARProcessor theProcessor = new ARProcessor(model, ceaAppIvorn, ceaServiceIvorn, ceaInterface);
		logger.info("end loadProcessorFromXML in ARXmlHandler");
		return theProcessor;
	}
}