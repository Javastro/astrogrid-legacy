#!/usr/bin/env python
# perform an adql query and display results in a variety of ways.
# run with --help for usage information.

#This is quite a straightforward implementation - but suitable for smaller queries.
# for larger results, or longer-running queries, the --myspace flag should be used
# to provide a myspace location in which to stage the results.

import xmlrpclib
import sys
import os
import optparse
import time
import urllib2

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

#take references to the acr services we'll be using
#think of these as 'imports' if you like.
apps = ar.astrogrid.applications
myspace = ar.astrogrid.myspace
plastic = ar.plastic.hub

#default cea app to query
service = 'ivo://mssl.ucl.ac.uk/solar_events_dsa/ceaApplication'
#default output format
format = "vot"
#dictionary mapping from short keywords to full CEA parameter values.
formatDict = {'vot':'VOTABLE','votbin':'VOTABLE-BINARY','csv':'COMMA-SEPARATED','plastic':'VOTABLE'}
# default query
query = "select top 100 * from yohkoh_flare_list as a"

#earlier releases of workbench / AR have a broken adql/s parser - so will only work with adql/x a the moment.
# for this version, uncomment the following statement.
#query ="""
#<Select xsi:type="selectType" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.ivoa.net/xml/ADQL/v1.0">
#    <?ag-adql-schema-version v1.0a1?>
#    <Restrict Top="100"/>
#    <SelectionList xsi:type="selectionListType">
#        <Item xsi:type="allSelectionItemType"/>
#    </SelectionList>
#    <From xsi:type="fromType">
#        <Table xsi:type="tableType" Alias="a" Name="yohkoh_flare_list"/>
#    </From>
#</Select>
#"""

#build an option parser.
parser = optparse.OptionParser(usage="%prog [options] <adql/s-query> | <query-file>",
                               description='''
Perform an adql query against a cea service. Results can be returned in a 
range of formats.
''')
parser.add_option('-f','--format', default=format, choices=['vot','votbin','csv','plastic']
                  ,help='format to return results in: vot, votbin, csv, plastic (default: %s)' % format)
parser.add_option('-s','--service',metavar="ID", default=service
                  ,help='RegistryID of the DSA to query (default: %s)' % service)
parser.add_option('-i','--inputfile',action='store_true', dest='fromFile', default=False
                  , help='read query from a file')
parser.add_option('-q','--query',action='store_false',dest='fromFile'
                  ,help='read query from commandline (default)')
parser.add_option('-m','--myspace',metavar='MYSPACE_LOCATION'
                , help='use a myspace file to stage the result (useful for large queries)')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#maybe add more options in here later

#parse the options
(opts,args) = parser.parse_args()

if opts.examples:
    print """
examples:
adql.py 
   ---- run with default parameters
adql.py -fvot "select top 100 * from yohkoh_flare_list as a" 
   --- return a votable
adql.py -format=plastic -q myQuery.adql
   --- load adql (/s or /x) from a file, display result to a plastic app
adql.py --service=ivo://org.astrogrid/IoA/FIRST/object-catalogue/ceaApplication -q aQuery.adql
   --- query against the FIRST object catalogue, load query from a file.
adql.py -myspace=/results/temp/adql1.vot -fplastic
   --- stage results in the myspace location '/results/temp/adql1.vot' within the user's homespace
       Display result in a plastic viewer
"""    
    sys.exit()
    
#on with the show. check we've got enough args left.
if len(args) == 0:
    sys.stderr.write( "No query provided - using default query\n" )
elif opts.fromFile:
    sys.stderr.write('Reading query from ' + args[0] + '\n')
    query = open(args[0]).read()
else:
    query = args[0]

#fill in the execution details. from the commandline options and args
#todo - should verify that the  service arg atually is an ADQL 
tool = apps.createTemplateStruct(opts.service,'ADQL')
tool['input']['Query']['value'] = query
tool['input']['Format']['value'] = formatDict[opts.format] 


#if a myspace location was provided, resolve it against user's homespace, and set
#up tool to write out to this location
if opts.myspace:
    sys.stderr.write('Will stage results to myspace\n')
    root = myspace.getHome()
    #make the full myspace path - adding a leading '/' if it's been missed.
    opts.myspace = root + ('/','')[opts.myspace[0] == '/'] + opts.myspace
    # check this file exists, and warn the user if so.
    if myspace.exists(opts.myspace):
        sys.stderr.write('Warning - myspace file %s exists\n' % opts.myspace )
    tool['output']['Result']['indirect']=True
    tool['output']['Result']['value'] = opts.myspace
    
# convert the structure to an xml document. could save it at this point
toolDoc = apps.convertStructToDocument(tool)
#print toolDoc

#toolDoc = open("/Users/noel/yohkoh.tool").read()
#print toolDoc
#submit the query to the server
execId = apps.submit(toolDoc)
# take a nap while things kick off. if we don't, we may not find it's there yet.
time.sleep(10)

#check on the progress of the query.
progress = apps.getExecutionInformation(execId)
# loop round until the query completes.  
while progress['status'] not in ['ERROR','COMPLETED','UNKNOWN'] :
    sys.stderr.write('.')
    time.sleep(10)
    progress = apps.getExecutionInformation(execId)

#query finished
if progress['status'] == "ERROR":
    sys.exit("Query ended in error")

#lovely. lets do something with the results
if opts.format == 'plastic' : #broadcast the file to plastic viewers
    myId = plastic.registerNoCallBack('adql.py')
    if opts.myspace: # we staged to myspace.
        url = myspace.getReadContentURL(opts.myspace)
        plastic.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[url,'adql-query'])
    else:
        #local result - fetch data
        results = apps.getResults(execId)
        data = results.values()[0] # we're only expecting a single result        
        plastic.requestAsynch(myId,'ivo://votech.org/votable/load'
                                          ,[data,'adql-query'])
    plastic.unregister(myId)    
else : #splurge data to screen
    if opts.myspace: # we staged to myspace - fetch results from ther.
        url = myspace.getReadContentURL(opts.myspace)
        resource = urllib2.urlopen(url)
        print resource.read() #a little dodgy with large files - should copy to screen bit by bit
    else: #local result
        results = apps.getResults(execId)
        print results.values()[0]

