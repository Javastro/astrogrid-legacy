/*$Id: CachingNodeDelegate.java,v 1.6 2007/04/04 08:58:38 nw Exp $
 * Created on 16-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.filemanager.client.delegate;


import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeNotFoundFault;

import org.apache.commons.collections.map.ReferenceMap;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

/**
 * implementaiton of node delegate that caches previously retreived nodes.
 * <p>
 * uses a reference map to implement the cache - so nodes that are no longer being used can be garbage collected. 
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2005
 *  
 */
public class CachingNodeDelegate extends VanillaNodeDelegate {

    public FileManagerNode getAccount(AccountIdent ident) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        return super.getAccount(ident);
    }

    public FileManagerNode getNode(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        if (cache.containsKey(nodeIvorn)) {
            return (FileManagerNode)cache.get(nodeIvorn);
        } else {
            return super.getNode(nodeIvorn);
        }
    }

    
    public FileManagerNode getNodeIgnoreCache(NodeIvorn nodeIvorn) throws RemoteException,
            FileManagerFault, NodeNotFoundFault {
        return super.getNode(nodeIvorn);
    }
    public void delete(NodeIvorn nodeIvorn) throws RemoteException, FileManagerFault,
            NodeNotFoundFault {
        super.delete(nodeIvorn);
        if (cache.containsKey(nodeIvorn)) {
            cache.remove(nodeIvorn);
        }
    }

    /**
     * caches a bundle of nodes, returns the wrapped version of the first in the
     * bundle
     */
    protected FileManagerNode returnFirst(org.astrogrid.filemanager.common.Node[] ns) {
        FileManagerNode first = null;
        for (int i = 0; i < ns.length; i++) {
            org.astrogrid.filemanager.common.Node bean = ns[i];
            NodeIvorn key = bean.getIvorn();
            AxisNodeWrapper wrapper = null;

            if (cache.containsKey(key)) { // update internal
                                                           // data bean
                wrapper = (AxisNodeWrapper) cache.get(key);
                wrapper.setBean(bean); // will only be set if
                                                        // therer are changes. i

            } else { // create new bean.
                wrapper = new AxisNodeWrapper(bean, this);
                cache.put(key, wrapper);
            }

            if (i == 0) { //hang on to this node, if it was the first in
                              // the bundle.
                first = wrapper;
            }
        }// end for.
        return first;
    }

    /**
     * Construct a new CachingNodeDelegate
     * 
     * @param fm
     * @param hints
     */
    public CachingNodeDelegate(FileManagerPortType fm, BundlePreferences hints) {
        super(fm, hints);
        cache =createCache();
    }

    protected final Map cache;
    
    // override to provide a different cache implementation.
    protected Map createCache() {
    /** cache for nodes - hard references for keys, weak references for values. 
     * @modified nww - corrected from WEAK to SOFT - otherwise it doesn't cache.*/
    	return new ReferenceMap(ReferenceMap.HARD,ReferenceMap.SOFT);

    }
    public void clearCache() {
        this.cache.clear();
    }

    public int size() {
        return this.cache.size();
    }
    
    public boolean isCached(NodeIvorn ivo) {
        return cache.containsKey(ivo);
    }
    
    public String toString() {
        StringBuffer result =  new StringBuffer("[Caching NodeDelegate ");
        result.append(super.toString());
        result.append("\n cached ");
        result.append(size());
        for (Iterator i = cache.values().iterator(); i.hasNext(); ) {
            result.append("\n");
            result.append(i.next().toString());
        }
        result.append("]");
        return result.toString();
    }

}

/*
 * $Log: CachingNodeDelegate.java,v $
 * Revision 1.6  2007/04/04 08:58:38  nw
 * altered visibliltiy of some components, to make them easier to extend.
 *
 * Revision 1.5  2005/05/04 08:37:04  clq2
 * fixed deleting from portal
 *
 * Revision 1.2.22.1  2005/05/03 10:41:50  nw
 * back-merged from latest in head - to make it easier for catherine to merge in.
 *
 * Revision 1.4  2005/04/14 12:05:24  nw
 * another cache fix - enable to peek inside the cache.
 * doesn't effect existing code.
 *
 * Revision 1.3  2005/04/13 13:10:09  nw
 * fixed cache to use soft, rather than weak references
 *
 * Revision 1.2  2005/03/11 13:37:05  clq2
 * new filemanager merged with filemanager-nww-jdt-903-943
 *
 * Revision 1.1.2.3  2005/03/01 15:07:28  nw
 * close to finished now.
 *
 * Revision 1.1.2.2  2005/02/27 23:03:12  nw
 * first cut of talking to filestore
 *
 * Revision 1.1.2.1  2005/02/18 15:50:14  nw
 * lots of changes.
 * introduced new schema-driven soap binding, got soap-based unit tests
 * working again (still some failures)
 *
 */