#!/usr/bin/env python
# submit a job for executioin
# useage :jobs-submit <local file containing workflow document>
# return : jobURN of new job
# example: jobs-submit myworkflow.xml

import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
wf = file(sys.argv[1]).read()
print s.astrogrid.jobs.submitJob(wf)
