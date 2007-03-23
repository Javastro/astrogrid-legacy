#!/usr/bin/env python
# produce fomatted information about a CEA application
import xmlrpclib
import sys
import os
import optparse

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

parser = optparse.OptionParser(usage='%prog [options] <application-id>',
                               description='produce formatted information about a CEA application')
parser.add_option('-e','--examples',action='store_true',default=False
                  ,help='display some examples of use and exit')
(opts,args) = parser.parse_args()

if opts.examples:
    print """
app-info.py
    -- run with default parameters
app-info.py ivo://org.astrogrid/HyperZ
    -- display text information about HyperZ
app-info.py ivo://mssl.ucl.ac.uk/solar_events_dsa/ceaApplication
    -- displayinformation about solar_events_dsa
  """
    sys.exit()

#default application id.
appId = 'ivo://org.astrogrid/Pegase'
if len(args) == 0:
    sys.stderr.write("No application provided - using default " + appId)
else:
    appId = args[0]

print ar.astrogrid.applications.getDocumentation(appId)

