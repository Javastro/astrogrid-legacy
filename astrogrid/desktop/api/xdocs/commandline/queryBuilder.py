#!/usr/bin/env python
# show  queryBuilder user interface
# usage: queryBuilder
# return : nothing.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.ui.queryBuilder.show()
