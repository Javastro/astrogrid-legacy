/*
 * $Id: UWSObjectResponseHandler.java,v 1.1 2008/09/24 13:47:18 pah Exp $
 * 
 * Created on 23 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.uws.client;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import net.ivoa.uws.JobSummary;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.xml.sax.SAXException;

/**
 * {@link ResponseHandler} to deal with the marshalling of UWS objects. Deals with the slightly messy exceptions and the closing of the connection in one place.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 23 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class UWSObjectResponseHandler<T> implements ResponseHandler<T> {
    
    
    
    private final Class<T> clazz;
 
    public UWSObjectResponseHandler(Class<T> c) {
       clazz = c; // it is a bit of a hack to have to pass in the class to the handler, but it seems that it is the only way to be able to call the generic function from CEAJAXUtils
    }
    
    public T handleResponse(final HttpResponse response)
            throws ClientProtocolException, IOException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        if(entity != null){
            InputStream instream = null ;
            try {
                 instream = entity.getContent();
                 T retval = CEAJAXBUtils.unmarshall(instream, clazz);
                 return retval;
            } catch (MetadataException e) {
                throw new ClientProtocolException("cannot create IVOA object ="+clazz.getSimpleName(),e);
            } catch (IllegalStateException e) {
                throw new ClientProtocolException("cannot create IVOA object="+clazz.getSimpleName(),e);
            } catch (JAXBException e) {
                throw new ClientProtocolException("cannot create IVOA object="+clazz.getSimpleName(),e);
            } catch (SAXException e) {
                throw new ClientProtocolException("cannot create IVOA object="+clazz.getSimpleName(),e);
            }
            finally
            {
                if (instream != null) {
                    instream.close();
                }
            }
        } else {
            return null;
        }
      
    }

}


/*
 * $Log: UWSObjectResponseHandler.java,v $
 * Revision 1.1  2008/09/24 13:47:18  pah
 * added generic UWS client code
 *
 */
