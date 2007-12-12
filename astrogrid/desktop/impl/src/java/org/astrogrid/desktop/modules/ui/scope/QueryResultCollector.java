/*$Id: QueryResultCollector.java,v 1.1 2007/12/12 13:54:13 nw Exp $
 * Created on 30-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

import edu.berkeley.guir.prefuse.graph.TreeNode;


public interface QueryResultCollector {

    /** report a query success
     */
    public void addQueryResult(Service ri,AstroscopeTableHandler handler);

    
    /** report a query failure */
    public void addQueryFailure(Service ri,Throwable t);
    /** clear all previous summaries */
    public void clear();
    

/** add these services to the list of things that are going to be queried.
 * @param services
 */
void addAll(Service[] services);

}

/* 
 $Log: QueryResultCollector.java,v $
 Revision 1.1  2007/12/12 13:54:13  nw
 astroscope upgrade, and minor changes for first beta release

 Revision 1.4  2007/05/10 19:35:22  nw
 reqwork

 Revision 1.3  2007/05/03 19:20:43  nw
 removed helioscope.merged into uberscope.

 Revision 1.2  2006/08/15 10:01:12  nw
 migrated from old to new registry models.

 Revision 1.1  2006/02/02 14:51:11  nw
 components of astroscope, plus new ssap component.
 
 */