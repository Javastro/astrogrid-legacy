/*$Id: ComponentManagerFactory.java,v 1.2 2004/03/07 21:04:38 nw Exp $
 * Created on 16-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.jes.component.production.ProductionComponentManager;

import junit.framework.Test;
import junit.framework.TestSuite;


/**

 * @author Noel Winstanley nw@jb.man.ac.uk 16-Feb-2004
 *
 */
public class ComponentManagerFactory {


    /** get the instnace.
     * lazily initialized - parses configuration and creates components on first call
     * @return
     *@todo use properties to determine which component manager impl to use
     */
    public static synchronized ComponentManager getInstance() throws ComponentManagerException {
                if (theInstance == null) {
                    theInstance = new ProductionComponentManager();
                    theInstance.start();
                }           
        return theInstance;
    }
    
    protected static ComponentManager theInstance;
        
    /** unsafe, useful for testing */
    public static void _setInstance(ComponentManager mgr) {
        theInstance = mgr;
        theInstance.start();
    }
    
    private ComponentManagerFactory() {    
    }
    
  
    /** static method - makes this look like a normal test component ! */
    public static Test suite(){
        try {
            return ComponentManagerFactory.getInstance().getSuite();
        } catch (ComponentManagerException e) {
            return new TestSuite("No tests available - component manager failed with " + e.getMessage());
        }
    }


}

