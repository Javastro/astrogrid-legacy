/*$Id: IDTransformer.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 21-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import org.apache.commons.collections.Transformer;

/** the identity transformation.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2005
 *
 */
public class IDTransformer implements Transformer {

    /** Construct a new IDTransformer
     * 
     */
    public IDTransformer() {
        super();
    }

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        return arg0;
    }
    
    private static final Transformer theInstance = new IDTransformer();
    public static final Transformer getInstance() {
        return theInstance;
    }

}


/* 
$Log: IDTransformer.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/05/12 15:59:13  clq2
nww 1111 again

Revision 1.2.20.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.
 
*/