/*$Id: SimpleComponentDescriptor.java,v 1.2 2004/03/07 21:04:38 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import junit.framework.Test;

/** Handy ad-hoc implementation of a component descriptor
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class SimpleComponentDescriptor implements ComponentDescriptor {
    protected SimpleComponentDescriptor(){
    }
    /** Construct a new SimpleComponentDescriptor
     * 
     */
    public SimpleComponentDescriptor(String name,String description) {
        this(name,description,null);
    }
        
   public SimpleComponentDescriptor(String name,String description,Test test) {
        this.test = test;
        this.name = name;
        this.description = description;
    }
    protected String name;
    protected  String description;
    protected Test test;
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return name;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return description;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return test;
    }
}


/* 
$Log: SimpleComponentDescriptor.java,v $
Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/