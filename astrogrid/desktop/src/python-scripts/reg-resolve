#!/usr/bin/env python
# retreive a registered service to endpoint
# usage: reg-resolve <ivorn>
# return : url of the endpoint for this service
# example reg-resolve uk.ac.le.star/filemanager
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
print s.astrogrid.registry.resolveIdentifier(sys.argv[1])