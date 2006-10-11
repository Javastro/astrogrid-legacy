#!/usr/bin/env python
# perform a siap search and display results in a  variety of ways, download imagses to local directoy.
# could go futher - add option to specify storage location, fire images into plastic, myspace, etc, etc.
#Usage: siap  [--format=<ascii|votable|csv|html|plastic|browser...>] [--download=<'all|n>] ra dec sr [coneURL | serviceId]
import xmlrpclib as x
import sys
import os
import urlparse
import os.path

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

service = 'ivo://adil.ncsa/targeted/SIA'
format = "ascii"
download = 0


if len(sys.argv) >= 3:
    arg = 1
    if sys.argv[arg].startswith('--format='):
        format = sys.argv[arg].split('=')[1]
        arg = arg + 1
    
    #note - parameter parsing isn't the best - is order dependent.    
    if sys.argv[arg].startswith('--download='):
        download = sys.argv[arg].split('=')[1]
        arg = arg + 1
        
    ra = sys.argv[arg]
    arg = arg + 1
    
    dec = sys.argv[arg]
    arg = arg + 1
    
    sz = sys.argv[arg]
    arg = arg + 1
    
    if (arg < len(sys.argv)) :
        service = sys.argv[arg]
    
    query = s.ivoa.siap.constructQuery(service,ra,dec,sz)
    if format == 'plastic':
        try:
            myId = s.plastic.hub.registerNoCallBack("cone.py")
            # broadcast a message.
            s.plastic.hub.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
        finally:
            s.plastic.hub.unregister(myId) 
                       
    elif format == 'browser':
        import tempfile
        tmpFile = tempfile.mktemp("html")
        # probablby a nicer way to do this
        tmpURL = urlparse.urlunsplit(['file','',os.path.abspath(tmpFile),'','']) 
        s.util.tables.convertFiles(query,"votable",tmpURL,"html")
        s.system.browser.openURL(tmpURL)
        
    
    else :
        doc =  s.util.tables.convertFromFile(query,"votable",format)
        print doc
    
    # download images
    currentDirURL = urlparse.urlunsplit(['file','',os.getcwd(),'',''])
    if download == 'all':
        s.ivoa.siap.saveDatasets(query,currentDirURL)
        print "Downloaded all images to " + currentDirURL
    elif int(download) > 0:
        s.ivoa.siap.saveDatasetsSubset(query,currentDirURL,range(int(download)))
        print "Downloaded " + download + " images to " + currentDirURL 
else:
    print "Usage: siap [--format=<ascii|votable|csv|html|plastic|browser...>] [--download=<'all'|n>] ra dec sr [coneURL | serviceId]"
    