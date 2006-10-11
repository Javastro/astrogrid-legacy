#!/usr/bin/env python
# send a file or URL (votable, fits, spectrum) to a plastic application. 
#Usage - plastic.py [-vot|-fits|-spec] <file|url>
import xmlrpclib as x
import sys
import os
import os.path
import urlparse

prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")


#now work out what the user intended.

#first- which mesage to send.

message = 'ivo://votech.org/votable/loadFromURL'
argc = 1
url = ""
messageFn = lambda u : [u,u]

if (sys.argv[argc] == '-vot'):
    #ok. it's the default.
   argc = argc+1
elif (sys.argv[argc] == '-fits'):
    message = "ivo://votech.org/fits/image/loadFromURL"
    messageFn = lambda u : [u]
    argc = argc+1    
elif (sys.argv[argc] == '-spec'):
    message= "ivo://votech.org/spectrum/loadFromURL"
    messageFn = lambda u: [u,u,{}]
    argc = argc+1


# see whether we've got a file or a url.
scheme = urlparse.urlparse(sys.argv[argc])[0]
if (scheme == 'http' or scheme == 'ftp' or scheme =='file'):
    url = sys.argv[argc]
else:
    # try it as a file..
    f = file(sys.argv[argc])
    url = urlparse.urlunsplit(["file","",os.path.abspath(f.name),"",""])


# first need to register.
try :
    myId = s.plastic.hub.registerNoCallBack("plastic.py")
    
    # broadcast a message.
    s.plastic.hub.requestAsynch(myId,message,messageFn(url))
finally:    
    #finally, unregester.
    s.plastic.hub.unregister(myId)
