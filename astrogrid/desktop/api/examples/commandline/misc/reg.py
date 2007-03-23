#!/usr/bin/env python
# retreive a named record from the registry
# usage: reg [--struct] <ivorn>
# return : xml document, or structure, of registry entry
# example reg uk.ac.le.star/filemanager
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
regEndpoint = s.ivoa.registry.getSystemRegistryEndpoint()
if len(sys.argv) > 2 and (sys.argv[1] == "-s" or sys.argv[1] == "--struct"):
	print s.ivoa.registry.getResource(sys.argv[2])
else :
	print s.ivoa.externalRegistry.getResourceXML(regEndpoint,sys.argv[1])