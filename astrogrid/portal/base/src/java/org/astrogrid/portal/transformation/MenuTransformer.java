package org.astrogrid.portal.transformation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.apache.excalibur.source.Source;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Add the non-specified menus to the main menu.
 */
public class MenuTransformer extends AbstractDOMTransformer {
  private String menuDirectory = null;
  private String menuHref = null;

  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    Logger logger = this.getLogger();
    logger.info("[setup]");

    menuDirectory = resolveMenuDirectory(resolver, params);
    
    logger.info("[setup] menu href: " + menuHref);
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.transformation.AbstractDOMTransformer#transform(org.w3c.dom.Document)
   */
  protected Document transform(Document doc) {
    Document result = doc;
    
    Logger logger = this.getLogger();

/*
    Element menuEl =
      result.createElementNS("http://www.astrogrid.org/portal", "menu");
    menuEl.setAttribute("display", "Miscellaneous");
    menuEl.setAttribute("name", "MenuTransformer");
*/
    logger.info("[transform] create menu element");

//    Element menuLinkEl =
//      result.createElementNS("http://www.astrogrid.org/portal", "link");
//    Node linkText = result.createTextNode("http://www.astrogrid.org");
//    menuLinkEl.appendChild(linkText);
//    menuEl.appendChild(menuLinkEl);

    logger.info("[transform] created menu link element");

    Element rootMenuEl = (Element) result.getFirstChild();
//    rootMenuEl.appendChild(menuEl);

    logger.info("[transform] added new menu to main menu");

    result = addMenus(result, rootMenuEl, doc);
    
    logger.info("[transform] added sub menus");

    return result;
  }

  private Document addMenus(Document doc, Element menuEl, Document sourceDoc) {
    Document result = doc;

    Logger logger = this.getLogger();

    File menuFile = null;
    Element xIncludeEl = null;
    File[] menuFiles = null;
    
    try {
      menuFiles = listMenuFiles(menuDirectory);
    }
    catch(Throwable t) {
      // Do nothing.
    }
    
    logger.info("[addMenus] menu files: " + menuFiles);
    
    if(menuFiles != null) {
      for (int fileIndex = 0; fileIndex < menuFiles.length; fileIndex++) {
        menuFile = menuFiles[fileIndex];
  
        xIncludeEl =
          result.createElementNS("http://www.w3.org/2001/XInclude", "xi:include");
        xIncludeEl.setAttribute("href", menuDirectory + menuFile.getName());
  
        if(testMenuInclude(sourceDoc.getDocumentElement(), menuFile)) {
          menuEl.appendChild(xIncludeEl);
        }
      }
    }
    else {
      logger.debug("[addMenus] no files... directory: " + menuDirectory);
    }

    return result;
  }

  private File[] listMenuFiles(String menuDirectory) throws Throwable {
    final Logger logger = this.getLogger();

    try {
      File menuDirectoryFile = new File(menuDirectory);

     return menuDirectoryFile.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          logger.info("[FilenameFilter.accept] dir: " + dir + ", name: " + name);
          if (name.equals("menu.xml")) {
            return false;
          }
  
          return Pattern.matches(".*\\.xml", name);
        }
      });
    }
    catch(Throwable t) {
      logger.debug("[listMenuFiles]", t);
      throw t;
    }
  }

  private boolean testMenuInclude(Element menu, File toInclude) {
    boolean result = false;
    
    try {
      URL includeUrl = new URL("file://" + toInclude.getAbsolutePath());
      
      JXPathContext menuContext = JXPathContext.newContext(menu);
      JXPathContext fileContext = JXPathContext.newContext(new DocumentContainer(includeUrl));
      
      String fileMenuName = (String) fileContext.getValue("/agp:menu-def/@name");
      Double menuValue = (Double) menuContext.getValue("count(//agp:menu-def[@name='" + fileMenuName + "'])");
      
      result = (menuValue.doubleValue() == 0.0d);
    }
    catch(MalformedURLException e) {
      result = false;
    }
    
    return result;
  }
  
  private String resolveMenuDirectory(SourceResolver resolver, Parameters params)
    throws MalformedURLException, IOException {
    String menuDirectoryParam = params.getParameter("menu-directory", "");
    Source menuDirectorySource = resolver.resolveURI(menuDirectoryParam);
    String menuDirectoryURI = menuDirectorySource.getURI(); 
    URI uri = URI.create(menuDirectoryURI);
    
    return uri.getPath();
  }
}
