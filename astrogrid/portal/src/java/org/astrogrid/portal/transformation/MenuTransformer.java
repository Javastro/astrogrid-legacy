package org.astrogrid.portal.transformation;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Add the non-specified menus to the main menu.
 */
public class MenuTransformer extends AbstractDOMTransformer {

  /* (non-Javadoc)
   * @see org.apache.cocoon.transformation.AbstractDOMTransformer#transform(org.w3c.dom.Document)
   */
  protected Document transform(Document doc) {
    Document result = doc;

    Element menuEl =
      result.createElementNS("http://www.astrogrid.org/portal", "menu");
    menuEl.setAttribute("id", "miscellaneous");
    menuEl.setAttribute("name", "MenuTransformer");

    Element menuLinkEl =
      result.createElementNS("http://www.astrogrid.org/portal", "link");
    Node linkText = result.createTextNode("http://www.astrogrid.org");
    menuLinkEl.appendChild(linkText);
    menuEl.appendChild(menuLinkEl);

    Element rootMenuEl = (Element) result.getFirstChild();
    rootMenuEl.appendChild(menuEl);

    result = addMenus(result, menuEl);

    return result;
  }

  private Document addMenus(Document doc, Element menuEl) {
    Document result = doc;

    File menuFile = null;
    Element xIncludeEl = null;
    File[] menuFiles =
      listMenuFiles("/home/gps/projects/astrogrid/workspace/astrogrid-portal/build/webapp/WEB-INF/menu");
    for (int fileIndex = 0; fileIndex < menuFiles.length; fileIndex++) {
      menuFile = menuFiles[fileIndex];

      xIncludeEl =
        result.createElementNS("http://www.w3.org/2001/XInclude", "xi:include");
      xIncludeEl.setAttribute("href", "WEB-INF/menu/" + menuFile.getName());

      menuEl.appendChild(xIncludeEl);
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
}
