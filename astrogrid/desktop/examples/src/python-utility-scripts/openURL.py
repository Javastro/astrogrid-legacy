#!/usr/bin/env python
# open a resource in the system browser
# usage: openURL <url>
# return nothing
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.system.browser.openURL(sys.argv[1])