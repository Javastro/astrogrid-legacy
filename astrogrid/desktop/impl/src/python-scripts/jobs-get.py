#!/usr/bin/env python
# retreive a transcript or summary for a job
# usage : jobs-get.py [--summary] <job-urn>
# return : xml document of job transcript, or text summary
# option : --summary - return a summary of the job. otherwise return the whole transcript.
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
if len(sys.argv) > 2 and (sys.argv[1] == "--summary" or sys.argv[1] == "-s"):
 i = s.astrogrid.jobs.getJobInformation(sys.argv[2])
 print i['name'], ":", i['id'], ':', i['description']
 print "", i['status'], ":", i['startTime'], ":", i['finishTime']
else :
 print s.astrogrid.jobs.getJobTranscript(sys.argv[1])

