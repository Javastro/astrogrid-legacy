#!/usr/bin/env python
# Run a bunch of parallel conesearches

import xmlrpclib as x
import sys
import os
import os.path
import urlparse
import time
from optparse import OptionParser
from astrogrid import acr

#-----------------------------------------------------------------------------
# CONFIG SETTINGS
#-----------------------------------------------------------------------------
# The ivorn of the cea cone service
# ROE
ivorn = "agtest.roe.ac.uk/srif112-mysql-first/cone"

# The myspace destination for the results
# ROE
destFolder = "ivo://agtest.roe.ac.uk/KonaAndrews#cone_test_folder"
dest = "ivo://agtest.roe.ac.uk/KonaAndrews#cone_test_folder/new_fileSUBS_NUM.vot"

# The number of simultaneous queries to launch
numQueries = 1

# The task document to use
#query = "TEMPLATES/selectAll_query.xml"
query = "TEMPLATES/coneQuery.xml"
#-----------------------------------------------------------------------------

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

if (checkonly == 0) :
	# Make lists for monitoring results
	execIDs = []
	execStatus = []

	# Choose a return format type
	# Open the query template
	f = open(query,'r')
	template = f.read()
	template = template.replace("SUBS_IVORN",ivorn)
	template = template.replace("SUBS_DEST",dest)

	# Submit the jobs
	for i in range(numQueries) :
		subtemplate = template.replace("SUBS_NUM","%d"%(i))
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
	print "\nSummary:  Num queries %d\n" % (numQueries)
	print "              COMPLETED %d    ERROR %d    OTHER %d\n" % (completed,error,other)

# Check filesizes
badCount = 0
goodCount = 0
fileStatus = []
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
