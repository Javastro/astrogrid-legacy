/*
 * $Id: MockInterface.java,v 1.1 2009/05/15 22:51:19 pah Exp $
 * 
 * Created on 15 May 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.InterfaceDefinition;

public class MockInterface extends InterfaceDefinition implements
        ApplicationInterface {

}


/*
 * $Log: MockInterface.java,v $
 * Revision 1.1  2009/05/15 22:51:19  pah
 * ASSIGNED - bug 2911: improve authz configuration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
 * combined agast and old stuff
 * refactored to a more specific CEA policy interface
 * made sure that there are decision points nearly everywhere necessary  - still needed on the saved history
 *
 */
