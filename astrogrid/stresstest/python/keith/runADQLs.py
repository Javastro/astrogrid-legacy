#!/usr/bin/env python
# Run a bunch of parallel ADQL queries for stress-testing a DSA

import xmlrpclib as x
import sys
import os
import os.path
import urlparse
import time
from optparse import OptionParser
from astrogrid import acr

# **************************************************************************
# KEITH: USER-CONFIGURABLE SETTINGS 

# The required results format
formats = ["VOTABLE"]
#formats = ["VOTABLE", "VOTABLE-BINARY", "COMMA-SEPARATED"]

# The number of rows to return 
#top = "100"
top = "10000"
#top = "100000"
#top = "1000000"

# The folder in VOSpace in which to put the results files
destFolder = "ivo://ktn@org.astrogrid.regtest/community#/stresstest_folder"

# The number of simultaneous queries to launch
#numQueries = 1
numQueries = 10
#numQueries = 50
#numQueries = 100
# **************************************************************************


#-------------------------------------------------------------------------------
def runQuickAdqlTests(ivorn,table,formats,top,destFolder,numQueries,query, checkonly) :
	print("IVORN IS " + ivorn)
	dest = destFolder + "/new_fileSUBS_NUM.vot"
	print("dest IS " + dest)
	if (checkonly == 0) :
		# Make lists for monitoring results
		execIDs = []
		execStatus = []

		
		# Choose a return format type
		# Open the query template
		f = open(query,'r')
		template = f.read()
		template = template.replace("SUBS_IVORN",ivorn)
		template = template.replace("SUBS_TABLE",table)
		template = template.replace("SUBS_TOP",top)
		template = template.replace("SUBS_DEST",dest)

		# Submit the jobs
		for i in range(numQueries) :
			#index = i % 3 # Alternate formats
			index = 0	# STICK TO VOTABLE
			#index = 1	# STICK TO VOTABLE-BIN
			#index = 2	# STICK TO CSV
			subtemplate = template.replace("SUBS_NUM","%d"%(i))
			subtemplate = subtemplate.replace("SUBS_FORMAT","%s"%(formats[index]))
			execIDs.append(s.astrogrid.applications.submit(subtemplate))
			execStatus.append("PENDING")
		#	execInfo = s.astrogrid.applications.getExecutionInformation(execId)		 
			print ("Launched job %d with id %s\n" % (i,execIDs[i]))
#

