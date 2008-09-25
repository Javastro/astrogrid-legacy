#!/usr/bin/env python
# show  astroscope user interface
# usage: astroscope
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.ui.astroscope.show()
