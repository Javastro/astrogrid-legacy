/*$Id: Xml2XhtmlTransformer.java,v 1.2 2005/05/12 15:37:45 clq2 Exp $
 * Created on 11-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.collections.Transformer;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2005
 *
 */
public class Xml2XhtmlTransformer implements Transformer{
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(Xml2XhtmlTransformer.class);

    /** Construct a new Xml2XhtmlTransformer
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * 
     */
    protected Xml2XhtmlTransformer(Source styleSource) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        super();
        xslt = TransformerFactory.newInstance().newTransformer(styleSource);
    }
    /**
     * @return
     */
    public static Source getStyleSource() {
        return new StreamSource(Xml2XhtmlTransformer.class.getResourceAsStream("xmlverbatim.xsl"));
    }
    private final javax.xml.transform.Transformer xslt;

    /**
     * @return
     */
    public static Transformer getInstance() {
        if (theInstance == null) {
            try {
                Source styleSoutce = getStyleSource();
                theInstance = new Xml2XhtmlTransformer(styleSoutce);
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
        Source source = new StreamSource(new StringReader(arg0.toString()));
        StringWriter sw = new StringWriter();
        Result sink = new StreamResult(sw);
        try {
            xslt.transform(source, sink);
            return sw.toString();
        } catch (TransformerException e) {
            // @todo Auto-generated catch block
            logger.error("TransformerException",e);
            return arg0.toString(); // better than nothing.
        }

    }

}


/* 
$Log: Xml2XhtmlTransformer.java,v $
Revision 1.2  2005/05/12 15:37:45  clq2
nww 1111

Revision 1.1.2.2  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.1.2.1  2005/05/11 14:25:23  nw
javadoc, improved result transformers for xml
 
*/