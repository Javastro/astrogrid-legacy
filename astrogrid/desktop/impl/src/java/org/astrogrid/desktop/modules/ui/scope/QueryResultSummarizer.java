/*$Id: QueryResultSummarizer.java,v 1.3 2007/05/03 19:20:43 nw Exp $
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


public interface QueryResultSummarizer {

    /** called by a retriever to add a result to the table.
     * 
     * @param ri description of the service
     * @param resultCount number of result returned (-1 indicates error)
     * @param message optional message
     */
    public void addQueryResult(Service ri, int resultCount, String message);

    /** clear all previous summaries */
    public void clear();
    
/** resultCount constant indicating 'error' */
    public static int ERROR = -1;
}

/* 
 $Log: QueryResultSummarizer.java,v $
 Revision 1.3  2007/05/03 19:20:43  nw
 removed helioscope.merged into uberscope.

 Revision 1.2  2006/08/15 10:01:12  nw
 migrated from old to new registry models.

 Revision 1.1  2006/02/02 14:51:11  nw
 components of astroscope, plus new ssap component.
 
 */