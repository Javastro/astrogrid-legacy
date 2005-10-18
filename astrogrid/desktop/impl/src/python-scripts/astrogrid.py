# A module to connect to the ACR
# Use it as:
#   from import import acr
#   Then acr.astrogrid.myspace ....etc
#   Author: jdt@roe.ac.uk

import os
import xmlrpclib
try:
    propsFile = file(os.path.expanduser('~/.astrogrid-desktop'))
except IOError,e:
    print "Couldn't load the ACR config file - please start your ACR"
    print e
    os._exit(1)
    
url = propsFile.readline().rstrip()
acr = xmlrpclib.Server(url+"xmlrpc");

