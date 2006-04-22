#!/usr/bin/env groovy
import org.astrogrid.acr.*
import org.astrogrid.acr.system.*
import org.astrogrid.acr.opt.*
/**
Noel Winstanley, Astrogrid, 2005

advanced - example of instantiating a component in a picocontainer, where dependencies are supplied from the remote acr.

requires acr client jars in groovy classpath
connects to acr using rmi access - listeners are not available in the xmlrpc access method
*/

// sample component
class MyComponent implements Runnable {
	MyComponent(Configuration c) { #has a dependency on the configuration service. obviously could have many more dependencies here.
		println "component instantiated"
		this.c =c
	}
	Configuration c
	void run() {
		println "component running"
		c.list().each { println it }
	}	
}

// connect to acr as usual
f = new Finder()
acr = f.find()

// create the acr-pico container.
cont = new AcrPicoContainer(acr)
// create a normal container as a child of this one (work-around), and register a component with it.
mut = new org.picocontainer.defaults.DefaultPicoContainer(cont)
mut.registerComponentImplementation(MyComponent.class)

// retrieve an instance of that component, and run it.
println "retrieving component instance"
r =  mut.getComponentInstance(MyComponent.class)
r.run()

// shut the app down - necessary, as won't close by itself.
System.exit(0)
