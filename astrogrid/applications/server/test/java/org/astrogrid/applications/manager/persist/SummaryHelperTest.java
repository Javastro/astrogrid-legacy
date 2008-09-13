/*
 * $Id: SummaryHelperTest.java,v 1.3 2008/09/13 09:51:02 pah Exp $
 * 
 * Created on 21 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.persist;

import static org.junit.Assert.*;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.javainternal.BuiltInApplicationDescriptionTest;
import org.junit.Before;
import org.junit.Test;

public class SummaryHelperTest extends BuiltInApplicationDescriptionTest {

 
    @Override
    @Before
    public void setUp() throws Exception {
	super.setUp();
    }

    @Test
    public void testSummarize() throws CeaException {
	testRunApplication();
	ExecutionSummaryType summary = SummaryHelper.summarize("testexecid", app);
	assertNotNull(summary);
    }

}


/*
 * $Log: SummaryHelperTest.java,v $
 * Revision 1.3  2008/09/13 09:51:02  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:59  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
 *
 */
