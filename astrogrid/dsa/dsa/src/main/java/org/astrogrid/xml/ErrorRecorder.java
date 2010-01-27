/**
 * $Id: ErrorRecorder.java,v 1.1 2010/01/27 17:17:04 gtr Exp $
 *
 */

package org.astrogrid.xml;

import java.util.Enumeration;
import java.util.Vector;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** An Error Handler that records (in a list) the non fatal errors and warnings  */

public class ErrorRecorder implements ErrorHandler {
   
   Vector errors = new Vector();
   Vector warnings = new Vector();

   /** Returns true if there are any errors */
   public boolean hasErrors() {
      return errors.size()>0;
   }
   
   /** Returns true if there are any warnings */
   public boolean hasWarnings() {
      return warnings.size()>0;
   }

   public SAXParseException[] getErrors() {
      return (SAXParseException[]) errors.toArray(new SAXParseException[] {} );
   }
   
   public SAXParseException[] getWarnings() {
      return (SAXParseException[]) warnings.toArray(new SAXParseException[] {} );
   }

   public String listErrors() {
      StringBuffer list = new StringBuffer();
      for (Enumeration e = errors.elements(); e.hasMoreElements();) {
         list.append( ((SAXParseException) e.nextElement()).getMessage()+" \n");
      }
      return list.toString();
   }
   
   /**
    * Receive notification of a non-recoverable error.
    *
    */
   public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
   }
   
   /**
    * Receive notification of a recoverable error, ie a validation error.
    * @see org.xml.sax.SAXParseException
    */
   public void error(SAXParseException exception) throws SAXException {
      System.out.println(exception.getMessage());
      errors.add(exception);
   }
   
   /**
    * Receive notification of a warning.
    */
   public void warning(SAXParseException exception) throws SAXException {
      System.out.println(exception.getMessage());
      warnings.add(exception);
   }
   
}

