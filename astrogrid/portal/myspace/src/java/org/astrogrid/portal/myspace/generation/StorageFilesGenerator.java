package org.astrogrid.portal.myspace.generation;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapper;
import org.astrogrid.portal.myspace.acting.framework.ContextWrapperFactory;
import org.astrogrid.portal.utils.acting.ActionUtils;
import org.astrogrid.portal.utils.acting.ActionUtilsFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Generate <code>StoreClient</code> list of files as XML.
 * 
 * @author peter.shillan
 */
public class StorageFilesGenerator extends AbstractGenerator {
  private static final String FILTER = "*";
  private static final String MYSPACE_TREE = "myspace-tree";
  private static final String MYSPACE_ITEM = "myspace-item";
  private static final String MYSPACE_FILE_ATTR = "file";
  private static final String MYSPACE_FOLDER_ATTR = "folder";
  
  private ContextWrapper context;
  
  /**
   * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
   */
  public void setup(SourceResolver resolver, Map objectModel, String src, Parameters params)
  		throws ProcessingException, SAXException, IOException {
    super.setup(resolver, objectModel, src, params);
    
    // Get environment.
    ActionUtils utils = ActionUtilsFactory.getActionUtils();
    Request request = ObjectModelHelper.getRequest(objectModel);
    Session session = request.getSession(true);

    // Create environmental context.
    context = ContextWrapperFactory.getContextWrapper(
        ContextWrapper.PARAM_PROTOCOL, utils, params, request, session);
  }

  /**
   * Generate <code>StoreClient</code> list of files into as XML.
   * 
   * @see org.apache.cocoon.generation.Generator#generate()
   */
  public void generate() throws IOException, SAXException, ProcessingException {
    Document document = null;
    
    // Create document we are generating.
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      document = builder.newDocument();
    }
    catch(Exception e) {
      new ProcessingException("failed to create new document", e);
    }
    
    // Get the root storage file.
    StoreFile storeFile = context.getStoreClient().getFiles(StorageFilesGenerator.FILTER);
    
    // Create the root element.
    Element rootElement = document.createElement(StorageFilesGenerator.MYSPACE_TREE);
    document.appendChild(rootElement);
    
    // Walk the storage file tree.
    walkFile(storeFile, document, rootElement);
    
    // Stream the fully produced document.
    DOMStreamer streamer = new DOMStreamer();
    streamer.setNormalizeNamespaces(false);
    streamer.setContentHandler(contentHandler);
    streamer.stream(document);
  }
  
  /**
   * Walk the store file tree adding containers and leaves to the document as
   * necessary.
   * 
   * @param storeFile current store file
   * @param document document being generated
   * @param parentElement element to add new ones to
   */
  private void walkFile(StoreFile storeFile, Document document, Element parentElement) 
  		throws ProcessingException {
  	if(storeFile == null) {
  		return;
  	}
  	
  	// Ignore the root - this is the service point - and process the children.
  	else if(storeFile.getParent() == null) {
  	  // Get list of files.
  	  StoreFile[] storeFiles = storeFile.listFiles();

  	  // For each child, visit the file with the new element as the parent element.
  	  for(int fileIndex = 0; fileIndex < storeFiles.length; fileIndex++) {
  	    walkFile(storeFiles[fileIndex], document, parentElement);
  	  }
  	}
  	
	  // Add new leaf element to document under parent element.
  	else if(storeFile.isFile()) {
  	  Element fileElement = addStorageItem(storeFile, document, parentElement);
  	  fileElement.setAttribute("type", StorageFilesGenerator.MYSPACE_FILE_ATTR);
  	}
  	
	  // Add new container element to document under parent element.
  	else if(storeFile.isFolder()) {
  	  // Get list of files.
  	  StoreFile[] storeFiles = storeFile.listFiles();
  	  
  	  // Add new leaf node to document under parent element.
  	  Element folderElement = addStorageItem(storeFile, document, parentElement);
  	  folderElement.setAttribute("type", StorageFilesGenerator.MYSPACE_FOLDER_ATTR);
  	  
  	  // For each child, visit the file with the new element as the parent element.
  	  for(int fileIndex = 0; storeFiles != null && fileIndex < storeFiles.length; fileIndex++) {
  	    walkFile(storeFiles[fileIndex], document, folderElement);
  	  }
  	}
  	
  	// If we get to here, we are not dealing with a file or folder... just return.
  	else {
  	  return;
  	  // throw new ProcessingException("invalid store file [" + storeFile.getName() +"]: not a folder or file");
  	}
  }
  
  private Element addStorageItem(StoreFile storeFile, Document document, Element parentElement) 
  		throws ProcessingException {
    // Get file name and full path.
    String fileName = storeFile.getName();
    String filePath = storeFile.getPath();
    String safeName = getSafeName(filePath);
    
    // Get storage location.
    Agsl agsl = storeFile.toAgsl();
    
    // Create new element.
    Element itemElement = document.createElement(StorageFilesGenerator.MYSPACE_ITEM);
    itemElement.setAttribute("safe-name", safeName);
    itemElement.setAttribute("full-name", filePath);
    itemElement.setAttribute("item-name", fileName);
    itemElement.setAttribute("id", safeName);
    try {
      itemElement.setAttribute("url", agsl.resolveURL().toString());
      
      Ivorn ivorn = agsl.toIvorn(context.getUser());
      itemElement.setAttribute("ivorn", ivorn.toString());
    }
    catch(IOException e) {
      throw new ProcessingException("could not resolve AstroGrid URL for [" + fileName + "]", e);
    }

    // Add to parent.
    parentElement.appendChild(itemElement);
    
    return itemElement;
  }

  private String getSafeName(String fileName) {
    StringBuffer result = new StringBuffer();
    
    char character = 0;
    for(int cIndex = 0; cIndex < fileName.length(); cIndex++) {
      character = fileName.charAt(cIndex);
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
