#!/usr/bin/env groovy
/**
Noel Winstanley, Astrogrid, 2005


minimal example of connecting to AR and calling a service

requires acr client jars in groovy classpath
connects to acr using rmi access 
*/
import org.astrogrid.acr.*
import org.astrogrid.acr.system.*
import java.net.URI

// connect to the acr. 
f = new Finder()
acr = f.find()

// retrieve a service from the acr - by specifying the interface class
conf = acr.getService(Configuration.class)
// call a method on this service.
l = conf.list()
l.each {println it}

// retrieve another service from the acr - this time by name
registry = acr.getService("astrogrid.registry")
// use this service.. note that we must create a uri to pass as a parameter - a string won't work (groovy isn't that loosly typed)

u = new URI('ivo://org.astrogrid/Pegase')
println registry.getResourceInformation(u) # returns a struct of data.
# registry.getRecord(u) returns a org.w3c.dom.Document..

u = new URI('ivo://uk.ac.le.star/filemanager')
println registry.resolveIdentifier(u) # returns a java.net.URL



// shut the app down - necessary, as won't close by itself.
System.exit(0)

