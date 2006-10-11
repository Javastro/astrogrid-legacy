#!/usr/bin/env python
# sample of building and running an application
import xmlrpclib as x
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

ra = 240.94
dec = 55.10
saveimages = 1
ccdall  = 0



runno = ""
band=""
magzpt=""
ccdno=""
reffile = ""
fileno=""
count=""
#WFS DQC Query
print "Querying WFS Data Quality Control Database for images containing position " + ra + ", " + de 
source = "http://apm14.ast.cam.ac.uk/queryDQC?ra=" + ra + "&dec=" + dec
 
table = null #parsed up version to source.
nameCol = 