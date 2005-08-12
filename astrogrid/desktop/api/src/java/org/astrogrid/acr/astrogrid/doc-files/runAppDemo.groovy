#!/bin/env groovy
import org.astrogrid.acr.Finder
import java.net.URI

#
# this example programatically creates and dispatches a cea application
# it's assumed that the user previously browsed the registry / list of 
# applications to select the app to call, find out it's parameter list, etc abd then codeed in the values.
# - the script can't do this by itself yet :)
#

#connect to the acr via the rmi interface - requires the acr-interface jar on groovy's classpath
finder = new Finder()
acrInst = finder.find()

# create a template tool structure
apps = acrInst.getService("astrogrid.applications")
struct = apps.createTemplateStruct(new URI("ivo://uk.ac.cam.ast/INT-WFS/images/CEA-application"),"default")

println struct
#fill in parameters in struct
# - hopefully this should be nicer from any language than working with the xml.
	
inputs = struct['input']
inputs['POS']['value'] = "180.0,0.0"
inputs['SIZE']['value'] = "1.0"
inputs['FORMAT']['value'] = "image/fits"
	
# setup a new timestamped file in myspace.
myspace = acrInst.getService("astrogrid.myspace")
home = myspace.getHome()
newFile = home.toString() + "/demo/int-wfs-results" + System.currentTimeMillis() +".vot"
# send the output to this file	  
struct['output']['IMAGES']['value'] = newFile
struct['output']['IMAGES']['indirect'] = true
println struct
	
# convert struct back to a tool document.
doc = apps.convertStructToDocument(struct)
#verify that the document is valid - i.e. we've supplied all the required parameters.
# will throw an exception on error.
apps.validate(doc)
 
#submit.
#work-around here - there's 2 app servers registered for this app, but need to specify 	
#this one, as the other seems to be misconfigured, and always fails.	
execId = apps.submitTo(doc,new URI("ivo://uk.ac.cam.ast/INT-WFS/images/CEC"))
println execId
	
# example of linking back into the gui - add the new application to the job monitor.
#add execId to the job monitor, and display the monitor.
jm = acrInst.getService("userInterface.jobMonitor")		
jm.addApplication("scripted application",execId)
jm.displayApplicationTab()	
jm.show()
# give it a refresh to find the status.
jm.refresh()
	
# meanwhile, lets monitor the progress of the job programmatically 
# a busy-wait loop until it finishes.	 
do {
	Thread.sleep(15 * 1000)	
	execInfo = apps.getExecutionInformation(execId)
} while (execInfo['status'] == "RUNNING")
	
if (execInfo['status'] == "ERROR") {
	println "Application ended in error"
} else {
	#print results from myspace.
	println myspace.read(new URI(newFile))
}

## now we'll call the application again, this time returning results inline.
struct['output']['IMAGES']['indirect'] = false
struct['output']['IMAGES']['value'] = ""

# and we'll search a different area
inputs['POS']['value'] = "100.0,0.0"

#run it again.
doc = apps.convertStructToDocument(struct)
execId = apps.submitTo(doc,new URI("ivo://uk.ac.cam.ast/INT-WFS/images/CEC"))
# wait for the results
execInfo = apps.getExecutionInformation(execId)		 
do {
	Thread.sleep(15 * 1000)	
	execInfo = apps.getExecutionInformation(execId)
} while (execInfo['status'] == "RUNNING")
			
if (execInfo['status'] == "ERROR") {
	println "Application ended in error"
} else {
	#print inline results
	print apps.getResults(execId)['IMAGES']	
}

