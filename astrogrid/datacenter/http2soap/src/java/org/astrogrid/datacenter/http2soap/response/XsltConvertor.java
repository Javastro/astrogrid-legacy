/*$Id: XsltConvertor.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.datacenter.http2soap.ResponseConvertor;
import org.astrogrid.datacenter.http2soap.ResponseConvertorException;
import org.w3c.dom.Document;

/** convertor that applies an xslt transformation to the response from legacy service
 *  - hence response must be valid XML / XHTML
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class XsltConvertor implements ResponseConvertor {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.adapter.ResponseConvertor#convertResponse(java.io.InputStream)
     */
    public void convertResponse(ReadableByteChannel cin,WritableByteChannel cout) throws ResponseConvertorException {
        try {
            TransformerFactory fac = TransformerFactory.newInstance();
            Source stylesheet = getStylesheetSource();
            Transformer trans = fac.newTransformer(stylesheet);
            Source source = new StreamSource(Channels.newInputStream(cin));
            Result result = new StreamResult(Channels.newOutputStream(cout));
            trans.transform(source,result);
        } catch (TransformerConfigurationException e) {
            throw new ResponseConvertorException("Could not create transformer",e);
        } catch (TransformerException e) {
            throw new ResponseConvertorException("Error occurred during transformation",e);
        }
    }

    private Source getStylesheetSource() {
        if (xslt != null) {
            return new DOMSource(xslt);
        } else 
        return new StreamSource(this.getClass().getResourceAsStream(xsltResource));
    } 

    protected String xsltResource;
    public void setXsltResource(String xsltResource) {
        this.xsltResource = xsltResource;
    }
    protected Document xslt;
    
    public void setXslt(Document xslt) {
        this.xslt = xslt;
    }
    
    public Document getXslt() {
        return xslt;
    }

}


/* 
$Log: XsltConvertor.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/