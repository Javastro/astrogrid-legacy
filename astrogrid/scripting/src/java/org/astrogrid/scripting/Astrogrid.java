package org.astrogrid.scripting;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;

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
 * <h3>XML Utilities</h3>
 * Provides helper methods to parse strings and inputStreams into {@link org.w3c.dom.Document}, and convert Documents and Elements back into
 * String.
 * <h3>Datacenter Query Utilities</h3>
 * Provides {@link #toQueryBody} methods to convert sql strings and ADQL queries into the correct format.
 * <h3>Ermmm</h3>
 * Need to add more stuff here - helpers for manipulating document formats sent to differnet services, etc.
 * 
 * @see Services
 * @see Service
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2004
 *
 */
public class Astrogrid extends Services {
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
            System.err.println("Could not initialize Astrogrid object");
            e.printStackTrace();
         }
      }
      return theInstance;
   }
   /** create the singleton astorgrid object, using service document at the given url */
   public synchronized static Astrogrid getInstance(URL url) {
      try {
        theInstance = new Astrogrid(url);
      } catch (Exception e) {
         System.err.println("Could not inizitalize Astrogrid object from url:" + url.toString());
         e.printStackTrace();
      }
      return theInstance;
   }
   /** create the singleton astorgrid object, using service document at the given url */
   public synchronized static Astrogrid getInstance(String url) {
      try {
        theInstance = new Astrogrid(url);
      } catch (Exception e) {
         System.err.println("Could not inizitalize Astrogrid object from url:" + url.toString());
         e.printStackTrace();
      }
      return theInstance;
   }
   
   private static Astrogrid theInstance;
    private final WorkflowManagerFactory factory = new WorkflowManagerFactory();
    private final ObjectHelper oHelper = new ObjectHelper();
    private final XMLHelper xHelper = new XMLHelper();
      
    /** access the workflow manager */
    public WorkflowManager getWorkflowManager() throws WorkflowInterfaceException {
        return factory.getManager();
    }
    
    public ObjectHelper getObjectHelper() {
        return oHelper;
    }
    
    public XMLHelper getXMLHelper() {
        return xHelper;
    }
    
    /** accces the system configuration object */
    public Config getSystemConfig() {
        return SimpleConfig.getSingleton();
    }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "Astrogrid root object:\n"+ super.toString();
   }


}
