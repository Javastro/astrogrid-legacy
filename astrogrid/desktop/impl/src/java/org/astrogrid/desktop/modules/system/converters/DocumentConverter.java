package org.astrogrid.desktop.modules.system.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Parse a string into a XML DOM Document
 * @author Noel Winstanley
 * @since Apr 14, 20062:34:21 AM
 */
public  class DocumentConverter implements Converter {
	public Object convert(final Class arg0, final Object arg1) {
	   if (arg0 != Document.class) { 
	       throw new RuntimeException("Can only convert to documents " + arg0.getName());
	   }
	   try {
	    return DomHelper.newDocument(arg1.toString());
	} catch (final Exception e) {
	    final RuntimeException ex =  new ConversionException("Failed to parse " + arg1 + " as XML document");
	    ex.initCause(e); // is this correct?
	    throw ex;
	}
	       
	}
}