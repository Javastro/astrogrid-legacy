#!/usr/bin/env python
#Noel Winstanley, Astrogrid, 2005
#Demonstration of how to use an ACR dialogue from a script, and manipulate the results.
import xmlrpclib
import sys
import os
import urllib2

#boilerplate - open connection to xmlrpc server
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
acr = xmlrpclib.Server(prefix + "xmlrpc")

#pop up file chooser dialogue.
# first parameter is the title of the dialog, 
# second parameter is True to enable the myspace tab of the resource chooser
# returnval is the uri of the chosen resource.
resource = acr.dialogs.resourceChooser.chooseResource("Select a file",True)
print "Chosen resource", resource 

# should return a myspace reference in the form ivo://...
# or a known URL format (file:/, http:/, ftp:/)
# or 'NULL' if the user pressed cancel.
if resource[0:4] == 'ivo:' : 
	#it's a myspace reference
	url = acr.astrogrid.myspace.getReadContentURL(resource)
	print "Data URL" , url
	# display contents of the url, using standard python libs.
	stream = urllib2.urlopen(url)
	print stream.read()
elif resource == 'NULL' :
	print "User pressed cancel"
else :			
	# some kind of url - can retreive it using standard python libs
	stream = urllib2.urlopen(resource)
	print stream.read()
	
