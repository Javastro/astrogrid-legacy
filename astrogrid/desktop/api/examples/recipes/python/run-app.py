#!/usr/bin/env python
#Noel Winstanley, Astrogrid, 2005
# sample of building parameters and running a remote CEA application
import xmlrpclib
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
acr = xmlrpclib.Server(prefix + "xmlrpc")

#
# this example programatically creates and dispatches a cea application
# it's assumed that the user previously browsed the registry / list of 
# applications to select the app to call, find out it's parameter list, etc and then selected the values to use
# - the script can't do this by itself yet :)
#

#get a handle on some services
apps = acr.astrogrid.applications
myspace = acr.astrogrid.myspace

# create a template tool structure for an application
struct = apps.createTemplateStruct("ivo://uk.ac.cam.ast/INT-WFS/images/CEA-application","default")
print "template tool structure",struct

#fill input  parameters in struct
inputs = struct['input']
inputs['POS']['value'] = "180.0,0.0"
inputs['SIZE']['value'] = "1.0"
inputs['FORMAT']['value'] = "image/fits"
	
# setup a new timestamped file in myspace.
home = myspace.getHome()
newFile = home + "/demo/int-wfs-results" + str(time.time()) +".vot"
# set the output parameter to be this file	  
struct['output']['IMAGES']['value'] = newFile
struct['output']['IMAGES']['indirect'] = True

	
# convert struct back to a tool document - this gives a persistent representation of the 
# remote tool invocation, which can be saved, added into a workflow, etc.
doc = apps.convertStructToDocument(struct)
print "Tool Document",doc

#verify that the document is valid - i.e. we've supplied all the required parameters.
# will throw an exception on error.
apps.validate(doc)
 
#submit.
#work-around here - there's 2 app servers registered for this app, but need to specify 	
#this one, as the other seems to be misconfigured, and always fails.	
# if this wasn't the case, could get away with just calling apps.submit(doc)
execId = apps.submitTo(doc,'ivo://uk.ac.cam.ast/INT-WFS/images/CEC')
print "New execution id",execId

# display the lookout gui, so we can see what's going on.
acr.userInterface.lookout.show()
	
# monitor the progress of the job programmatically 
# a busy-wait loop until it finishes - sadly can't get callback notifications over the xmlrpc access method.
# using the rmi method, can just register a istener, instead of polling.

execInfo = apps.getExecutionInformation(execId)		 
while execInfo['status'] in[ "INITIALIZING","PENDING","RUNNING"] :
	acr.userInterface.lookout.refresh()
	time.sleep(30)
	print "Checking again"
	execInfo = apps.getExecutionInformation(execId)
	
if execInfo['status'] == "ERROR":
	print "Application ended in error"
else:
	#print results from myspace.
	print myspace.read(newFile)

#### 

## now we'll call the application again, this time returning results inline.
struct['output']['IMAGES']['indirect'] = False
struct['output']['IMAGES']['value'] = ""

# and we'll search a different area
inputs['POS']['value'] = "100.0,0.0"

#run it again.
doc = apps.convertStructToDocument(struct)
execId = apps.submitTo(doc,'ivo://uk.ac.cam.ast/INT-WFS/images/CEC')
acr.userInterface.lookout.show()
# wait for the results
execInfo = apps.getExecutionInformation(execId)		 
while execInfo['status'] in[ "INITIALIZING","PENDING","RUNNING"]  :
	acr.userInterface.lookout.refresh()
	time.sleep(30)
	execInfo = apps.getExecutionInformation(execId)
	
if execInfo['status'] == "ERROR":
	print "Application ended in error"
else:
	#print inline results
	print apps.getResults(execId)['IMAGES']	

# :)
