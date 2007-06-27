#!/usr/bin/env python
# show new instance of filemanager
# usage: filemanager
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.ui.fileManager.show()
