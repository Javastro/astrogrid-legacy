/*
 * $Id: BuiltInJavaClassConfiguration.java,v 1.2 2008/09/03 14:18:44 pah Exp $
 * 
 * Created on 11 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.javaclass;

import java.io.IOException;

public class BuiltInJavaClassConfiguration extends BaseJavaClassConfiguration {

    public BuiltInJavaClassConfiguration() throws IOException{
	super("org.astrogrid.applications.javaclass.SampleJavaClassApplications");
    }
}


/*
 * $Log: BuiltInJavaClassConfiguration.java,v $
 * Revision 1.2  2008/09/03 14:18:44  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/06/16 21:58:58  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 */
