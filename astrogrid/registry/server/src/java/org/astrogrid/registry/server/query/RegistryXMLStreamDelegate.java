package org.astrogrid.registry.server.query;

import javax.xml.stream.*;
import javax.xml.stream.util.StreamReaderDelegate;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.codehaus.xfire.util.STAXUtils;
import java.io.StringReader;
import org.astrogrid.contracts.http.filters.ContractsFilter;
import org.xmldb.api.base.XMLDBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class RegistryXMLStreamDelegate extends StreamReaderDelegate implements XMLStreamReader {

  protected ResourceSet resSet = null;

  private XMLStreamReader resXMLStreamReader = null;
  //by default say we are on the wrapper part of the reader.
  private boolean currentReaderisWrapper = true;
  private XMLStreamReader wrapperStreamReader =  null;
  private boolean identOnly = false;
  
  private static final String ASTROGRID_SCHEMA_BASE = "http://software.astrogrid.org/schema/";
  protected static String schemaLocationBase;
  
  /**
   * Logging variable for writing information to the logs
   */
   private static final Log log = LogFactory.getLog(RegistryXMLStreamDelegate.class);
  
  
  /**
   * Static to be used on the initiatian of this class for the config
   */   
  static {
        if(schemaLocationBase == null) {              
            schemaLocationBase = ContractsFilter.getContextURL() != null ? ContractsFilter.getContextURL() + "/schema/" :
                                 ASTROGRID_SCHEMA_BASE;
        }
  }  
  
  public RegistryXMLStreamDelegate(ResourceSet resSet,String xmlWrapper) {
    super();
  	this.resSet = resSet;
  	wrapperStreamReader = STAXUtils.createXMLStreamReader(new StringReader(xmlWrapper));
  	setParent(wrapperStreamReader);
  }
  
  public RegistryXMLStreamDelegate(ResourceSet resSet,String xmlWrapper, boolean identOnly) {
	    super();
	  	this.resSet = resSet;
	  	this.identOnly = identOnly;
	  	wrapperStreamReader = STAXUtils.createXMLStreamReader(new StringReader(xmlWrapper));
	  	setParent(wrapperStreamReader);
	  }  
  
  public abstract String getResourceContent(Resource res, boolean identOnly) throws org.xmldb.api.base.XMLDBException;
  
  public int next()  throws XMLStreamException {
   	
	int current;
	try {
    if(resXMLStreamReader != null) {
    	return super.next();
    }
    
    //okay resourceset size is 0 and and not on the wrapper(header/footer) reader.
    //so lets set it back to the wrapper reader and return an end_element.  This
    //should now put it is back on the end element for the footer area.
  	if(resSet.getSize() == 0 && !currentReaderisWrapper) {
  		setParent(wrapperStreamReader);
  		currentReaderisWrapper = true;
  		//we can return END_ELEMENT since that is where we left off for the footer area.
  		return wrapperStreamReader.END_ELEMENT;
  	}
  	current = super.next();
  	//We are at an end element and nothing has been checked for
  	//resourceSet yet.  So check if there are any and begin processing the
  	//first resource after that hasNext()
  	//will take over with the iteration.
  	if(current == wrapperStreamReader.END_ELEMENT && resSet.getSize() > 0) {
	   //log.info("resset size = " + resSet.getSize() + " here is the string in next() and resources still left = " + getResourceContent(resSet.getResource((resSet.getSize() - 1)),identOnly));
  	   resXMLStreamReader = STAXUtils.createXMLStreamReader
  	                           (new StringReader(getResourceContent(resSet.getResource((resSet.getSize() - 1)),identOnly)));
  		
  	   //okay set the delegate to this new reader.
  	   setParent(resXMLStreamReader);
  	   
  	   //no longer on the wrapper elements.
  	   currentReaderisWrapper = false;
  	   //okay we should have our content in the stream reader
  	   //remove the resource from the collection.
  	   resSet.removeResource((resSet.getSize() - 1));
  	   //log.info("just removed some kind of resource size = " + resSet.getSize());
  	   return super.nextTag();
  	}
  	
	}catch(XMLDBException xdbe) {
		throw new XMLStreamException(xdbe.getMessage());
	}
  	
  	return current;
  }
  
  
  public boolean hasNext()  throws XMLStreamException {
  	boolean current = super.hasNext();
  	//check if we are processing xml in the ResourceSet.
  	try {
    if(resXMLStreamReader != null) {
    	//okay were at the end of the document go to the next resource if there are any.
    	if(!current) {
    		if(resSet.getSize() > 0) {
    	    resXMLStreamReader.close();
    	    //log.info("hasNext() resset size = " + resSet.getSize() + " here is the string in hasNext() and resources still left = " + getResourceContent(resSet.getResource((resSet.getSize() - 1)), identOnly));
    		resXMLStreamReader = STAXUtils.createXMLStreamReader
  	                           (new StringReader(getResourceContent(resSet.getResource((resSet.getSize()-1)), identOnly)));
    		
    		/*
    		 * resXMLStreamReader = STAXUtils.createXMLStreamReader
  	                           (new StringReader(getResourceContent(resSet.getResource(0), identOnly)));
    	    
    		 */
    	    
  	        //set a new parent reader.
  	        setParent(resXMLStreamReader);
  	   
	  	   //okay we should have our content in the stream reader
  		   //remove the resource from the collection.
  	   	   resSet.removeResource((resSet.getSize() - 1));
  	   	   //resSet.removeResource(0);
  	  	   //log.info("just removed some kind of resource size = " + resSet.getSize());  	   	   
  	   	   return true;
  	   	   }else {
  	   	     //okay we went through some kind of resourceset and now done.
  	   	     //lets set the reader to null.  Return true.  The 
  	   	     //next() method will set it back to the footer part of the
  	   	     //wrapperelements.
  	   		 //log.info("no more resources left time to set xmlstreamreader to null and return true in hasNext() because there is still the wrapper");
  	   	     resXMLStreamReader = null;
  	   	     return true;
  	   	   }
    	}else {
    	  //were not at the end of the document and in the Resource somewhere.
    	  return current;
    	}
    }//if
    
  	}catch(XMLDBException xdbe) {
  		throw new XMLStreamException(xdbe.getMessage());
  	}
    //okay either  no resourceset or finished with it.  Must
    //be in the wrapper elements area.
    return current;
  }

}