/*
 * AstroGrid Portal: MenuGenerator
 */
package org.astrogrid.portal.generation;

import org.apache.avalon.framework.parameters.Parameters;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.Map;


/**
 * Dynamically generate the AstroGrid site menu.
 */
public class MenuGenerator
    extends AbstractGenerator {
  private final AttributesImpl emptyAttr = new AttributesImpl();

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, par);
  }

  /* (non-Javadoc)
   * @see org.apache.avalon.excalibur.pool.Recyclable#recycle()
   */
  public void recycle() {
    super.recycle();
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate()
      throws IOException, SAXException, ProcessingException {
    contentHandler.startDocument();

    contentHandler.startElement("", "menu", "menu", emptyAttr);
    
		contentHandler.startElement("", "submenu", "submenu", emptyAttr);

		contentHandler.startElement("", "item", "item", emptyAttr);
		contentHandler.endElement("", "item", "item");

		contentHandler.endElement("", "submenu", "submenu");
    
		contentHandler.startElement("", "item", "item", emptyAttr);
		contentHandler.endElement("", "item", "item");

    contentHandler.endElement("", "menu", "menu");

    contentHandler.endDocument();
  }
}
