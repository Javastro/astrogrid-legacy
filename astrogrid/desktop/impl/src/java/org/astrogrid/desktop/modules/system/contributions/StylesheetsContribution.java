/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;

/** Contribution a stylesheet for rendering xml results into html.
 * buids and caches an xslt transformer for the stylesheet on first access.
 * @author Noel Winstanley
 * @since Apr 18, 20064:42:12 PM
 */
public class StylesheetsContribution {
	
	// safe to share a copy between all stylesheets contribs, as only hivemind thread assembles these contribs
	private static TransformerFactory fac = TransformerFactory.newInstance();
	

	/**
	 * @param name the name of the sheet - resource path, releative to {@link Xml2XhtmlTransformer}
	 * @throws TransformerConfigurationException 
	 */
	public void setSheet(final String name) throws TransformerConfigurationException {
		final InputStream stream = Xml2XhtmlTransformer.class.getResourceAsStream(name);
		if (stream == null) {
			throw new TransformerConfigurationException("Failed to read resource: " + name);
		}
		this.templates = fac.newTemplates(new StreamSource(stream));
	}


	/** Configures when this contribution is applicable.
	 * @param regexp the regexp to set
	 */
	public void setRegexp(final String regexp) {
		this.pattern = Pattern.compile(regexp, Pattern.DOTALL);
	}
	
	/** @see #isApplicable(CharSequence) */
	public boolean isApplicable(final Document d) {
		try {
			return isApplicable(DomHelper.DocumentToString(d));
		} catch (final IOException x) {
			return false;
		}
		
	}
	/** test if this sheet is applicable to an input by matching the regexp against it */
	public boolean isApplicable(final CharSequence input) {
		if (pattern == null) {
			return false;
		}
		final Matcher m = pattern.matcher(input);
		return m.lookingAt(); // tests whether a prefix of the pattern matches.
	}
	
	
	private Templates templates;
	private Pattern pattern;
	/** creates a new transformer from the stylesheet */
	public Transformer createTransformer() throws TransformerConfigurationException {
		if (templates == null) {
			throw new TransformerConfigurationException("No template supplied");
		}
		final Transformer t =  templates.newTransformer();
		//t.setOutputProperty(OutputKeys.METHOD,"html");
		return t;
	}
	
	@Override
    public String toString() {
	if (this.pattern != null) {
		return this.pattern.pattern();
	} else {
		return "uninitialized";
	}
	}
	
	
	
	
}
