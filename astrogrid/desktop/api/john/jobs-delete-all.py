#!/usr/bin/env python
#   Script to delete all jobs with a certain user-supplied status
#   Author: jdt@roe.ac.uk
from astrogrid import acr
import sys

if len(sys.argv)!=2:
    print "Delete all jobs of a certain status.  Statuses are: COMPLETED, ERROR"
    sys.exit(0)
status = sys.argv[1]

listOfAllJobs = acr.astrogrid.jobs.listFully()

for job in listOfAllJobs:
    if job['status']==status:
        print "Deleting:", job['name'], job['id']
        acr.astrogrid.jobs.deleteJob(job['id'])


    


