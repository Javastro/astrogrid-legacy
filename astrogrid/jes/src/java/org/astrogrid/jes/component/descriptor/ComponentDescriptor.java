/*$Id: ComponentDescriptor.java,v 1.2 2004/03/15 23:45:07 nw Exp $
 * Created on 06-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.descriptor;

import junit.framework.Test;

/** An interface that components should implement to be self-documenting
 * <p>
 * Note that although it's nice for components to implement this interface, it is not mandatory - hence none of the component interfaces extend this one.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Mar-2004
 *
 */
public interface ComponentDescriptor {

    /** full name for the component */
    String getName();
    /** formatted description of the component, (with maybe details of current configuration */
    String getDescription();
    /** Access a junit test suite that when run will verify that the component instance and configured correctly
     * <p>
     * may return null if no tests are applicable. */
    Test getInstallationTest() ;
    
}


/* 
$Log: ComponentDescriptor.java,v $
Revision 1.2  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.1  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/