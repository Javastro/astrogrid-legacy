/*$Id: QueryHelper.java,v 1.1 2003/11/18 14:24:16 nw Exp $
 * Created on 18-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.common;

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.axisdataserver.types.Query;

/** helper class for building query objects 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Nov-2003
 *
 */
public class QueryHelper {

    /**
     * 
     */
    private QueryHelper() {
    }
    
    /** construct the simplet valid query object */
    public static Query buildMinimalQuery() {
        Query q = new Query();
        q.setSelect(ADQLUtils.buildMinimalQuery());
        return q;
    }

}


/* 
$Log: QueryHelper.java,v $
Revision 1.1  2003/11/18 14:24:16  nw
helper class for testing. may be able to remove later.
 
*/