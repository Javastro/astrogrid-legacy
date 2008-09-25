#!/usr/bin/env python
# submit a tool document for execution
# usage : app-submit.py [options] <document-uri> [server]
# parameter: uri of tool document to submit
# options: see ./app-submit.py --help

import xmlrpclib as x
import sys
import os
import os.path
import urlparse
import time
from optparse import OptionParser

parser = OptionParser(usage="usage: %prog [options] <document-uri> [server]")
parser.add_option("-w","--wait",action="store_true", default=False, help="wait until application completes")
parser.add_option("-f","--fetch",action="store_true",default=False,help="Fetch results (implies --wait)")	

(options, args) = parser.parse_args()
	

if args[0][0:4] == 'ivo:' or args[0][0] == '#' or args[0][0:4] == 'ftp:' or args[0][0:5] == 'http:' or args[0][0:5] == 'file:' :
	documentUri = args[0]
else:
	f = file(args[0])
	documentUri = urlparse.urlunsplit(["file","",os.path.abspath(f.name),"",""])
	 
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
	 
	  
if len(args) == 2:
	execId = s.astrogrid.applications.submitStoredTo(documentUri,args[1])
else :
	execId = s.astrogrid.applications.submitStored(documentUri)

if options.wait or options.fetch:
	execInfo = s.astrogrid.applications.getExecutionInformation(execId)		 
	while execInfo['status'] == "RUNNING" :
		time.sleep(30)
		execInfo = s.astrogrid.applications.getExecutionInformation(execId)
else :
	print execId	
	
if options.fetch:
	results = s.astrogrid.applications.getResults(execId)
	if len(results) == 1:
		print results.values()[0]
	else:
		for (k,v) in results.items():
			print "Result :", k
			print v
elif options.wait:
	print s.astrogrid.applications.getExecutionInformation(execId)['status']