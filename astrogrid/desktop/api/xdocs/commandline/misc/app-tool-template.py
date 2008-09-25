#!/usr/bin/env python
# create a template tool document that can be used to call a cea service
# usage : app-tool-template.py <CEA application ivorn> [<interface name>]
# return : a template tool document, with elements for the parameters required by this interface.
# e.g ./app-tool-template.py ivo://org.astrogrid/HyperZ
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
if len(sys.argv) < 3:
 iface = "default"
else:
 iface = sys.argv[2]

print s.astrogrid.applications.createTemplateDocument(sys.argv[1],iface)