# THE TEST FUNCTION 
def runAdqlTests(ivorn,table,formats,top,destFolder,numQueries,query, checkonly) :
	dest = destFolder + "/new_fileSUBS_NUM.vot"
	if (checkonly == 0) :
		# Make lists for monitoring results
		execIDs = []
		execStatus = []

		
		# Choose a return format type
		# Open the query template
		f = open(query,'r')
		template = f.read()
		template = template.replace("SUBS_IVORN",ivorn)
		template = template.replace("SUBS_TABLE",table)
		template = template.replace("SUBS_TOP",top)
		template = template.replace("SUBS_DEST",dest)

		# Submit the jobs
		for i in range(numQueries) :
			#index = i % 3 # Alternate formats
			index = 0	# STICK TO VOTABLE
			#index = 1	# STICK TO VOTABLE-BIN
			#index = 2	# STICK TO CSV
			subtemplate = template.replace("SUBS_NUM","%d"%(i))
			subtemplate = subtemplate.replace("SUBS_FORMAT","%s"%(formats[index]))
			execIDs.append(s.astrogrid.applications.submit(subtemplate))
			execStatus.append("PENDING")
		#	execInfo = s.astrogrid.applications.getExecutionInformation(execId)		 
			print ("Launched job %d with id %s\n" % (i,execIDs[i]))
			#time.sleep(30)

		# Pause a moment
		print "Pausing before checking job status..."
		time.sleep(10)

		# Monitor the status of the jobs 
		alldone = 0
		while (alldone == 0) :
			alldone = 1
			for i in range(numQueries) :
				if ((execStatus[i] != "COMPLETED") and (execStatus[i] != "ERROR")) :
					try :
						execInfo = s.astrogrid.applications.getExecutionInformation(execIDs[i])	
						execStatus[i] = execInfo['status']
					except Exception, e:
						execStatus[i] = "UNKNOWN"
						print "Error getting job status: ",str(e)
					if ((execStatus[i] == "RUNNING") or (execStatus[i] == "PENDING") or (execStatus[i] == "UNKNOWN")) :
						#print ("Still %s : job %s" % (execStatus[i], execIDs[i]))
						alldone = 0
			time.sleep(5)

		# Print a small summary
		completed = 0
		error = 0
		other = 0
		for i in range(numQueries) :
			print "%s : %s" %(execStatus[i],execIDs[i])
			if (execStatus[i] == "COMPLETED") :
				completed = completed + 1
			elif (execStatus[i] == "ERROR") :
				error = error + 1
			else :
				other = other + 1
		print "\nSummary:  Num queries %d    Num rows %s \n" % (numQueries,top)
		print "              COMPLETED %d    ERROR %d    OTHER %d\n" % (completed,error,other)

	# Check filesizes
	badCount = 0
	goodCount = 0
	fileStatus = []
	print "TRYING TO LIST IVORNS FOR ",destFolder
	contents = acr.astrogrid.myspace.listIvorns(destFolder)
	for i in range(numQueries) :
		fileStatus.append("new_fileSUBS_NUM.vot")
		fileStatus[i] = fileStatus[i].replace("SUBS_NUM","%d"%(i))
	for f in contents:
		info = acr.astrogrid.myspace.getNodeInformation(f)
		filename = info['name']
		try :
			filesize = info['attributes']['org.astrogrid.filestore.content.size']
			print "File ",filename, "has size ",filesize
			# Flag up this file as found
			if (filesize == "0") :
				badCount = badCount + 1
				for i in range(numQueries) :
					if (fileStatus[i] == filename) :
						fileStatus[i] = fileStatus[i] + "is EMPTY"
			else :
				goodCount = goodCount + 1
				for i in range(numQueries) :
					if (fileStatus[i] == filename) :
						fileStatus[i] = ""
		except Exception, e :
			print "[Can't get attributes for file ",filename,"]"
			badCount = badCount + 1
			for i in range(numQueries) :
				if (fileStatus[i] == filename) :
					fileStatus[i] = fileStatus[i] + "is BAD"

	print "\nGOOD TRANSFERS: ",goodCount,"    BAD TRANSFERS: ",badCount,"    MISSING FILES: ",(numQueries - (goodCount + badCount))

	for i in range(numQueries) :
		if ("is BAD" in fileStatus[i]) :
			print " -- File ",fileStatus[i]
		elif ("is EMPTY" in fileStatus[i]) :
			print " -- File ",fileStatus[i]
		elif (fileStatus[i] != "") :
			print " -- File ",fileStatus[i]," is MISSING"
# END OF runAdqlTests
#-------------------------------------------------------------------------------

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

checkonly = 0
if (len(sys.argv) == 2) :
	if (sys.argv[1] == "check") :
		checkonly = 1
	else :
		print "Bad usage: ",sys.argv[1]

elif (len(sys.argv) != 1) :
	print "Bad usage"
	sys.exit(1)


#-----------------------------------------------------------------------------
# CONFIG SETTINGS
#-----------------------------------------------------------------------------
# The ivorn of the cea service
# CAM TEST NETWORK
ivorn="org.astrogrid.regtest.dsa/first-dsa/wsa/ceaApplication"

# The table to be queried
#table = "TabName_catalogue"
table = "wsa.firstSource"


# The task document to use
query = "TEMPLATES/selectAll_query.xml"
#query = "TEMPLATES/selectSome_query.xml"
#-----------------------------------------------------------------------------

runQuickAdqlTests(ivorn,table,formats,top,destFolder,numQueries,query,checkonly)
