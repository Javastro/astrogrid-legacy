/*$Id: ElementBuilder.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.builder;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.http2soap.ResultBuilder;
import org.astrogrid.datacenter.http2soap.ResultBuilderException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**Builder that returns result as a DOM element
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class ElementBuilder extends AbstractResultBuilder implements ResultBuilder {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.legacy.ResultBuilder#build(java.io.InputStream)
     */
    public Object build(ReadableByteChannel cin) throws ResultBuilderException, IOException {
        try {
        Document d = XMLUtils.newDocument(Channels.newInputStream(cin));
        return d.getDocumentElement();
        } catch (ParserConfigurationException e) {
            throw new ResultBuilderException("Could not initialize parser",e);
        } catch (SAXException e) {
            throw new ResultBuilderException("Could not parse result channel as xml",e);
        }
        
    }

}


/* 
$Log: ElementBuilder.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/