/*$Id: URIComparator.java,v 1.2 2005/11/11 10:08:18 nw Exp $
 * Created on 09-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.recorder;

import java.io.Serializable;
import java.util.Comparator;

import jdbm.helper.StringComparator;


public final class URIComparator implements Comparator, Serializable {
    static final long serialVersionUID = 12345689123456789L;
    private final Comparator comp =new StringComparator();

    public int compare(Object o1, Object o2) {
        return comp.compare(o1.toString(),o2.toString());
    }
}

/* 
$Log: URIComparator.java,v $
Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/