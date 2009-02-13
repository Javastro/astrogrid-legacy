#!/usr/bin/env python
# perform a siap search and display results in a  variety of ways, download imagses to local directoy.
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.
import xmlrpclib 
import sys
import os
import urlparse
import os.path
import optparse
#verify we're running a suitable version of python
if not (sys.version_info[0] > 2 or sys.version_info[1] >= 5):
    print """This script runs best on python 2.5 or above. 
    You're running """ + sys.version

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
siap = ar.ivoa.siap

#default siap service to query
defaultService = 'ivo://nasa.heasarc/skyview/rass'
#default output format
defaultFormat = "csv"
#number of images to download
download = 0

parser = optparse.OptionParser(usage='%prog [options] <radius> {<ra> <dec>| <object-name>}',
                               description="Perform an image search and display results, optionally download images")
parser.disable_interspersed_args()
parser.add_option('-f','--format', default=defaultFormat, choices=['votable','csv','plastic','browser']
                  ,help='format to return results in:  votable, csv, plastic, browser (default: %default)')
parser.add_option('-s','--service',metavar="ID", default=defaultService
                  ,help='RegistryID or URL the Image Service to query (default: %default)')
parser.add_option('-d','--download',metavar="n|ALL"
                  ,help="number of images to download (default: %default)")
parser.add_option('-b','--browse', action='store_true',default=False
                  , help='browse all known SIAP services in a GUI, and exit')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#parse the options
(opts,args) = parser.parse_args()

if opts.examples:
    parser.exit(0, """siap.py 0.1 m32 
        : search of radius 0.1 decimal degree around m32, display results as csv
        
siap.py --download=all 0.1 m32 
        : do same search, and save all images to local directory.
        
siap.py -d4 0.1 m32 
        : same search, save first 4 images to local directory

siap.py --service=ivo://adil.ncsa/targeted/SIA -d3 0.2 m54
        : query a specific service by giving it's registry resource ID.  

siap.py --service=http://some.siap.service/endpoint 0.1 45.6 -10.23
        : search a specific service by giving it's endpoint
        
siap.py -fvotable 0.1 83.822083 -5.391111
        : do a search, return results as votable
            
siap.py --format=plastic 0.1 ngc123
        : send results to a running plastic application (e.g. Topcat)
    
siap.py --format=browser 0.1 83.822083 -5.391111
        : do a search, display result in the system webbrowser
""")
elif opts.browse:
    rs = ar.dialogs.registryGoogle.selectResourcesQueryFilter(
                "All SIAP Services",True,siap.getRegistryQuery())
    if len(rs) > 0:
        print "You selected"
        for r in rs:
            print r['title']
            print " ", r['id']
    sys.exit()    
#work out the position to search at
ra = 0.0
dec = 0.0
sz = 0.0
if len(args) == 0:
    parser.print_help()
    parser.error("No parameters provided")
elif len(args) < 2:
    parser.print_help()
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

#use AR to build the query URL
query = siap.constructQuery(opts.service,ra,dec,sz)

#decide what to do with this query    
if opts.format == 'plastic':
    try:
        myId = plastic.registerNoCallBack("siap.py")
        # broadcast a message.
        plastic.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
    finally:
        plastic.unregister(myId) 
                       
elif opts.format == 'browser':
        import tempfile
        tmpFile = tempfile.mktemp("html")
        # probablby a nicer way to do this
        tmpURL = urlparse.urlunsplit(['file','',os.path.abspath(tmpFile),'','']) 
        #convert votable result to html, saving to a local file        
        tables.convertFiles(query,"votable",tmpURL,"html")
        browser.openURL(tmpURL) # open this html in the browser
        
else :
        print tables.convertFromFile(query,"votable",opts.format)
    
# download images
if opts.download: #we'll download some images
    currentDirURL = urlparse.urlunsplit(['file','',os.getcwd(),'',''])
    if opts.download == 'all':
        sys.stderr.write("Downloading all images to " + currentDirURL)
        siap.saveDatasets(query,currentDirURL)
    elif int(opts.download) > 0:
        sys.stderr.write("Downloading " + opts.download + " images to " + currentDirURL)
        siap.saveDatasetsSubset(query,currentDirURL,range(int(opts.download)))
