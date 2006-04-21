/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Contribution bean for a stylesheet for rendering xml results into html.
 * buids and caches an xslt transformer for the stylesheet on first access.
 * @author Noel Winstanley
 * @since Apr 18, 20064:42:12 PM
 */
public class StylesheetsContribution {
	
	// safe to share a copy between all stylesheets contribs, as only hivemind thread assembles these contribs
	private static TransformerFactory fac = TransformerFactory.newInstance();
	

	/**
	 * @param name the name to set
	 * @throws TransformerConfigurationException 
	 */
	public void setSheet(String name) throws TransformerConfigurationException {
		this.templates = fac.newTemplates(new StreamSource(Xml2XhtmlTransformer.class.getResourceAsStream(name)));
	}
	/** pattern that determines wherther this stylesheet is applicable.
	 * @return the regexp
	 */

	/**
	 * @param regexp the regexp to set
	 */
	public void setRegexp(String regexp) {
		this.pattern = Pattern.compile(regexp, Pattern.DOTALL);
	}
	
	//@todo find a more efficient way of doing this .
	public boolean isApplicable(Document d) {
		return isApplicable(DomHelper.DocumentToString(d));
		
	}
	
	public boolean isApplicable(CharSequence input) {
		Matcher m = pattern.matcher(input);
		return m.lookingAt(); // tests whether a prefix of the pattern matches.
	}
	
	
	private Templates templates;
	private Pattern pattern;
	/** creates a new transformer from the stylesheet */
	public Transformer createTransformer() throws TransformerConfigurationException {
		Transformer t =  templates.newTransformer();
		//t.setOutputProperty(OutputKeys.METHOD,"html");
		return t;
	}
	
	public String toString() {
	if (this.pattern != null) {
		return this.pattern.pattern();
	} else {
		return "uninitialized";
	}
	}
	
	
	
	
}
