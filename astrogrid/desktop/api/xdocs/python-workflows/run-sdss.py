#!/usr/bin/env python
# sample of building and submitting an invocation document for a CEA remote application
import xmlrpclib as x
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

# create a template tool structure
print "Creating template from SDSS-DR3 application: "
print "  ivo://uk.ac.le.star/SDSS-DR3/images/CEA-application"


ra=[249.0]
dec=[42.]

for i in range(len(ra)):
        struct = s.astrogrid.applications.createTemplateStruct("ivo://uk.ac.le.star/SDSS-DR3/images/CEA-application","default")

        inputs = struct['input']
        inputs['POS']['value'] = "%s,%s" % (ra[i], dec[i])
        inputs['SIZE']['value'] = "0.017"
        inputs['FORMAT']['value'] = "ALL"
        inputs['BANDPASS']['value'] = "*"

        # setup a new timestamped file in myspace.
        home = s.astrogrid.myspace.getHome()
        newFile = home + "/aoctest/sdss-results" + str(time.time()) +".vot"

        # send the output to this file    
        struct['output']['IMAGES']['value'] = newFile
        struct['output']['IMAGES']['indirect'] = True
        
        print ""
        print "     Interface: ", struct['interface']
        print "     Inputs: "
        print "          POS:      ", inputs['POS']
        print "          SIZE:     ", inputs['SIZE']
        print "          FORMAT:   ", inputs['FORMAT']
        print "          BANDPASS: ", inputs['BANDPASS']
        print "     Output:"
        print "          IMAGES:   ", struct['output']['IMAGES']['value']

        # convert struct back to a tool document.
        doc = s.astrogrid.applications.convertStructToDocument(struct)

        #verify that the document is valid - i.e. we've supplied all the required parameters.
        # will throw an exception on error.
        s.astrogrid.applications.validate(doc)

        execId = s.astrogrid.applications.submitTo(doc,'ivo://uk.ac.le.star/SIAP-CEC-2')
        print execId


        #  monitor the progress of the job programmatically 
        # a busy-wait loop until it finishes.
        execInfo = s.astrogrid.applications.getExecutionInformation(execId)              
        while execInfo['status'] not in ['ERROR','COMPLETED'] :
				print '.'
				time.sleep(30)
				execInfo = s.astrogrid.applications.getExecutionInformation(execId)

        if execInfo['status'] == "ERROR":
                print "Application ended in error"
        else:
                #print results from myspace.
                print s.astrogrid.myspace.read(newFile)


