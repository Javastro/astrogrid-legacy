/*
 * $Id: ApplicationFactory.java,v 1.2 2008/09/03 14:18:33 pah Exp $
 * 
 * Created on 12 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.community.User;

/**
 * Creating new application instances.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface ApplicationFactory {

    /**
     * Apply this application description to a set of parameters, to create an instance of the application, ready to execute.
     * @param callerAssignedID external identifer for the new application. This identifier is assigned by the caller, but is not used within CEA for distinguishing the application
     * @param user the user whose permissions to execute this tool under
     * @param tool data object that defines which interface to call, and with what parameter values.
     * @return an <tt>Application</tt>
     * @throws Exception
     */
    public  Application initializeApplication(String callerAssignedID, User user, Tool tool) throws Exception;

}


/*
 * $Log: ApplicationFactory.java,v $
 * Revision 1.2  2008/09/03 14:18:33  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.1.2.3  2008/05/01 15:22:48  pah
 * updates to tool
 *
 * Revision 1.1.2.2  2008/04/17 16:08:32  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.1.2.1  2008/03/19 23:10:54  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
