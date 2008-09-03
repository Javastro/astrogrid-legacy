/*
 * $Id: MockCeaConfiguration.java,v 1.2 2008/09/03 14:19:05 pah Exp $
 * 
 * Created on 1 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.contracts;

import java.io.File;
import java.io.IOException;

public class MockCeaConfiguration extends CEAConfiguration {

    @Override
    public void setTemporaryFilesDirectory(File temporaryFilesDirectory) {
	//ignore the request and make a temp directory so that files do not clash during tests
	try {
	    this.temporaryFilesDirectory = makeTemporaryDirectory();
	System.out.println("temp files directory="+temporaryFilesDirectory.getAbsolutePath());
	} catch (IOException e) {
	    this.temporaryFilesDirectory = temporaryFilesDirectory;
	    e.printStackTrace();
	    System.err.println("error creating temp directory - setting to requested as fallback "+temporaryFilesDirectory.getAbsolutePath());
	}
    }

    
    
}


/*
 * $Log: MockCeaConfiguration.java,v $
 * Revision 1.2  2008/09/03 14:19:05  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/09/03 12:22:54  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 */
