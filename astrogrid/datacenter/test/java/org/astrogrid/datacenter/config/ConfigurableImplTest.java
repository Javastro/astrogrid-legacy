/*$Id: ConfigurableImplTest.java,v 1.2 2003/08/22 10:36:27 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.config;
import junit.framework.Test;
import junit.framework.TestSuite;

/** test case for ComfigurableImpl. 
 * Atg presernt, just extends the abstract ConfigurableTest class.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class ConfigurableImplTest extends ConfigurableTestSpec {

    /**
     * @param name
     */
    public ConfigurableImplTest(String name) {
        super(name);
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ConfigurableImplTest.class);
    }
    public static Test suite() {
        return new TestSuite(ConfigurableImplTest.class);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected Configurable createConfigurable() {
        return new ConfigurableImpl() {;};
    }

}


/* 
$Log: ConfigurableImplTest.java,v $
Revision 1.2  2003/08/22 10:36:27  nw
tidied imports

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.
 
*/