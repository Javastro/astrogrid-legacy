package org.astrogrid.scripting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Top level object of the astrogrid scripting model<p />
 * 
 * Create an instance of this class within your scripting language<p/>
 * 
 * <h2>Features</h2>
 * <h3>Service Discovery</h3>
 * This class extends {@link Services}, which maintains lists of the different kinds of astrogrid services, and create delegates as needed.
 * <h3>Configured Registry Delegate and Store Client</h3>
 * This class provides simple access to the default registy and store services.
 * <h3>Workflow Manager</h3>
 * This class provides helper methods to build workflow documents, submit them to jes servers, and view results
 * <h3>XML Utilities</h3>
 * Provides helper methods to parse strings and inputStreams into {@link org.w3c.dom.Document}, and convert Documents and Elements back into
 * String.
 * <h3>Datacenter Query Utilities</h3>
 * Provides methods to convert sql strings and ADQL queries into the correct format.
 * <h3>Object creation utilties</h3>
 * Provides helper methods to create some of the kinds of objects used in the astrogrid system.
 * 
 * @see Services
 * @see Service
 * @deprecated. Use Toolbox
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2004
 *
 */
public class Astrogrid extends Services {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Astrogrid.class);
    

   /** construct a new astrogrid object, using the default service document
    * <p>
    * Use {@link #getInstance()} instead if your scripting language supports this.
    * @throws IOException
    * @throws SAXException
    */
	public Astrogrid() throws IOException, SAXException {   
      super();
	}
   /** construct a new astrogrid object, using the service document at the specified url
    * <p>
    * Use {@link #getInstance(java.net.URL)} instead if your scripting language supports static methods
    * @param url location of service document
    * @throws IOException
    * @throws SAXException
    */
   public Astrogrid(URL url) throws IOException, SAXException {
      super(url);
   }
   public Astrogrid(String url) throws MalformedURLException, IOException, SAXException {
      super(url);
   }
   /** access the singleton astrogrid object, initializaing if necessary
    * */   
   public synchronized static Astrogrid getInstance()  {
      if (theInstance == null) {
         try {
            theInstance = new Astrogrid();
         } catch (Exception e) {
            logger.error("Could not initialize Astrogrid object",e);
         }
      }
      return theInstance;
   }
   /** create the singleton astorgrid object, using service document at the given url */
   public synchronized static Astrogrid getInstance(URL url) {
      try {
        theInstance = new Astrogrid(url);
      } catch (Exception e) {
        logger.error("Could not inizitalize Astrogrid object from url:" + url.toString(),e);
      }
      return theInstance;
   }
   /** create the singleton astorgrid object, using service document at the given url */
   public synchronized static Astrogrid getInstance(String url) {
      try {
        theInstance = new Astrogrid(url);
      } catch (Exception e) {
         logger.error("Could not inizitalize Astrogrid object from url:" + url.toString(),e);
      }
      return theInstance;
   }
   
   private static Astrogrid theInstance;
    /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "Astrogrid root object:\n"+ super.toString();
   }


}
