/*
 * $Id: HyperZTest.java,v 1.2 2008/09/03 14:19:08 pah Exp $
 * 
 * Created on 4 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.hyperz;


import org.astrogrid.applications.commandline.AbstractCmdLineRealAppTestCase;
import org.astrogrid.applications.description.execution.Tool;
import org.junit.Before;

public class HyperZTest extends AbstractCmdLineRealAppTestCase{

    @Before
    public void setUp() throws Exception {
	super.setUp();
    }

    @Override
    protected Tool buildTool(String delay) throws Exception {
	// TODO Auto-generated method stub
	throw new  UnsupportedOperationException("HyperZTest.buildTool() not implemented");
    }

    @Override
    protected TestAppInfo setupApplication() {
	return new TestAppInfo(){

	    public String getAppName() {
		return "ivo://org.astrogrid/HyperZ";
	    }

	    public String getInterfaceName() {
		return "simple";
	    }
	    
	};
    }

}


/*
 * $Log: HyperZTest.java,v $
 * Revision 1.2  2008/09/03 14:19:08  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/05/01 15:36:08  pah
 * incorporated chiba xforms
 *
 * Revision 1.1.2.2  2008/04/17 16:16:55  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 * some uws functionality present - just the bare bones.
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.1.2.1  2008/04/08 14:04:11  pah
 * Completed move to using spring as container for webapp - replaced picocontainer
 *
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 *
 */
