#!/usr/bin/env python
# get the value of a coonfiguration entry
# usage: config-get <keyname>
# return associated value, if any.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
print s.system.configuration.getKey(sys.argv[1])