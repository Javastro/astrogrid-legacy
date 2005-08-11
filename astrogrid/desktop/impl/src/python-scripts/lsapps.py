#!/usr/bin/env python
# List registered applications
# usage : lsapps.py [-f|--full]
# return : list of application names
# option : [-f | --full] - return fuller descriptions of applications
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
if len(sys.argv) > 1 and (sys.argv[1] == "--full" or sys.argv[1] == "-f"):
 for i in s.astrogrid.applications.listFully():
  print i['name'], ":", i['id'], ":", "interfaces " + str(i['interfaces'])
else :
 for i in s.astrogrid.applications.list():
  print i

