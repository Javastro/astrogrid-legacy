#!/usr/bin/env python
# delete a completed job
# usage : jobs-delete <job-urn>
# return : nothing
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
s.astrogrid.jobs.deleteJob(sys.argv[1])

