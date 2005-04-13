#!/usr/bin/env python
# login to astrogrid
# usage: login <user> <password> <community>
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.astrogrid.community.login(sys.argv[1],sys.argv[2],sys.argv[3])