package org.astrogrid.datacenter.cocoon.acting.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class ValidationHandler extends DefaultHandler {
    private Category logger = Category.getInstance(getClass());
    
    private List errors = new ArrayList();
    private List warnings = new ArrayList();
    private List fatalErrors = new ArrayList();
    
    /**
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException e) throws SAXException {
      super.error(e);
      
      logger.debug("[error] " + e.getMessage());
      
      errors.add(e);
    }

    /**
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException e) throws SAXException {
      super.fatalError(e);
      
      logger.debug("[fatalError] " + e.getMessage());
      
      fatalErrors.add(e);
    }

    /**
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException e) throws SAXException {
      super.warning(e);

      logger.debug("[warning] " + e.getMessage());
      
      warnings.add(e);
    }
    
    public void exception(Exception e) {
      logger.debug("[exception] " + e.getMessage());
      
      errors.add(e);
    }
    
    public boolean anyErrors() {
      logger.debug("[anyErrors] errors: " + errors.size());

      return errors.size() != 0;
    }
    
    public Iterator getErrorIterator() {
      return errors.iterator();
    }

    public List getErrorMessages() {
      return convertToStrings(errors);
    }

    public boolean anyWarnings() {
      logger.debug("[anyWarnings] warnings: " + warnings.size());

      return warnings.size() != 0;
    }

    public Iterator getWarningIterator() {
      return warnings.iterator();
    }

    public List getWarningMessages() {
      return convertToStrings(warnings);
    }

    public boolean anyFatalErrors() {
      logger.debug("[anyFatalErrors] fatalErrors: " + fatalErrors.size());

      return fatalErrors.size() != 0;
    }

    public Iterator getFatalErrorsIterator() {
      return fatalErrors.iterator();
    }

    public List getFatalErrorsMessages() {
      return convertToStrings(fatalErrors);
    }

    public boolean valid() {
      return
          (!anyErrors() &&
           !anyWarnings() &&
           !anyFatalErrors());
    }

    private List convertToStrings(List exceptions) {
      List result = new ArrayList(exceptions.size());
    
      Exception e = null;
      Iterator exIt = exceptions.iterator();
      while(exIt.hasNext()) {
        e = (Exception) exIt.next();
        result.add(e.getLocalizedMessage());
      }
    
      return result;
    }
  }