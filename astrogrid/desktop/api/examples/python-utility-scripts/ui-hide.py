#!/usr/bin/env python
# hide dashboard user interface.
# usage: ui-login
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.system.ui.hide()
