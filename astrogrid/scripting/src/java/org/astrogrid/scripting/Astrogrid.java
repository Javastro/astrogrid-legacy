package org.astrogrid.scripting;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.sql.SQLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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


   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "Astrogrid root object:\n"+ super.toString();
   }


   // helper methods related to datacenter - maybe these are better moved into a child object?
   /** convert an adql query to an Element
    * @see org.astrogrid.datacenter.adql.ADQLUtils */
   public Element toQueryBody(Select s) throws ADQLException {
      return ADQLUtils.toQueryBody(s);
   }
   /** convert an sql string query to an Element
    * @see org.astrogrid.datacenter.sql.SQLUtils */
   public Element toQueryBody(String s) throws IOException {
      return SQLUtils.toQueryBody(s);
   }



   // XML helper methods
   /** parse contents of input stream into document */
   public Document newDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
      return XMLUtils.newDocument(is);
   }
   /** parse contents of string into document */
   public Document newDocument(String s) throws ParserConfigurationException, SAXException, IOException {
      InputStream is = new ByteArrayInputStream(s.getBytes());
      return XMLUtils.newDocument(is);
   }
   /** create new DOM Document */
   public Document newDocument() throws ParserConfigurationException {
      return XMLUtils.newDocument();
   }
   /** convert document to string */
   public String documentToString(Document doc) {
      return XMLUtils.DocumentToString(doc);
   }
   /** convert element to string */
   public String elementToString(Element el) {
      return XMLUtils.ElementToString(el);
   }

}
