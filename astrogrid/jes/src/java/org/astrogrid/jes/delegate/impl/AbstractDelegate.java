/*$Id: AbstractDelegate.java,v 1.2 2004/02/09 11:41:44 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.Delegate;

import org.apache.commons.logging.Log;

/**
 * abstract class that captures common functionality of all jes delegates 
 * (in fact, probably all astrogrid delegates).
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public abstract class  AbstractDelegate implements Delegate {
    protected static final Log log = org.apache.commons.logging.LogFactory.getLog(AbstractDelegate.class);
    protected static final int DEFAULT_TIMEOUT = 60000;

    protected String targetEndPoint = null;

    protected int timeout = DEFAULT_TIMEOUT;

    public void setTargetEndPoint(String targetEndPoint) {
    	this.targetEndPoint = targetEndPoint;
    }

    public String getTargetEndPoint() {
    	return targetEndPoint;
    }

    public void setTimeout(int timeout) {
    	this.timeout = timeout;
    }

    public int getTimeout() {
    	return timeout;
    }
    
    /** helper method for determining when to create a 'test' delegate */
    protected static boolean isTestDelegateRequired(String endpoint) {
        if (endpoint == null || endpoint.trim().length() == 0) {
            log.warn("Null endpoint passed in - selecting test delegate. Select this delegat explicitly by using the endpoint " + TEST_URI);
            return true;
        }
        if (endpoint.equalsIgnoreCase(TEST_URI)) {
            log.info("Creating test delegate");
            return true;
        } else {
            return false;
        }                
    }
}
/* 
$Log: AbstractDelegate.java,v $
Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/