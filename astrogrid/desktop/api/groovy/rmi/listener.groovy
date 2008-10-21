#!/usr/bin/env groovy
import org.astrogrid.acr.*
import org.astrogrid.acr.astrogrid.*
import org.astrogrid.acr.system.*
/*
Noel Winstanley, Astrogrid, 2005

demonstration of attaching an event listener to the AR

requires acr client jars in groovy classpath
connects to acr using rmi access - listeners are not available in the xmlrpc access method
*/

//just proints out the events
class MyListener implements UserLoginListener {
  void userLogin(UserLoginEvent e) {
    println "Logged in"
	}
  void userLogout(UserLoginEvent e) {
    println "Logged out"
	}
}

// connect to the acr
f = new Finder()
acr = f.find()


// register the listener
community = acr.getService(Community.class) // get a service by classname
community.addUserLoginListener(new MyListener())

myspace = acr.getService("astrogrid.myspace") // get a service by name - alternative would be reg.getService(MySpace.class)

// assuming the acr is not already logged in, the following will force a login, and we will observe a callback to the listener
println myspace.getHome()

// shut this script down - necessary, as won't close by itself - it will keep listening for events.
System.exit(0)
