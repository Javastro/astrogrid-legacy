/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

//import java.io.IOException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.adql.AdqlCompiler;
import org.astrogrid.adql.AdqlException;
import org.astrogrid.desktop.modules.ag.XPathHelper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Implementation of the adql interface using jeff's Adql compiler.
 * @future find a way to re-use instances of the adql compiler - it's not-thread safe. maybe make this entire class pooled?
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:48:28 PM
 */
public class AdqlImpl implements AdqlInternal {

    public Document s2x(String arg0) throws InvalidArgumentException {
        try {
        	
        	// doesnt seem to give correcnt namespaces..
        	// no namespace on top level element.
            // maybe this is expected - will add it.
            AdqlCompiler adqlCompiler = new AdqlCompiler( new StringReader(arg0) ) ;            
           Document doc =  (Document)adqlCompiler.compileToXmlDom() ;
           // now need to add namespace to top level element.
           doc.getDocumentElement().setAttributeNS(XPathHelper.XMLNS_NS,"xmlns",XPathHelper.ADQL_NS);
        	return doc;
        } catch ( AdqlException x) {
            String[] messages = x.getMessages();
            throw new InvalidArgumentException( StringUtils.join(messages,"\n"));   
        }
    }
    
    public String s2xs(String arg0) throws InvalidArgumentException{
        AdqlCompiler adqlCompiler = new AdqlCompiler( new StringReader(arg0) ) ;
        try {
        	
            return adqlCompiler.compileToXmlText();
        } catch ( AdqlException x) {
            String[] messages = x.getMessages();
            throw new InvalidArgumentException( StringUtils.join(messages,"\n"));   
        }
    }
    
//@future - implement and add to public interface.
	public String x2s(Document arg0) {
		return null;
	}

}
