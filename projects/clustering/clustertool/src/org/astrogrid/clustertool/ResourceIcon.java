/*
 * $Id: ResourceIcon.java,v 1.1 2009/09/14 19:09:26 pah Exp $
 * 
 * Created on Sep 14, 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.clustertool;

import javax.swing.ImageIcon;

public class ResourceIcon {

    protected static final ImageIcon CONNECT = makeIcon("connected-24.gif");
    protected static final ImageIcon DISCONNECT = makeIcon("disconnected-24.gif");
    public static final ImageIcon SAMP = makeIcon("comms2.gif");

    
   static private ImageIcon makeIcon(String name){
       
       return new javax.swing.ImageIcon(ResourceIcon.class.getResource("/org/astrogrid/clustertool/resources/"+name));
   }
   
}


/*
 * $Log: ResourceIcon.java,v $
 * Revision 1.1  2009/09/14 19:09:26  pah
 * basic framework of GUI mostly working
 *
 */
