#!/usr/bin/env python
# logout of astrogrid
# usage: logout
# returns : nothing
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.astrogrid.community.logout()