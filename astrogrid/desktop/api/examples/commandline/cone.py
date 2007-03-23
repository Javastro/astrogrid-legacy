#!/usr/bin/env python
# perform a cone search and display results in a variety of ways.
# run with --help for usage information.
import xmlrpclib 
import sys
import os
import optparse

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

#import service we're going to use from ar.
plastic = ar.plastic.hub
browser = ar.system.browser
tables = ar.util.tables
sesame = ar.cds.sesame

#default cone search service to query
service = 'ivo://nasa.heasarc/optical'
#default output format
format = "csv"

#build an option parser
parser = optparse.OptionParser(usage='%prog [options] <radius> {<ra> <dec>| <object-name>}',
                               description='Perform a cone search and return results')
parser.disable_interspersed_args()
parser.add_option('-f','--format', default=format, choices=['votable','csv','plastic','browser']
                  ,help='format to return results in: votable, csv, plastic, browser (default: %s)' % format)
parser.add_option('-s','--service',metavar="ID", default=service
                  ,help='RegistryID or URL endpoint of the Cone Service to query (default: %s)' % service)
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#parse the options
(opts,args) = parser.parse_args()

if opts.examples:
    print """
examples:
cone.py 0.1 m32 
        --- resolve position of m32, perform a 0.1 decimal degree search around this position, display results as csv
cone.py 0.1 83.822083 -5.391111
        --- perform a 0.1 decimal degree search around this position, return value as csv
cone.py -fvotable 0.1 83.822083 -5.391111
        --- do a search, return results as votable
cone.py --format=plastic 0.1 83.822083 -5.391111
        --- do a search, send results to a plastic viewer
cone.py -fbrowser 0.1 83.822083 -5.391111
        --- do a search, display result in the system browser       
cone.py --service=ivo://nasa.heasarc/abell 0.2 m54
        --- search 0.2 decimal degrees about m54 using the specified service
"""
    sys.exit()
#work out the position to search at
ra = 0.0
dec = 0.0
sz = 0.0
if len(args) < 2:
    parser.error("Not enough parameters provided")
elif len(args) == 2: #an object name had been given - try to resolve using sesame
    sz = args[0]
    object = args[1]
    sys.stderr.write("Resolving " + object )
    result = sesame.resolve(object)
    ra = result['ra']
    dec=result['dec']
    sys.stderr.write(" to %f %f\n" % (ra,dec))
else: # assume position has been provided on commandlne
    sz = args[0]
    ra = args[1]
    dec=  args[2]

# use AR to buuild the query URL       
query = ar.ivoa.cone.constructQuery(service,ra,dec,sz)

#decide what we're going to do with this query
if opts.format == 'plastic':
    try:
        myId = plastic.registerNoCallBack("cone.py")
        # broadcast a message. - tells the plastic apps to go get this table themselves.
        plastic.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
    finally:
        plastic.unregister(myId) 
                       
elif opts.format == 'browser':
        import tempfile
        import urlparse
        import os.path
        tmpFile = tempfile.mktemp("html")
        # probablby a nicer way to do this
        tmpURL = urlparse.urlunsplit(['file','',os.path.abspath(tmpFile),'','']) 
        #convert votable result to html, saving to a local file
        tables.convertFiles(query,"votable",tmpURL,"html")
        browser.openURL(tmpURL) # open this html in the browser

else :
        doc =  tables.convertFromFile(query,"votable",opts.format)
        print doc

    