#!/usr/bin/env python
# shows an new instanceof voexplorer
# usage: voexplorer
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.ui.registryBrowser.show()
