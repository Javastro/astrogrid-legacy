package org.astrogrid.scripting;
import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

/**
 * Top level object of the astrogrid scripting model
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
   
   private static Astrogrid theInstance;


   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return "Astrogrid root object:\n"+ super.toString();
   }

}
