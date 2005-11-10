/*$Id: Votable2XhtmlTransformer.java,v 1.1 2005/11/10 16:28:26 nw Exp $
 * Created on 10-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.collections.Transformer;
import org.w3c.dom.Document;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2005
 * uses stylesheet by  
Chenzhou Cui <ccz@bao.ac.cn>  (National Astronomical Observatory of China)
http://services.china-vo.org/votable2xhtml/
 */
public class Votable2XhtmlTransformer implements Transformer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Votable2XhtmlTransformer.class);

    /** Construct a new Votable2XhtmlTransformer
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * 
     */
    protected Votable2XhtmlTransformer(Source styleSource) throws TransformerConfigurationException, TransformerFactoryConfigurationError {        
        super();
        xslt = TransformerFactory.newInstance().newTransformer(styleSource);        
    }
    public static Source getStyleSource() {
        return new StreamSource(Xml2XhtmlTransformer.class.getResourceAsStream("VOTable2XHTML.xsl"));
    }
    private final javax.xml.transform.Transformer xslt;    

    public static Transformer getInstance() {
        if (theInstance == null) {
            try {
                Source styleSoutce = getStyleSource();
                theInstance = new Votable2XhtmlTransformer(styleSoutce);
            } catch (Exception e) {
                logger.error("Could not load stylesheet ",e);
                theInstance = IDTransformer.getInstance();
            }
        }
        return theInstance;
    }
    
    private static Transformer theInstance;

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        Source source = new DOMSource((Document)arg0);
        StringWriter sw = new StringWriter();
        Result sink = new StreamResult(sw);
        try {
            xslt.transform(source, sink);
            return sw.toString();
        } catch (TransformerException e) {
            logger.error("TransformerException",e);
            return XMLUtils.DocumentToString((Document)arg0);
        }

    }

    
}


/* 
$Log: Votable2XhtmlTransformer.java,v $
Revision 1.1  2005/11/10 16:28:26  nw
added result display to vo lookout.
 
*/