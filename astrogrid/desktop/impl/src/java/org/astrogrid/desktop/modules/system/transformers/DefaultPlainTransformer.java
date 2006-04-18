/*$Id: DefaultPlainTransformer.java,v 1.2 2006/04/18 23:25:46 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.Transformer;

/** Default implementation of transformer to plaintext.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class DefaultPlainTransformer implements Transformer {

    /** Construct a new DefaultPlainTransformer
     * 
     */
    public DefaultPlainTransformer(final Transformer trans) {
        super();
        this.trans = trans;
    }

    private final Transformer trans;
    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(Object arg0) {
        Object arg1 = trans.transform(arg0);
        StringBuffer result = new StringBuffer();
        format(arg1,result);
        return result.toString();
    }
    
    private void format(Object arg1,StringBuffer buff) {
        if (arg1 == null) {
            buff.append(arg1); // let buff work out how to represent nulls.
            return;
        }
        if (arg1 instanceof Vector) {
            List v = (Vector)arg1;
          //  buff.append("[\n");
            for (Iterator i = v.iterator(); i.hasNext(); ) {
                //buff.append("\t");
                format(i.next(),buff);
                buff.append("\n");
            }
            //buff.append("]\n");
            return;
        }
        if (arg1 instanceof Hashtable) {
            Map m = (Hashtable)arg1;
            //buff.append("{\n");
            for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry)i.next();
                //buff.append("\t");
                buff.append(e.getKey());
                buff.append("=");
                format(e.getValue(),buff);
                buff.append("\t");
            }
            //buff.append("}\n");
            return;
        }
        
        buff.append(arg1);
        return;
        
    }
 

}


/* 
$Log: DefaultPlainTransformer.java,v $
Revision 1.2  2006/04/18 23:25:46  nw
merged asr development.

Revision 1.1.68.2  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.1.68.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.1  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/