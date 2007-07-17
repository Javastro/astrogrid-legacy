/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

//import java.io.IOException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.adql.AdqlCompiler;
import org.astrogrid.adql.AdqlException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Implementation of the adql interface using jeff's Adql compiler.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:48:28 PM
 */
public class AdqlImpl implements AdqlInternal {

    public Document s2x(String arg0) throws InvalidArgumentException {
        AdqlCompiler adqlCompiler = new AdqlCompiler( new StringReader(arg0) ) ;
        try {
        	
        	// doesnt seem to give correcnt namespaces..
        	// no namespace on top level element.
           // return (Document)adqlCompiler.compileToXmlDom() ;
        	
        	// parse it from string - hopefully will then contain correct namespaces.
        	return DomHelper.newDocument(s2xs(arg0));
        } catch (ParserConfigurationException x) {
            throw new InvalidArgumentException(x);			
		} catch (SAXException x) {
            throw new InvalidArgumentException(x);
		} catch (IOException x) {
            throw new InvalidArgumentException(x);
		}
    }
    
    public String s2xs(String arg0) throws InvalidArgumentException{
        AdqlCompiler adqlCompiler = new AdqlCompiler( new StringReader(arg0) ) ;
        try {
        	
            return adqlCompiler.compileToXmlText(true);
        } catch ( AdqlException x) {
            throw new InvalidArgumentException(x);   
        }
    }
    
//@todo - implement and add to public interface.
	public String x2s(Document arg0) {
		return null;
	}

}
