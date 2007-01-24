package examples;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.builtin.ACR;

/*$Id: Listener.java,v 1.2 2007/01/24 14:04:46 nw Exp $
 * Created on 13-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 13-Jan-2006
 demonstration of attaching a listener to the AR

requires AR client jars in java  classpath
connects to acr using rmi access - listeners are not available in the xmlrpc access method
*/

public class Listener {

//  just proints out the events
    public static class MyListener implements UserLoginListener {
      public void userLogin(UserLoginEvent e) {
        System.out.println("Logged in");
        }
      public void userLogout(UserLoginEvent e) {
        System.out.println("Logged out");
        }
    }

    public static void main(String[] args) throws Exception {
//          connect to the acr
            Finder f = new Finder();
            ACR acr = f.find();


//             register the listener
            Community community = (Community)acr.getService(Community.class);// get a service by classname
            community.addUserLoginListener(new MyListener());

            Myspace myspace = (Myspace) acr.getService("astrogrid.myspace"); // get a service by name - alternative would be reg.getService(MySpace.class)

//             assuming the acr is not already logged in, the following will force a login, and we will observe a callback to the listener
            System.out.println( myspace.getHome());

         
//      shut the app down - necessary, as won't close by itself.
        System.exit(0)   ;        
    }
}


/* 
$Log: Listener.java,v $
Revision 1.2  2007/01/24 14:04:46  nw
updated my email address

Revision 1.1  2006/10/12 02:24:39  nw
build script,fixed for java 1.4

Revision 1.1  2006/08/31 20:19:57  nw
moved examples into api.

Revision 1.1  2006/06/29 01:44:23  nw
*** empty log message ***

Revision 1.1  2006/04/22 12:41:55  nw
refactored workbench into ar.

Revision 1.1  2006/01/13 15:44:22  nw
new set of examples.
 
*/