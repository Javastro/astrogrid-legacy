/*
 * $Id: MockCEC.java,v 1.2 2011/09/02 21:55:54 pah Exp $
 * 
 * Created on 15 May 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.util.List;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.security.SecurityGuard;

public class MockCEC implements ExecutionController {

    public boolean abort(String executionId, SecurityGuard secGuard)
            throws CeaException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "MockCEC.abort() not implemented");
    }

    public boolean execute(String executionId, SecurityGuard secGuard)
            throws CeaException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "MockCEC.execute() not implemented");
    }

    public List<Application> getQueue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "MockCEC.getQueue() not implemented");
    }

    public String init(Tool tool, String jobstepID, SecurityGuard securityGuard)
            throws CeaException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "MockCEC.init() not implemented");
    }

}


/*
 * $Log: MockCEC.java,v $
 * Revision 1.2  2011/09/02 21:55:54  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 */
