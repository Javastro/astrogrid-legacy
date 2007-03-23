#!/usr/bin/env python
# perform a full registry query
# usage : reg-search.py [-x|--xquey] <query string of SADQL or XQuery>
# return : xml document of results.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

if len(sys.argv) > 1 and (sys.argv[1] == "--xquery" or sys.argv[1] == "-x"):
	print s.ivoa.registry.xquerySearchXML(sys.argv[2])
else:
	regEndpoint = s.ivoa.registry.getSystemRegistryEndpoint()
	print s.ivoa.externalRegistry.adqlxSearchXML(sys.argv[1],regEndpoint)