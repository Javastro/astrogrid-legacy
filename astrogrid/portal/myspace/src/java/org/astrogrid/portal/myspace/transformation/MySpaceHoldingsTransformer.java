package org.astrogrid.portal.myspace.transformation;

import org.apache.avalon.framework.parameters.Parameters;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractDOMTransformer;
import org.apache.cocoon.util.jxpath.DOMFactory;
import org.apache.cocoon.xml.XMLUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MySpaceHoldingsTransformer
    extends AbstractDOMTransformer {
      
  public static void main(String[] args) {
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document sourceDoc = builder.parse(new File(args[0]));
      
      MySpaceHoldingsTransformer transformer = new MySpaceHoldingsTransformer();
      Document transDoc = transformer.transform(sourceDoc);
      
      String result = XMLUtils.serializeNodeToXML(transDoc);
      System.out.print(result);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  /* (non-Javadoc)
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
      throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
  }

  /* (non-Javadoc)
   * @see org.apache.cocoon.transformation.AbstractDOMTransformer#transform(org.w3c.dom.Document)
   */
  protected Document transform(Document doc) {
    Document result = doc;
    
    JXPathContext context = JXPathContext.newContext(doc);
    Iterator recordPointerIt = context.iteratePointers("//dataItemRecord");
    
    try {
      // Create a new document with the myspace-tree root.
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document transDoc = builder.newDocument();
      
      Element rootElement = transDoc.createElement("myspace-tree");
      transDoc.appendChild(rootElement);
      
      // Create a new JXPath context that can create new DOM elements.
      JXPathContext transContext = JXPathContext.newContext(transDoc);
      transContext.setFactory(new DOMFactory());

      // Add all data item records as myspace-item nodes.
      boolean success = false;
      Pointer recordPointer = null;
      MySpaceRecord record = null;
      Element recordElement = null;
      while(recordPointerIt.hasNext()) {
        recordPointer = (Pointer) recordPointerIt.next();
        record = new MySpaceRecord(context, recordPointer);
        record.addElement("myspace-tree", transDoc, transContext);
        
        success = true;
      }

      if(success) {
        result = transDoc;
      }
    }
    catch(Exception e) {
      // Do nothing.
      e.printStackTrace();
    }
    
    return result;
  }
  
  private static class MySpaceRecord {
    private String fullName;
    private String safeName;
    private String parentName;
    private String itemName;
      
    private String id;
    private String ownerID;
    private String creationDate;
    private String expiryDate;
    private String size;
    private String type;
    private String permissionsMask;
    private String dataHolderURI;
    
    public MySpaceRecord(JXPathContext context, Pointer recordPointer) {
      String path = recordPointer.asPath();
      
      fullName = (String) context.getValue(path + "/dataItemName");
      id = (String) context.getValue(path + "/dataItemID");
      ownerID = (String) context.getValue(path + "/ownerID");
      creationDate = (String) context.getValue(path + "/creationDate");
      expiryDate = (String) context.getValue(path + "/expiryDate");
      size = (String) context.getValue(path + "/size");
      type = (String) context.getValue(path + "/type");
      permissionsMask = (String) context.getValue(path + "/permissionsMask");
      dataHolderURI = (String) context.getValue(path + "/dataHolderURI");
      
      separatePaths();
    }
    
    public String toString() {
      StringBuffer result = new StringBuffer();
      
      result.append('[');
      
      result.append("[safeName, ").append(safeName).append("]");
      result.append("[fullName, ").append(fullName).append("]");
      result.append("[parentName, ").append(parentName).append("]");
      result.append("[itemName, ").append(itemName).append("]");
      result.append("[id, ").append(id).append("]");
      result.append("[ownerID, ").append(ownerID).append("]");
      result.append("[creationDate, ").append(creationDate).append("]");
      result.append("[expiryDate, ").append(expiryDate).append("]");
      result.append("[size, ").append(size).append("]");
      result.append("[type, ").append(type).append("]");
      result.append("[permissionsMask, ").append(permissionsMask).append("]");
      result.append("[dataHolderURI, ").append(dataHolderURI).append("]");
      
      result.append(']');
      
      return result.toString();
    }
    
    public void addElement(String root, Document doc, JXPathContext context) {
      // Search for the parent node.
      Pointer parentPointer = context.getPointer("/descendant::myspace-item[@full-name = '" + parentName + "']");
      
      // If there is no parent node, set the root node as the parent.
      JXPathContext insertionContext = null;
      if(parentPointer == null || "null()".equals(parentPointer.asPath())) {
        parentPointer = context.getPointer("/myspace-tree");
      }
      
      // Create a new node for this record in the document.
      Element itemElement = doc.createElement("myspace-item");
      
      itemElement.setAttribute("full-name", "ag-insert");
      Node parentNode = (Node) parentPointer.getNode();
      parentNode.appendChild(itemElement);
      
      // Get the insertion context for this new node.
      Pointer insertionPoint =
          context.getRelativeContext(parentPointer)
                 .getPointer("myspace-item[@full-name = 'ag-insert']");
      insertionContext = context.getRelativeContext(insertionPoint);

      // Now that we have the new node's insertion context, create the attributes under it.
      insertionContext.createPathAndSetValue("attribute::safe-name", safeName);
      insertionContext.createPathAndSetValue("attribute::full-name", fullName);
      insertionContext.createPathAndSetValue("attribute::parent-name", parentName);
      insertionContext.createPathAndSetValue("attribute::item-name", itemName);
      insertionContext.createPathAndSetValue("attribute::id", id);
      insertionContext.createPathAndSetValue("attribute::owner-id", ownerID);
      insertionContext.createPathAndSetValue("attribute::creation-date", creationDate);
      insertionContext.createPathAndSetValue("attribute::expiry-date", expiryDate);
      insertionContext.createPathAndSetValue("attribute::size", size);
      insertionContext.createPathAndSetValue("attribute::type", type);
      insertionContext.createPathAndSetValue("attribute::permission-mask", permissionsMask);
      insertionContext.createPathAndSetValue("attribute::data-holder-uri", dataHolderURI);
    }
    
    private void separatePaths() {
      if(fullName != null && fullName.length() > 0) {
        int index = fullName.lastIndexOf('/');
        
        if(index > -1) {
          parentName = fullName.substring(0, index);
          itemName = fullName.substring(index + 1);
        }
        
        safeName = escapeName();
      }
    }
    
    private String escapeName() {
      StringBuffer result = new StringBuffer();
      
      char character = 0;
      for(int cIndex = 0; cIndex < fullName.length(); cIndex++) {
        character = fullName.charAt(cIndex);
        switch(character) {
          case '/':
          case '@':
          case ' ':
            result.append('_');
            break;

          default:
            result.append(character);
            break;
        }
      }
      
      return result.toString();
    }
  }
}
