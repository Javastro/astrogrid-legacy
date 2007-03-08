/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.Adql;
import org.astrogrid.adql.AdqlStoX;
import org.astrogrid.adql.ParseException;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/** Implementation of the adql interface using jeff's Adql compiler.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20072:48:28 PM
 */
public class AdqlImpl implements Adql {

	public Document s2x(String arg0) throws InvalidArgumentException {
	 AdqlStoX stoX = new AdqlStoX(new StringReader(arg0));
	 try {
		String s = stoX.compileToXmlText();
		return DomHelper.newDocument(s);
	} catch (ParseException x) {
		throw new InvalidArgumentException(x);
	} catch (ParserConfigurationException x) {
		throw new InvalidArgumentException(x);
	} catch (SAXException x) {
		throw new InvalidArgumentException(x);
	} catch (IOException x) {
		throw new InvalidArgumentException(x);
	}
	 
	}
//@todo - implement and add to public interface.
	public String x2s(Document arg0) {
		return null;
	}

}
