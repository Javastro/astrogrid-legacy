#!/usr/bin/env python
# login to astrogrid
# usage: login <user> <password> <community>
# return : nothing.
import xmlrpclib 
import sys
import os
import optparse
#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

parser = optparse.OptionParser(usage='%prog <username> <password> <community>',description='Log-in to Astrogrid')
parser.add_option('-e','--examples',action='store_true',default=False
                  ,help='display some examples of use and exit')
(opts,args) = parser.parse_args()

if opts.examples:
    print """
login.py jimbean mypassword uk.ac.le.star
    """
    sys.exit()

if len(args) < 3:
    parser.error("Insufficient parameters provided")
    
ar.astrogrid.community.login(args[0],args[1],args[2])