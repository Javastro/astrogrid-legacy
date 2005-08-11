#!/usr/bin/env python
# sample of building and running an application
import xmlrpclib as x
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

#
# this example programatically creates and dispatches a cea application
# it's assumed that the user previously browsed the registry / list of 
# applications to select the app to call, find out it's parameter list, etc abd then codeed in the values.
# - the script can't do this by itself yet :)
#


# create a template tool structure
struct = s.astrogrid.applications.createTemplateStruct("ivo://uk.ac.cam.ast/INT-WFS/images/CEA-application","default")

print struct
#fill in parameters in struct
# - hopefully this should be nicer from any language than working with the xml.
	
inputs = struct['input']
inputs['POS']['value'] = "180.0,0.0"
inputs['SIZE']['value'] = "1.0"
inputs['FORMAT']['value'] = "image/fits"
	
# setup a new timestamped file in myspace.
home = s.astrogrid.myspace.getHome()
newFile = home + "/demo/int-wfs-results" + str(time.time()) +".vot"
# send the output to this file	  
struct['output']['IMAGES']['value'] = newFile
struct['output']['IMAGES']['indirect'] = True
print struct
	
# convert struct back to a tool document.
doc = s.astrogrid.applications.convertStructToDocument(struct)
print doc
#verify that the document is valid - i.e. we've supplied all the required parameters.
# will throw an exception on error.
s.astrogrid.applications.validate(doc)
 
#submit.
#work-around here - there's 2 app servers registered for this app, but need to specify 	
#this one, as the other seems to be misconfigured, and always fails.	
execId = s.astrogrid.applications.submitTo(doc,'ivo://uk.ac.cam.ast/INT-WFS/images/CEC')
print execId
	
# example of linking back into the gui - add the new application to the job monitor.
#add execId to the job monitor, and display the monitor.
s.userInterface.jobMonitor.addApplication("scripted application",execId)
s.userInterface.jobMonitor.displayApplicationTab()	
s.userInterface.jobMonitor.show()
# give it a refresh to find the status.
s.userInterface.jobMonitor.refresh()
	
# meanwhile, lets monitor the progress of the job programmatically 
# a busy-wait loop until it finishes.
execInfo = s.astrogrid.applications.getExecutionInformation(execId)		 
while execInfo['status'] == "RUNNING" :
	time.sleep(30)
	execInfo = s.astrogrid.applications.getExecutionInformation(execId)
	
if execInfo['status'] == "ERROR":
	print "Application ended in error"
else:
	#print results from myspace.
	print s.astrogrid.myspace.read(newFile)

## now we'll call the application again, this time returning results inline.
struct['output']['IMAGES']['indirect'] = False
struct['output']['IMAGES']['value'] = ""

# and we'll search a different area
inputs['POS']['value'] = "100.0,0.0"

#run it again.
doc = s.astrogrid.applications.convertStructToDocument(struct)
execId = s.astrogrid.applications.submitTo(doc,'ivo://uk.ac.cam.ast/INT-WFS/images/CEC')
# wait for the results
execInfo = s.astrogrid.applications.getExecutionInformation(execId)		 
while execInfo['status'] == "RUNNING" :
	time.sleep(30)
	execInfo = s.astrogrid.applications.getExecutionInformation(execId)
	
if execInfo['status'] == "ERROR":
	print "Application ended in error"
else:
	#print inline results
	print s.astrogrid.applications.getResults(execId)['IMAGES']	

# :)
