/*$Id: QueryResultCollector.java,v 1.4 2009/04/06 11:43:19 nw Exp $
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

import java.util.List;

import org.astrogrid.desktop.modules.system.Tuple;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;


/** interface to something that aggregates results from many retrievers. */
public interface QueryResultCollector {

    /** report a query success
     */
    public void addQueryResult(Retriever ri,AstroscopeTableHandler handler);

    
    /** report a query failure */
    public void addQueryFailure(Retriever ri,Throwable t);
    /** clear all previous summaries */
    public void clear();
    

/** add these retriever to the list of things that are going to be queried.
 * @param retrievers list of retrievers paired with a fileobject that is to be used as it's results directory.
 */
void addAll(final List<Tuple<Retriever,FileObjectView>> retrievers);

}

/* 
 $Log: QueryResultCollector.java,v $
 Revision 1.4  2009/04/06 11:43:19  nw
 Complete - taskConvert all to generics.

 Incomplete - taskVOSpace VFS integration

 Revision 1.3  2008/11/04 14:35:48  nw
 javadoc polishing

 Revision 1.2  2008/02/22 17:03:35  mbt
 Merge from branch mbt-desktop-2562.
 Basically, Retrievers rather than Services are now the objects (associated
 with TreeNodes) which communicate with external servers to acquire results.
 Since Registry v1.0 there may be multiple Retrievers (even of a given type)
 per Service.

 Revision 1.1.18.1  2008/02/21 11:06:09  mbt
 First bash at 2562.  AstroScope now runs multiple cone searches per Service

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
