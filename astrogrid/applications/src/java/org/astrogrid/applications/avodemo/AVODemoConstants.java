/*
 * $Id: AVODemoConstants.java,v 1.4 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 23-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.avodemo;

import org.astrogrid.community.User;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public final class AVODemoConstants {

public final static String ACCOUNT= "avodemo@test.astrogrid.org";
public final static String GROUP = "test";
public final static String TOKEN = "dummy";
public final static String appconEndPoint = "http://localhost:8080/astrogrid-applications/services/ApplicationControllerService";
public final static String SENDMAILHOST = "localhost";
   /**
    * make sure that it cannot be instantiated.
    */
   private AVODemoConstants() {
   }

}
