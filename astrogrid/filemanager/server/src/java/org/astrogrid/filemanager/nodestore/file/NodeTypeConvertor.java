/*$Id: NodeTypeConvertor.java,v 1.2 2005/03/11 13:37:05 clq2 Exp $
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

import org.astrogrid.filemanager.common.NodeTypes;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;

/**custom convertor for xstream.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2005
 *
 */
class NodeTypeConvertor extends AbstractBasicConverter implements Converter {

    /**
     * @see com.thoughtworks.xstream.converters.basic.AbstractBasicConverter#fromString(java.lang.String)
     */
    protected Object fromString(String arg0) {
        return NodeTypes.fromString(arg0);
    }

    /**
     * @see com.thoughtworks.xstream.converters.basic.AbstractBasicConverter#canConvert(java.lang.Class)
     */
    public boolean canConvert(Class arg0) {
        return NodeTypes.class.equals(arg0);
    }

   

}


/* 
$Log: NodeTypeConvertor.java,v $
Revision 1.2  2005/03/11 13:37:05  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.2  2005/03/01 15:07:27  nw
close to finished now.

Revision 1.1.2.1  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.1  2005/02/25 12:33:27  nw
finished transactional store
 
*/