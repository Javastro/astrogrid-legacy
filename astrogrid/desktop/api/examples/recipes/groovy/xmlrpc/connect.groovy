#!/usr/bin/env groovy
/**
Noel Winstanley, Astrogrid, 2005


minimal example of connecting to AR and calling a service using XML-RPC connection method

requires no additional library jars added to groovy
connects to acr using xmlrpci access 
*/

//CONNECT TO AR
// read AR connection file
f = new File(System.getProperty('user.home'),".astrogrid-desktop")
assert f.exists(), 'Please start your AR and rerun this script'
// create xmlrpc connection.
path = f.getText().trim()
acr = new groovy.net.xmlrpc.XMLRPCServerProxy("${path}xmlrpc")


//retrieve a service from the acr - by specifying the interface class

//call a function of the acr - by giving the full module.service.function name
l = acr.system.configuration.list()
println "Results of configuration.list()"
for (key in l) {
	println key
}

//an alternative way of calling functions of ACR - first take a reference to the service
registry = acr.astrogrid.registry

//and then call functions on this service
u = 'ivo://org.astrogrid/Pegase'
println "Results of registry.getResourceInformation()"
println registry.getResourceInformation(u) // returns a struct of data.

u = "ivo://uk.ac.le.star/filemanager"
println "Results of registry.resolveIdentifier()"
println registry.resolveIdentifier(u) // returns a java.net.URL


