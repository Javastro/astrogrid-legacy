#!/usr/bin/env python
# Run a bunch of parallel ADQL queries for stress-testing a DSA
#
# This script makes use of the standard stresstesting commandline options.
# innerloop defines how many queries are run in parallel, and outerloop
# defines how many times the overall test (a set of queries in parallel).
#
# The script supports the following additional options:
#
#   -r <n> / --rows=<n>  
#   Number of rows to be returned by each query (default 10k)
# 
#   -d <cea app ivorn> / --dsa=<cea app ivorn>
#   The ivorn of the DSA CEA application to use for querying
#   Default: org.astrogrid.regtest.dsa/mysql-first/CatName_first/ceaApplication
#
#   -q <querytable name> / --querytable=<querytable name>
#   The name of the actual table to be queried (needs to match the DSA chosen)
#
#   Sample invocation:
#
#  python runADQLs.py -u ktn -p ktn -o 5 -i 10 -r 10000 -c org.astrogrid.regtest.dsa/mysql-first/CatName_first/ceaApplication -q TabNameFirst_catalogue -t
#	
##############################################################################

import xmlrpclib as x
import sys
import os
import os.path
from threading import Thread
import urlparse
import time
from optparse import OptionParser

sys.path.append("../dave/")
from startup import *
from myspace import *
from logger import *


#-------------------------------------------------------------------------------
class StatusChecker(Thread) :
	def __init__(self,runningIDs,startTimes,numQueries,outerloopcount) :
		Thread.__init__(self)
		self.runningIDs = runningIDs
		self.startTimes = startTimes
		self.numQueries = numQueries
		self.outerloopcount = outerloopcount

	def writeLogLine(self,outerloop, innerloop, startTime,endTime,status) :
#		logging.info(
#			"EXPORT ACTION [query] OUTERLOOP [%d] INNERLOOP [%d] TIMING [%f] STATUS [%s]\n",
#			outerloop,innerloop,endTime-startTime,status);

        logging.info(
            "TEST [%s] ACTION [%s] LOOP [%d][%d] TIME [%f] STATUS [%s]",
            testname,
            'query',
            outer,
            inner
            endTime-startTime,
            status
            )


	#

	def run(self) :
		retryCount = 0
		maxRetryCount = 30	# Will wait at least 30 seconds for jobs to appear
		processed = {}
		finishedIDs = {}
		failedIDs = {}
		unknownIDs = {}
		endTimes = {}
		
		# Monitor the status of the jobs 
		while ( (len(processed) < self.numQueries) and (retryCount < maxRetryCount)):
			keys = self.runningIDs.keys()
			logging.debug("Checking status, %d active jobs remaining\n" % len(keys))
			for i in range(len(keys)):
				key = keys[i]
				try :
					execInfo = s.astrogrid.applications.getExecutionInformation(self.runningIDs[key])	
	
					# Handle successfully completed jobs
					if (execInfo['status'] == "COMPLETED") :
						if (not processed.has_key(key)) :
							endTimes[key] = time.time()
							self.writeLogLine(
								self.outerloopcount, key, self.startTimes[key],endTimes[key],"OK")
							finishedIDs[key] = self.runningIDs[key]
							processed[key] = 1
	
					# Handle jobs that failed cleanly
					elif (execInfo['status'] == "ERROR") :
						if (not processed.has_key(key)) :
							endTimes[key] = time.time()
							self.writeLogLine(
								self.outerloopcount, key, self.startTimes[key],endTimes[key],"ERROR")
							failedIDs[key] = self.runningIDs[key]
							processed[key] = 1
	
					# Handle jobs that failed cleanly
					elif ((execInfo['status'] != "RUNNING") and 
								(execInfo['status'] != "PENDING") and
								(execInfo['status'] != "UNKNOWN")) : 
						if (not processed.has_key(key)) :
							endTimes[key] = time.time()
							self.writeLogLine(
								self.outerloopcount, key, self.startTimes[key],endTimes[key],"UNKNOWN")
							unknownIDs[keys[i]] = self.runningIDs[key]
							processed[key] = 1

				except Exception, e:
					if (not processed.has_key(key)) :
						endTimes[key] = time.time()
						self.writeLogLine(
							self.outerloopcount, key, self.startTimes[key],endTimes[key],"UNKNOWN")
						unknownIDs[key] = self.runningIDs[key]
						processed[key] = 1
	
			if (len(self.runningIDs) == 0) :
				retryCount = retryCount + 1
				logging.debug("Didn't find jobs to monitor- upping retry count")
			time.sleep(1)
	
		if (retryCount >= maxRetryCount) :
			logging.error("Status monitor didn't find any jobs to monitor")
		else :
			# Print a small summary
			logging.debug("COMPLETED %d    ERROR %d    UNKNOWN %d\n" % (len(finishedIDs),len(failedIDs),len(unknownIDs)))

