#!/usr/bin/env python
# delete a vospace file
# usage : vorm.py <vospace-ivorn>
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
ivorn = sys.argv[1]
s.astrogrid.myspace.delete(ivorn)
