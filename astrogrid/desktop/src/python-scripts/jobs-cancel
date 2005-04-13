#!/usr/bin/env python
# cancel a running job
# usage : jobs-cancel <job-urn>
# return : nothing
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.astrogrid.jobs.cancelJob(sys.argv[1])

