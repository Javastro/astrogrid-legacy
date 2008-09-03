/*
 * $Id: ContainerInstallationTestSuite.java,v 1.2 2008/09/03 14:18:57 pah Exp $
 * 
 * Created on 8 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.component;

import junit.framework.Test;
import junit.framework.TestCase;

public class ContainerInstallationTestSuite extends TestCase {

    public static Test suite() {         

	return CEAComponentContainer.getInstance().getSuite();
    }
}


/*
 * $Log: ContainerInstallationTestSuite.java,v $
 * Revision 1.2  2008/09/03 14:18:57  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/04/08 14:45:10  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 */
