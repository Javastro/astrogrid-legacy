#!/usr/bin/env python
# List all jobs for this user.
# usage : jobs-list.py [f|--full]
# return : list of jobURNs
# option : [-f | --full] - return fuller descriptions of jobs
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
if len(sys.argv) > 1 and (sys.argv[1] == "--full" or sys.argv[1] == "-f"):
	for i in s.astrogrid.jobs.listFully() :
		print i['name'], ":", i['id'], ':', i['description']
		print "", i['status'], ":", i['startTime'], ":", i['finishTime']
else :
	for urn in s.astrogrid.jobs.list() :
        	print urn