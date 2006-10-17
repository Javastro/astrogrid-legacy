/*$Id: DalProtocolManager.java,v 1.4 2006/10/17 07:21:30 KevinBenson Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections.iterators.UnmodifiableIterator;
import org.astrogrid.acr.ivoa.resource.Service;

/**
 * aggregates a set of retreivers together - so they can be operated as a whole.
 * also manages a tablemodel that summarizes the resultls of querying each service - this is also accessible through
 * the separate {@link QueryResultSummarizer} interface - as later, we may want to refactor this elewhere.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class DalProtocolManager implements QueryResultSummarizer {

    public DalProtocolManager() {
        this.l = new ArrayList();
    }
    private final List l;
    private final DefaultTableModel table = new DefaultTableModel(
            new Object[]{"Service","Results","Message" /*,"URL"*/}
            ,0) {
    	public boolean isCellEditable(int i,int j) {
    		return false;
    	}
    };
    
    /** add a protocol to the manager */
    public void add(DalProtocol r) {
        l.add(r);
    }
    
    /** return an iterator over all the protocols in the manager */
    public Iterator iterator() {
        return UnmodifiableIterator.decorate(l.iterator());
    }
    
    /** return a table model containing query summaries for each retriever in the 
     * protocols
     * @return
     */
    public TableModel getQueryResultTable() {
        return table;
    }
    
    /**
     * @see org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer#addQueryResult(org.astrogrid.acr.astrogrid.ResourceInformation, java.lang.String, int, java.lang.String)
     */
    public void addQueryResult(Service ri,int result, String message) {
       table.addRow(new Object[]{ri, new Integer(result),message == null ? "" : message});
    }
    
    
    /**
     * @see org.astrogrid.desktop.modules.ui.scope.QueryResultSummarizer#addQueryResult(org.astrogrid.acr.astrogrid.ResourceInformation, java.lang.String, int, java.lang.String)
    public void addQueryResult(Service ri,int result, String message, String url) {

       table.addRow(new Object[]{ri, new Integer(result),message == null ? "" : message,url});
    }
    */

    public int size() {
        return l.size();
    }

    
    
       

}


/* 
$Log: DalProtocolManager.java,v $
Revision 1.4  2006/10/17 07:21:30  KevinBenson
small changes to history part of astroscope to have an object Name.  One small thing on Retriever to try and put successful status messages.

Revision 1.3  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.6.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/