/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import org.astrogrid.acr.ServiceException;


/** Persistence / Serialization API - converts between objects and XML.
 * 
 * Gives a little bit of distance from the XStream implementation, 
 * on which this is based - and also catches the RuntimeExceptions
 * which XStream throws, lifting them into ServiceExceptions.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20073:50:05 PM
 */
public interface XmlPersist {
    void toXml(Object o, java.io.OutputStream os) throws ServiceException;
    void toXml(Object o, Writer w) throws ServiceException;
    String toXml(Object o) throws ServiceException;
    
    Object fromXml(InputStream is) throws ServiceException;
    Object fromXml(Reader r) throws ServiceException;
    Object fromXml(String s) throws ServiceException;
}
