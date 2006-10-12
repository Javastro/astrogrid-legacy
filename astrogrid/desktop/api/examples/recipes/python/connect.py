#!/usr/bin/env python
# Noel Winstanley, Astrogrid, 2005
# minimal example of connecting to AR and calling a service.
import xmlrpclib 
import sys
import os

#parse the configuration file.
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
endpoint = prefix + "xmlrpc"
print "Endpoint to connect to is", endpoint

#connect to the acr 
acr = xmlrpclib.Server(endpoint)

#get a reference to the registry service from the AR
registry = acr.astrogrid.registry

#call a method
print registry.getResourceInformation('ivo://org.astrogrid/Pegase') 
	# returns a struct of data

print registry.getRecord('ivo://org.astrogrid/Pegase') 
	# return the xml of a registry entry (string)

print registry.resolveIdentifier('ivo://uk.ac.le.star/filemanager')