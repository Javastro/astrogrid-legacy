#!/usr/bin/env python
# perform a cone search and display results in a variety of ways.
#Usage: cone [--format=<ascii|votable|csv|html|plastic|browser...>] ra dec sr [coneURL | serviceId]
import xmlrpclib as x
import sys
import os
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")

service = 'ivo://nasa.heasarc/optical'
format = "ascii"



if len(sys.argv) >= 3:
    arg = 1
    if sys.argv[arg].startswith('--format='):
        format = sys.argv[arg].split('=')[1]
        arg = arg + 1
    ra = sys.argv[arg]
    arg = arg + 1
    
    dec = sys.argv[arg]
    arg = arg + 1
    
    sz = sys.argv[arg]
    arg = arg + 1
    
    if (arg < len(sys.argv)) :
        service = sys.argv[arg]
    
    query = s.ivoa.cone.constructQuery(service,ra,dec,sz)
    if format == 'plastic':
        try:
            myId = s.plastic.hub.registerNoCallBack("cone.py")
            # broadcast a message.
            s.plastic.hub.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
        finally:
            s.plastic.hub.unregister(myId) 
                       
    elif format == 'browser':
        import tempfile
        import urlparse
        import os.path
        tmpFile = tempfile.mktemp("html")
        # probablby a nicer way to do this
        tmpURL = urlparse.urlunsplit(['file','',os.path.abspath(tmpFile),'','']) 
        s.util.tables.convertFiles(query,"votable",tmpURL,"html")
        s.system.browser.openURL(tmpURL)
        
    
    else :
        doc =  s.util.tables.convertFromFile(query,"votable",format)
        print doc
    
else:
    print "Usage: cone [--format=<ascii|votable|csv|html|plastic|browser...>] ra dec sr [coneURL | serviceId]"
    