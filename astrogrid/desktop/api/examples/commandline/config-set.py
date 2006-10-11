#!/usr/bin/env python
#  set a key value
# usage : config-set <key> <value>
# return nothing
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.system.configuration.setKey(sys.argv[1],sys.argv[2])