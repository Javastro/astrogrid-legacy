package org.astrogrid.portal.transformation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Add the non-specified menus to the main menu.
 */
public class MenuTransformer extends AbstractDOMTransformer {
  private Category logger = Category.getInstance(getClass().getName());
  
  private String menuDirectory = null;

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    menuDirectory = params.getParameter("menu-directory", "");
    
    logger.debug("[setup] menu directory: " + menuDirectory);
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.transformation.AbstractDOMTransformer#transform(org.w3c.dom.Document)
   */
  protected Document transform(Document doc) {
    Document result = doc;

    Element menuEl =
      result.createElementNS("http://www.astrogrid.org/portal", "menu");
    menuEl.setAttribute("display", "Miscellaneous");
    menuEl.setAttribute("name", "MenuTransformer");

    Element menuLinkEl =
      result.createElementNS("http://www.astrogrid.org/portal", "link");
    Node linkText = result.createTextNode("http://www.astrogrid.org");
    menuLinkEl.appendChild(linkText);
    menuEl.appendChild(menuLinkEl);

    Element rootMenuEl = (Element) result.getFirstChild();
    rootMenuEl.appendChild(menuEl);

    result = addMenus(result, menuEl, doc);

    return result;
  }

  private Document addMenus(Document doc, Element menuEl, Document sourceDoc) {
    Document result = doc;

    File menuFile = null;
    Element xIncludeEl = null;
    File[] menuFiles = listMenuFiles(menuDirectory);
    for (int fileIndex = 0; fileIndex < menuFiles.length; fileIndex++) {
      menuFile = menuFiles[fileIndex];

      xIncludeEl =
        result.createElementNS("http://www.w3.org/2001/XInclude", "xi:include");
      xIncludeEl.setAttribute("href", "WEB-INF/menu/" + menuFile.getName());

      if(testMenuInclude(sourceDoc.getDocumentElement(), menuFile)) {
        menuEl.appendChild(xIncludeEl);
      }
    }

    return result;
  }

  private File[] listMenuFiles(String menuDirectory) {
    File menuDirectoryFile = new File(menuDirectory);

    return menuDirectoryFile.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        if (name.equals("menu.xml")) {
          return false;
        }

        return Pattern.matches(".*\\.xml", name);
      }
    });
  }

  private boolean testMenuInclude(Element menu, File toInclude) {
    boolean result = false;
    
    try {
      URL includeUrl = new URL("file://" + toInclude.getAbsolutePath());
      
      JXPathContext menuContext = JXPathContext.newContext(menu);
      JXPathContext fileContext = JXPathContext.newContext(new DocumentContainer(includeUrl));
      
      String fileMenuName = (String) fileContext.getValue("/menu/@name");
      Double menuValue = (Double) menuContext.getValue("count(//menu[@name='" + fileMenuName + "'])");
      
      result = (menuValue.doubleValue() == 0.0d);
    }
    catch(MalformedURLException e) {
      result = false;
    }
    
    return result;
  }
  
}
