/*$Id: Cache.java,v 1.2 2005/04/25 12:13:54 clq2 Exp $
 * Created on 12-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.collections.map.LRUMap;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
/**
 * A caching map - combination of a LeastRecentlyUsed map, with soft references.
 * As the implementation of soft references suggests that the JVM frees the least recently used reference too, then
 * we should get a nicely behaved cache - will have the fixed size until memory starts to get tight, where the least recently used items get freed.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Apr-2005
 *
 */

public class Cache {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Cache.class);

    public Cache(int size) {
        m = new LRUMap(size);
    }

    /** use a combination of a LRU map and a reference map. */
   protected final Map m;
    public void stuff(JobURN key, Object value) {
        Reference ref = new SoftReference(value);
        m.put(key.getContent(),ref);
    }
    
    public Object check(JobURN urn) {
        Reference ref = (Reference)m.get(urn.getContent());
       if (logger.isDebugEnabled()) {
           logger.debug("Cache " + (ref != null && ref.get() != null ? " HIT" : "MISS"));
       }
        return ref == null ? null : ref.get();
    }
    
    public void delete(JobURN key) {
        m.remove(key.getContent());
    }        
}

/* 
$Log: Cache.java,v $
Revision 1.2  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.1.2.1  2005/04/12 17:07:35  nw
optimization class
 
*/