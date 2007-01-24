#!/usr/bin/env python
#Noel Winstanley, AstroGrid, 2006
#noel.winstanley@manchester.ac.uk
#Fire an xquery at a vo registry.
#Usage: start Workbench / AR
# 	execute 'python xquery.py myquery.xquery'
import xmlrpclib 
import sys
import os
import os.path


#usual boilerplate to parse the configuration file.
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
endpoint = prefix + "xmlrpc"

#connect to the acr 
ar = xmlrpclib.Server(endpoint)
f = open(sys.argv[1])
print ar.ivoa.registry.xquerySearchXML(f.read())



