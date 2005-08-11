#!/usr/bin/env python
# write data to a vospace resource
# usage voput <local-file> <vospace-ivorn> 
import xmlrpclib as x
import sys
import os
import os.path
import urlparse

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
ivorn = sys.argv[2]

# build url from local file reference.
f = file(sys.argv[1])
url = urlparse.urlunsplit(["file","",os.path.abspath(f.name),"",""])

# create node if needed.
if not s.astrogrid.myspace.exists(ivorn):
	s.astrogrid.myspace.createFile(ivorn)
	
s.astrogrid.myspace.copyURLToContent(url,ivorn)

