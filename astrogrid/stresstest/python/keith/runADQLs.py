#!/usr/bin/env python
# Run a bunch of parallel ADQL queries for stress-testing a DSA
#
# Additional parameters:    
#  runADQLs.py -a <authID> -u <user> -p <password> -i <numqueries> -o <numtests># 


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
#from settings import *
from logger import *

#logger.setLevel(logging.DEBUG)

# Create a myspace wrapper.
logging.debug("Creating myspace object with info: %s %s %s", astrouser, astropass, astroauth)
#logging.debug("creating myspace object")
myspace = MySpace(ar, astrouser, astropass, astroauth)
myspace.login(ar)


# **************************************************************************
# KEITH: USER-CONFIGURABLE SETTINGS 
# *********************************

# The required results format
#----------------------------
GL_formats = ["VOTABLE"]	# All in votable
#GL_formats = ["VOTABLE", "VOTABLE-BINARY", "COMMA-SEPARATED"] #Variety


# The number of rows to return for each query
#--------------------------------------------
GL_top = "100"
#GL_top = "10000"
#GL_top = "100000"
#GL_top = "1000000"



# The number of simultaneous queries to launch
#---------------------------------------------
GL_numQueries = innerloop


# The DSA to use
#----------------
# NB - smallq has a small job queue, other dsa has a bigger one
#GL_ivorn="org.astrogrid.regtest.dsa/mysql-first-smallq/CatName_first/ceaApplication"
GL_ivorn="org.astrogrid.regtest.dsa/mysql-first/CatName_first/ceaApplication"

# The table to be queried
GL_table = "TabNameFirst_catalogue"

# *********************************
# END OF USER-CONFIGURABLE SETTINGS 
# **************************************************************************


#-------------------------------------------------------------------------------
class StatusChecker(Thread) :
	def __init__(self,runningIDs,startTimes,numQueries,outerloopcount) :
		Thread.__init__(self)
		self.runningIDs = runningIDs
		self.startTimes = startTimes
		self.numQueries = numQueries
		self.outerloopcount = outerloopcount

	def writeLogLine(self,outerloop, innerloop, startTime,endTime,status) :
		logging.info(
			"EXPORT ACTION [query] OUTERLOOP [%d] INNERLOOP [%d] TIMING [%f] STATUS [%s]\n",
			outerloop,innerloop,endTime-startTime,status);
	#

	def run(self) :
		alldone = 0
		processed = {}
		finishedIDs = {}
		failedIDs = {}
		unknownIDs = {}
		endTimes = {}
		
		# Monitor the status of the jobs 
		totlen = len(finishedIDs) + len(failedIDs) + len(unknownIDs)
		while (len(processed) < self.numQueries):
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
	
			time.sleep(1)
	
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
			self.runningIDs[i] = s.astrogrid.applications.submit(subtemplate)
			self.startTimes[i] = time.time()
			logging.debug("Launched job %d with id %s\n" % (i,self.runningIDs[i]))
	
	# END OF runAdqlTests
#-------------------------------------------------------------------------------

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")


#-----------------------------------------------------------------------------
# CONFIG SETTINGS
#-----------------------------------------------------------------------------

# The task document to use
GL_query = "TEMPLATES/selectAll_query.xml"
#GL_query = "TEMPLATES/selectSome_query.xml"
#-----------------------------------------------------------------------------

# Create the folder in VOSpace in which to put the results files
#---------------------------------------------------------------
GL_destFolder = "ivo://ktn@org.astrogrid.regtest/community#/stresstest_folder_smallq"

for looper in range(outerloop) :

	# Create the test folder.
	rootFolder =  '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':looper}
	logger.info("CREATING ROOT FOLDER %s", rootFolder)
	myspace.createFolder(ar, rootFolder)
	GL_destFolder = "ivo://%s@%s/community#/%s" % (astrouser,astroauth,rootFolder)
	print ("Dest folder is %s\n",GL_destFolder)

	GL_runningIDs = {}
	GL_startTimes = {}

	queryRunner = AdqlQueryRunner(GL_runningIDs,GL_startTimes,GL_ivorn,
		GL_table,GL_formats,GL_top,GL_destFolder,GL_numQueries,GL_query, looper)

	statusChecker = StatusChecker(GL_runningIDs,GL_startTimes,
		GL_numQueries,looper)

	queryRunner.start()
	statusChecker.start()

	queryRunner.join()
	statusChecker.join()

# Clean up
#if (testtidy):
if (1):
	myspace.deleteFile(ar, '%(root)s' % {'root':astroroot})


#if (testtidy):
#	myspace.logout(ar)

		