#-------------------------------------------------------------------------------
class AdqlQueryRunner(Thread) :
	def __init__(self,runningIDs,startTimes,ivorn,table,formats,top,destFolder,numQueries,query,outerloopcount) :
		Thread.__init__(self)
		self.runningIDs = runningIDs
		self.startTimes = startTimes
		self.ivorn = ivorn
		self.table = table
		self.formats = formats
		self.top = top
		self.destFolder = destFolder
		self.numQueries = numQueries
		self.query = query
		self.outerloopcount = outerloopcount
	#


	def run(self):
		dest = self.destFolder + "/new_fileSUBS_NUM.vot"
		# Make dictionaries for monitoring results
		#finishedIDs = {}
		#failedIDs = {}
		#unknownIDs = {}
		#endTimes = {}
	
		# Populate the templated query
		f = open(self.query,'r')
		template = f.read()
		template = template.replace("SUBS_IVORN",self.ivorn)
		template = template.replace("SUBS_TABLE",self.table)
		template = template.replace("SUBS_TOP",self.top)
		template = template.replace("SUBS_DEST",dest)

		# Submit the jobs
		for i in range(self.numQueries) :
			formatIndex = i % len(self.formats)
			formatIndex = 0
			subtemplate = template.replace("SUBS_NUM","%d"%(i))
			subtemplate = subtemplate.replace("SUBS_FORMAT","%s"%(self.formats[formatIndex]))
			try :
				self.runningIDs[i] = s.astrogrid.applications.submit(subtemplate)
				self.startTimes[i] = time.time()
				logging.debug("Launched job %d with id %s\n" % (i,self.runningIDs[i]))
			except Exception, e:
				try :
					del self.runningIDs[i]
				except Exception, e2:
					pass
				logging.error("Failed to launch CEA query:")
				logging.error(e)
				exit(1)
	
	# END OF runAdqlTests
#-------------------------------------------------------------------------------

# The task document to use
GL_query = "TEMPLATES/selectAll_query.xml"
#GL_query = "TEMPLATES/selectSome_query.xml"

# The results format(s) to use - round-robined if >1
GL_formats = ["VOTABLE"]
#GL_formats = ["VOTABLE", "VOTABLE-BINARY", "COMMA-SEPARATED"]

# Create a myspace wrapper.
logging.debug("Creating myspace object with info: %s %s %s", astrouser, astropass, astroauth)
myspace = MySpace(ar, astrouser, astropass, astroauth)
myspace.login(ar)

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

# Now run the tests
for looper in range(outerloop) :

	# Create the test folder.
	rootFolder =  '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':looper}
	logging.debug("CREATING ROOT FOLDER %s", rootFolder)
	myspace.createFolder(ar, rootFolder)
	GL_destFolder = "ivo://%s@%s/community#/%s" % (astrouser,astroauth,rootFolder)
	GL_runningIDs = {}
	GL_startTimes = {}

	queryRunner = AdqlQueryRunner(GL_runningIDs,GL_startTimes,ceaivorn,
		querytable,GL_formats,str(rows),GL_destFolder,innerloop,
		GL_query,looper)

	statusChecker = StatusChecker(GL_runningIDs,GL_startTimes,
		innerloop,looper)

	queryRunner.start()
	statusChecker.start()

	queryRunner.join()
	statusChecker.join()

# Clean up
if (testtidy):
	myspace.deleteFile(ar, '%(root)s' % {'root':astroroot})
#	myspace.logout(ar)

		
