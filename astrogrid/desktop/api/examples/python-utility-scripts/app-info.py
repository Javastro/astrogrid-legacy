#!/usr/bin/env python
# produce fomatted information about a CEA application
# usage : app-info.py [--text | --xml] <CEA applicaiton ivorn>
# return : formatted text, or xml registry entry for this application.
# option : --text (default) - return formatted text
# option : --xml - return raw xml registry data.
# example : app-info.py ivo://org.astrogrid/HyperZ
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
if len(sys.argv) > 2 and (sys.argv[1] == "--xml" or sys.argv[1] == "-x"):
        print s.astrogrid.registry.getRecord(sys.argv[2])
else :
        print s.astrogrid.applications.getDocumentation(sys.argv[len(sys.argv)-1])

