#!/usr/bin/env python
#Demonstration of how to use an ACR dialogue from a script, and manipulate the results.
import xmlrpclib as x
import sys
import os
import urllib2

#boilerplate - open connection to xmlrpc server
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")
#
#pop up file chooser dialogue.
# first parameter is the title of the dialog, 
# second parameter is True to enable the myspace tab of the resource chooser
# (there should probably be an option to disable the local-file tab of the chooser too).
resource = s.dialogs.resourceChooser.chooseResource("Select a file",True)
print resource 
# should return a myspace reference in the form ivo://...
# or a known URL format
# or 'NULL' if the user pressed cancel.
if resource[0:4] == 'ivo:' :
	# get the data url for this myspace resource
	url = s.astrogrid.myspace.getReadContentURL(resource)
	print url
	# display contents of the url, using standard python libs.
	stream = urllib2.urlopen(url)
	print stream.read()
elif resource == 'NULL' :
	print "User pressed cancel"
else :			
	# some kind of url - can retreive it directly.
	stream = urllib2.urlopen(resource)
	print stream.read()
	
