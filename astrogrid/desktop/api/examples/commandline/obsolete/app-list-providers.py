#!/usr/bin/env python
# List servers that provide a particular application
# usage : app-list-providers.py [-f|--full]<app-id>
# return : list of servers that support this application
# option : [-f | --full] - return fuller descriptions of servers
# example : app-list-providers.py ivo://org.astrogrid/HyperZ
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
arr = s.astrogrid.applications.listServersProviding(sys.argv[len(sys.argv)-1])
if len(sys.argv) > 2 and (sys.argv[1] == "--full" or sys.argv[1] == "-f"):
    for i in arr :
        print i['title'], ":", i['id'] 
else:
	for i in arr :
		print i['id']

