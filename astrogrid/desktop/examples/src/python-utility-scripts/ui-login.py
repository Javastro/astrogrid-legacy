#!/usr/bin/env python
# show ui login dialogue (if not already logged in)
# usage: ui-login
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.astrogrid.community.guiLogin()
