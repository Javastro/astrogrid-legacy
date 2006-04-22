#!/usr/bin/env python
# Noel Winstanley, John Taylor, Astrogrid, 2006
# workshop tutorial
###############################################################################
#
# This tutorial will lead you through the steps of developing a
# basic script that will:
# 1 Get a list of SIAP (Simple Image Access) services from the registry
# 2 Build a SIAP Query
# 3 Execute the query against a SIAP Server
# 4 Parse the resulting votable to extract image metadata
# 5 Display image metadata as a formatted webpage with links to the images
#
# Once the basic script has been developed, some or all of the following
# enhancements can be added
# Aa allow user to specify query position
# Ab accept object names, use webservice to resolve to ra,dec position
# B allow user to select SIAP service to query
# C Save selected images to myspace
# 
# For further resources, see Wiki page at http://wiki.astrogrid.org/bin/view/Astrogrid/AgTechWorkshopJan06#Making_applications_VO_aware
################################################################################


#imports for communicating with acr
import xmlrpclib 
import sys
import os
import os.path
import urlparse

#imports for processing xml 
from xml.dom.ext.reader import Sax2
from xml import xpath

#usual boilerplate to parse the configuration file.
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
endpoint = prefix + "xmlrpc"
print "Endpoint to connect to is", endpoint

#connect to the acr 
acr = xmlrpclib.Server(endpoint)

#-------------------------------------------------------------------------------
#STEP 0
#To get you started.  this calls an ACR function that just returns a canned ADQL
# query that when executed will select all active SIAP services in the registry
#CHECK THIS STEP RUNS CORRECTLY BEFORE PROCEEDING

adql = acr.ivoa.siap.getRegistryQuery()
print "Query for registry:", adql

#-------------------------------------------------------------------------------
#STEP 1 - get a list of siap services

# TODO: Query the registry using the adql query, to return a list of 
# resource information objects (these objects are pre-parsed versions of the registry
# metadata - which make for simpler scripting.
# each resource information object represents a siap service.

riList = acr.astrogrid.registry.adqlSearchRI(adql)

# TODO: replace the data-dump below with a a table of the unique id and name
# of each siap service. (add description, coverage information etc if you like too)

for r in riList:
	print r['id'], r['name']

#-------------------------------------------------------------------------------
#STEP 2 - build the SIAP query - a URL
	
#Some hard-coded definitions.

#The service we're going to call - (Mast Image Scrapbook)
targetService = riList[43] 

#registry key of the target service.
targetServiceId = targetService['id']

# position coords - again, hard-coded for now. (m42)
#later 
ra = 83.8220833
dec=-5.3911111
radius=0.1

# TODO: construct a query url using these parameters
queryURL = acr.ivoa.siap.constructQuery(targetServiceId,ra,dec,radius)
print queryURL


#-------------------------------------------------------------------------------
# STEP 3 - execute the query

# TODO: execute the query, saving result in local string
votableString = acr.ivoa.siap.getResults(queryURL)

print votableString

#-------------------------------------------------------------------------------
# STEP 4 - parsing the votable


# If you have a preferred votable parser, you're free to use this here.
# otherwise use the following.. using vanilla DOM and XPATH, but a bit nasty -
#so here's the solution...

#parse result string into a DOM Document.
votableDoc = Sax2.Reader().fromString(votableString)
el = votableDoc.documentElement

#find position of interesting columns, identified by UCD
# XPath Magic (apologies)
refIx = 1 + xpath.Evaluate('count(/VOTABLE/RESOURCE/TABLE/FIELD[@ucd="VOX:Image_AccessReference"]/preceding-sibling::FIELD)',el) 
print "position of image reference column: " , refIx

titleIx = 1 + xpath.Evaluate('count(/VOTABLE/RESOURCE/TABLE/FIELD[@ucd="VOX:Image_Title"]/preceding-sibling::FIELD)',el) 
print "position of title column: ", titleIx

formatIx = 1 + xpath.Evaluate('count(/VOTABLE/RESOURCE/TABLE/FIELD[@ucd="VOX:Image_Format"]/preceding-sibling::FIELD)',el)
print "position of format column: ", formatIx

# template for html file we're about to generate..
html = """
<html>
<body>
<h3>Images from %(name)s (%(id)s)</h3>
<table border="1">
<tr><th>Title</th><th>Format</th><th>&nbsp;</th><th>&nbsp;</th></tr>
""" % targetService

#TODO
#this loop iterates over each row of the votable - extract values from the columns of interest 
for r in xpath.Evaluate('/VOTABLE/RESOURCE/TABLE/DATA/TABLEDATA/TR',el):
	imgURL = xpath.Evaluate('string(TD[' + str(refIx) + '])',r)
	title = xpath.Evaluate('string(TD[' + str(titleIx) + '])',r)
	format = xpath.Evaluate('string(TD[' + str(formatIx) + '])',r)
	print imgURL
	print title
	print format
	# build up html table row.
	html = html + """
<tr><td>%(title)s</td>
    <td>%(format)s</td>
    <td><a href="%(url)s">view</a></td>
</tr>
""" % {'title':title, 'format':format, 'url':imgURL}

#finish off html
html = html + "</table></body></html>"
print html

#-------------------------------------------------------------------------------
#STEP 5 - write HTML to local file, and display in browser

#TODO - remove halt
raise Exception , "tutorial halted"

resultFile = file("results.html","w")
resultFile.write(html)
resultFile.close()
print resultFile

#convert this filename into a url
resultUrl = urlparse.urlunsplit(["file","",os.path.abspath(resultFile.name),"",""])
print resultUrl
#TODO - call acr to open resultUrl in browser


#######################################################################################
#Further work.
#
# Aa prompt user for query position - parse commandline args accessed from sys.argv, or 
#    prompt the user (use float(sys.stdin.readline())
# Ab accept an object name instead of position, and resolve to position using 
#    sesame web service
#   - call sesame service (in cds module) - get result in xml format
#   - use xpath as above to extract ra and dec fields
#
#---------------------------------------------------------------------------------
# B Allow user to select SIAP service to query
# two possible approaches 
#    a) could display service list to user, and then prompt for a number 
#       selection or somesuch using sys.stdin.readline()
#    b) display the registry chooser UI dialogue.
#	- more scalable, allows user to search for service by keyword
#	- necessary to restrict results to just siap services - uses 'where' clause
#	  of original adql query - "@xsi:type like '%SimpleImageAccess' and @status = 'active'"	
#---------------------------------------------------------------------------------
# C Save selected files to myspace
#   * can be done by adding another column 'save' to the output html table. This column
#   contains hyperlinks that call the myspace save method of the acr via it's HTML interface.
#---------------------------------------------------------------------------------
#
# More suggestions:
#   * Adapt script so that it queries a list of siap services
#   * Adapt script so that if query returns one or more results, all are saved into myspace
#   * Add something that consumes / processes the retrieved images - desktop tool or CEA application
#   * replace xpath-based parsing with call out to STILTS - to make parsing more robust
#





