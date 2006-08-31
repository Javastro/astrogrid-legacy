#!/usr/bin/env python
# lists configuration keys.
# usage: config-list
# returns : text list of key-value settings
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
for (i, j) in  s.system.configuration.list().iteritems():
        print i, ":", j
