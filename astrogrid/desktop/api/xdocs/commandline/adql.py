#!/usr/bin/env python
# perform an adql query and display results in a variety of ways.
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
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
registry = ar.ivoa.registry

#default cea app to query
defaultService = 'ivo://wfau.roe.ac.uk/ukidssDR1-dsa/wsa/ceaApplication'
#default output format
defaultFormat = "csv"
#dictionary mapping from short keywords to full CEA parameter values.
formatDict = {'vot':'VOTABLE','votbin':'VOTABLE-BINARY','csv':'COMMA-SEPARATED','plastic':'VOTABLE'}
# default query
query = "select * from Filter"

#build an option parser.
parser = optparse.OptionParser(usage="%prog [options] <adql-query> | <query-file>",
                               description='''
Query a database service. (DSA) Results can be returned in a range of formats.
''')
parser.add_option('-f','--format', default=defaultFormat, choices=['vot','votbin','csv','plastic']
                  ,help='format to return results in: vot, votbin, csv, plastic (default: %s)' % defaultFormat)
parser.add_option('-s','--service',metavar="ID", default=defaultService
                  ,help='RegistryID of the DSA to query (default: %s)' % defaultService)
parser.add_option('-i','--inputfile',action='store_true', dest='fromFile', default=False
                  , help='read query from a file')
parser.add_option('-q','--query',action='store_false',dest='fromFile'
                  ,help='read query from commandline (default)')
parser.add_option('-m','--myspace',metavar='MYSPACE_LOCATION'
                , help='use a myspace file to stage the result (useful for large queries)')
parser.add_option('-l','--list',action='store_true',default=False
                  ,help='list all known queryable database services, and exit')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#maybe add more options in here later

#parse the options
(opts,args) = parser.parse_args()

if opts.examples:
    print """
adql.py "select * from Filter"
   : run a query against default database, return results as CSV

adql.py -fvot "select * from Filter" 
   : run a query, return a votable
   
adql.py -i query.adql
   : load query from a file,
   
adql.py --format=plastic -i query.adql   
   : send results to a running plastic application (e.g. Topcat)

adql.py --list
   : list the names and identifiers of all queryable database services
   
adql.py --service=ivo://wfau.roe.ac.uk/twomass-dsa/wsa "select top 10 * from twomass_psc"
   : query against 2MASS

adql.py -myspace=/results/temp/adql1.vot -i query.adql
   : stage results at '/results/temp/adql1.vot' within the user's myspace
       
"""    
    sys.exit()
elif opts.list:
    #query to produce index of catalog services which provide a cea capability
    xq = """
<ul>
{
for $r in //vor:Resource[not (@status='inactive' or @status='deleted')
   and @xsi:type &= '*CatalogService' 
   and capability/@standardID="ivo://org.astrogrid/std/CEA/v1.0"]
order by $r/title
return <li>{$r/title/text()} : {$r/identifier/text()}</li>
}
</ul>
    """
    # do the query
    s = registry.xquerySearchXML(xq)
    #extract info from xml
    import re
    for l in re.finditer('<li>(.*)</li>',s):
        print l.group(1)
    sys.exit()    
    
#on with the show. check we've got enough args left.
if len(args) == 0:
    sys.stderr.write( "No query provided - using default query - %s\n" % duery )
elif opts.fromFile:
    sys.stderr.write('Reading query from ' + args[0] + '\n')
    query = open(args[0]).read()
else:
    query = args[0]

#check what kind of resource opts.service is.
#and if it's not a cea application, see if we can find the related cea app.
if opts.service != defaultService:
    res = registry.getResource(opts.service)
    if not (res['type'].endswith("CeaApplication")) :
        #guess it's the server - try to find the correct relationshop metadata
        relationships = res['content']['relationships']
        for r in relationships:
            if r['relationshipType'] == 'service-for':
                opts.service =  r['relatedResources'][0]['value']

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
    results = apps.getResults(execId)
    sys.exit(results['cea-error'])


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

