/*$Id: URIConvertor.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
 * Created on 24-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.nodestore.file;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;

/**custom convertor for xstream
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
class URIConvertor extends AbstractBasicConverter implements Converter {

    /** Construct a new URIConvertor
     * 
     */
    public URIConvertor() {
        super();
    }

    /**
     * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
     */
    public boolean canConvert(Class arg0) {
        return URI.class.equals(arg0);
    }

    /**
     * @throws MalformedURIException
     * @see com.thoughtworks.xstream.converters.basic.AbstractBasicConverter#fromString(java.lang.String)
     */
    protected Object fromString(String arg0) {        
        try {
            return new URI(arg0);
        } catch (MalformedURIException e) {
            throw new ConversionException(e);
        }
        
    }

}


/* 
$Log: URIConvertor.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:37  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/