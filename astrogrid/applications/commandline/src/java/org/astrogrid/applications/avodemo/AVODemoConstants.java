/*
 * $Id: AVODemoConstants.java,v 1.1 2004/09/06 17:18:12 pah Exp $
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

public final static String ACCOUNT= "frog@org.astrogrid.localhost";
public final static String GROUP = "test";
public final static String TOKEN = "dummy";
public final static String appconEndPoint = "http://astrogrid.jb.man.ac.uk:8080/astrogrid-applications-Int05_release/services/ApplicationControllerService";
public final static String SENDMAILHOST = "localhost";
public static final String COMMUNITY = "org.astrogrid.localhost";
public static final String MYSPACE = COMMUNITY + "/myspace";

   /**
    * make sure that it cannot be instantiated.
    */
   private AVODemoConstants() {
   }

}
