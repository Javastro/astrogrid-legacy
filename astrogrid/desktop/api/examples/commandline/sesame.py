#!/usr/bin/env python
# resolve an object name to a position using the Sesame service.
#Usage - sesame.py [-a] objectName
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

result = s.cds.sesame.resolve(sys.argv[len(sys.argv)-1])
if len(sys.argv) > 2 and (sys.argv[1] == "--all" or sys.argv[1] == "-a"):
    print """
Object Name: %(OName)s, Type: %(OType)s
RA,DEC: %(ra)f,%(dec)f
Sexagesimal: %(posStr)s
Aliases: %(aliases)s""" %  result
else :
    print "%(ra)f,%(dec)f" % result

