#!/usr/bin/env python
# read contents of a myspace resource
# usage : voget.py [-u] <myspace-ivorn>
# return contents of this resource, or url where data may be fetched from
# option : -u, --url return url to data, rather than data itself.
import xmlrpclib as x
import sys
import os
import urllib2

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
path = sys.argv[len(sys.argv) -1]
url =  s.astrogrid.myspace.getReadContentURL(path)
if sys.argv[1] == "-u" or sys.argv[1] == "--url" :
        print url
else:
        resource = urllib2.urlopen(url)
        print resource.read()