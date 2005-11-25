#!/usr/bin/env python
""" 
volazarus
Sends the contents of the current directory to your home
directory on MySpace.
Needs astrogrid.py
todo: consider using os.walk 
   Author: jdt@roe.ac.uk
"""
from astrogrid import acr
import os
import urllib
import urlparse

def copyFolder(localFolder, remoteFolder):
    # should check that it is a folder
    folderInfo = acr.astrogrid.myspace.getNodeInformation(remoteFolder)
    if not folderInfo['folder'] or not os.path.isdir(localFolder):
        raise RuntimeError, "This function should be called with folders as args"

    contents = os.listdir(localFolder)

    for f in contents:

        if os.path.isfile(f):
            
            try:
                remoteFile = acr.astrogrid.myspace.createChildFile(remoteFolder,f)
                print "Copying " + f + " to " + remoteFile
                contentsURL = urlparse.urlunparse(('file','',os.path.abspath(f),'','',''))
                #print "Contents URL " + str(contentsURL)
                acr.astrogrid.myspace.copyURLToContent(contentsURL, remoteFile)

            except Exception, e:
                print "Whoops " + str(e)
                
        elif os.path.isdir(f):
            remoteDir = acr.astrogrid.myspace.createChildFolder(remoteFolder,f)
            copyFolder(f, remoteDir)


def copyRoot():
    home = acr.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
    # home should be a directory !
    copyFolder(os.path.curdir,home)

if __name__ == "__main__":
   # myFile = os.path.join(os.path.curdir,"arrrg")
   # import urlparse
    #('http', 'www.cwi.nl:80', '/%7Eguido/Python.html', '', '', '')
   # print str(urlparse.urlunparse(('file','',os.path.abspath(myFile),'','','')))
    copyRoot()


    
        