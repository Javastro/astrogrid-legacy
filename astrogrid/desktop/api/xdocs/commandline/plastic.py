#!/usr/bin/env python
# send a file or URL (votable, fits, spectrum) to a plastic application. 
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.
# todo add in support for myspace.
import xmlrpclib 
import sys
import os
import os.path
import optparse
import urlparse
#verify we're running a suitable version of python
if not (sys.version_info[0] > 2 or sys.version_info[1] >= 5):
    print """This script runs best on python 2.5 or above. 
    You're running """ + sys.version
    
#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")
#take a reference to the ar service we're going to use
plastic = ar.plastic.hub

#now work out what the user intended.
parser = optparse.OptionParser(usage='%prog [options] <file | url>',
                               description="Send a file or URL containing votable, fits, or spectrum data to a PLASTIC application (such as Topcat or Aladin) ")
parser.add_option('-v','--vot',action='store_true',help='Treat as a votable (default)')
parser.add_option('-s','--spec',action='store_true',help='Treat as a spectrum')
parser.add_option('-f','--fits',action='store_true',help='Treat as a fits image')
parser.add_option('-e','--examples',action='store_true',default=False
                  ,help='display some examples of use and exit')

(opts,args) = parser.parse_args()

if opts.examples:
    parser.exit(0,"""
examples:
plastic.py mytable.vot
    : send the file 'mytable.vot' to any plastic table viewer
    
plastic.py -s downloads/spectra.fits
    : send the file 'downloads/spectra.fits' to any plastic specra viewers
    
plastic.py -i http://someServer/foo/images.fits
    : send this http url to any plastic image viewers
""")
    
#first- which mesage to send.
#defaults - for loat votable.
message = 'ivo://votech.org/votable/loadFromURL'
url = ""
messageFn = lambda u : [u,u]

if (opts.vot):
    #ok. it's the default.
   pass
elif (opts.fits):
    message = "ivo://votech.org/fits/image/loadFromURL"
    messageFn = lambda u : [u]   
elif (opts.spec):
    message= "ivo://votech.org/spectrum/loadFromURL"
    messageFn = lambda u: [u,u,{}]

#now - see if we've got a file or a url.
if len(args) == 0:
    parser.print_help()    
    parser.error("No file or URL supplied")
    
scheme = urlparse.urlparse(args[0])[0]
if (scheme == 'http' or scheme == 'ftp' or scheme =='file'):
    url = args[0]
else:
    # try it as a file..
    f = file(args[0])
    url = urlparse.urlunsplit(["file","",os.path.abspath(f.name),"",""])


# first need to register.
try :
    myId = plastic.registerNoCallBack("plastic.py")
    # broadcast a message.
    plastic.requestAsynch(myId,message,messageFn(url))
finally:    
    #finally, unregester.
    plastic.unregister(myId)
