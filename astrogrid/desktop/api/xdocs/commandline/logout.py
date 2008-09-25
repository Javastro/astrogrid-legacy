#!/usr/bin/env python
# logout of astrogrid
# usage: logout
# returns : nothing
import xmlrpclib 
import sys
import os
import optparse
#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

parser = optparse.OptionParser(usage='%prog',description='Log out of Astrogrid')
parser.parse_args() # ignored - just used to provide help

ar.astrogrid.community.logout()