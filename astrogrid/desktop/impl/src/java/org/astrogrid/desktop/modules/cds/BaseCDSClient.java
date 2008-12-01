/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Base class for all CDS clients - as they follow a common pattern.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20085:43:17 PM
 */
public class BaseCDSClient implements PropertyChangeListener {
    protected final HttpClient http;
    protected String base;
    protected final XMLInputFactory inputFactory;   
    protected final Preference endpoint;      
    /**
     * Commons Logger for this class
     */
    protected  final Log logger;
    public BaseCDSClient(final HttpClient http,final Preference endpoint)  {
        super();
        logger = LogFactory.getLog(this.getClass());
        this.http = http;
        this.endpoint = endpoint;
        inputFactory = XMLInputFactory.newInstance();         
        endpoint.addPropertyChangeListener(this);
        endpoint.initializeThroughListener(this);
    }
    

    /**
     * builds the basic http method, to which the query string should be added. 
     */
    protected HttpMethod buildHttpMethod() {
        final HttpMethod meth = new GetMethod(base);
        meth.setFollowRedirects(true);
        return meth;
    }
    
    /** executes a http method to return a string. calls 'parseResponse' to parse the response. */
    protected String executeHttpMethod(final HttpMethod meth) throws ServiceException {
        try {            
            final int status = http.executeMethod(meth);
            if (status != HttpStatus.SC_OK) {
                throw new ServiceException("Failed to query service: returned HTTP Error " + status);
            }
            return parseResponse(meth.getResponseBodyAsStream());
        } catch (final HttpException x) {
            throw new ServiceException("Failed to query service",x);
        } catch (final IOException x) {
            throw new ServiceException("Failed to query service",x);
        } catch (final XMLStreamException x) {
            throw new ServiceException("Failed to parse response from service",x);
        } finally {
            meth.releaseConnection();
        }        
    }
    
    
    /** parse response: this implementation just extracts the content of the 'return' element 
     * @throws XMLStreamException */
    protected String parseResponse(final InputStream str) throws XMLStreamException, ServiceException {
        try {
          final XMLStreamReader is = inputFactory.createXMLStreamReader(str);
          for (is.next(); ! (is.isEndElement() && is.getLocalName().equals("Sesame")); is.next()) {
              if (is.isStartElement()) { // otherwise, we don't care.
                  final String elementName = is.getLocalName();
                  if (elementName.equals("return")) {
                      final String s=  is.getElementText();
                      if (s == null) {
                          return "";
                      } else {
                          return s.trim();
                      }
                  }
              }
          }
          throw new ServiceException("No return from service");
        } finally {
          IOUtils.closeQuietly(str);          
        }
    }

    
    
    
    public void propertyChange(final PropertyChangeEvent evt) {        
        try {
            final URL u = new URL(endpoint.getValue());
            base = u.toString();
         
        } catch (final MalformedURLException x) {
            logger.error("Endpoint is malformed",x);
        }        
    }

}
