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
    print "Copying contents of " + str(localFolder)+ " to " + str(remoteFolder)

    contents = os.listdir(localFolder)
    #print "contents: " + str(contents)

    for f in contents:
        fullPath = os.path.join(localFolder,f)
        if os.path.isfile(fullPath):
            
            try:
                remoteFile = acr.astrogrid.myspace.createChildFile(remoteFolder,f)
                print "Copying " + f + " to " + remoteFile
                contentsURL = urlparse.urlunparse(('file','',os.path.abspath(fullPath),'','',''))
                #print "Contents URL " + str(contentsURL)
                acr.astrogrid.myspace.copyURLToContent(contentsURL, remoteFile)

            except Exception, e:
                print "Whoops " + str(e)
                print "Skipping file " + str(fullPath)
                
        elif os.path.isdir(fullPath):
            try:
                remoteDir = acr.astrogrid.myspace.createChildFolder(remoteFolder,f)
                
            except Exception,e:
                print "Warning - folder " + fullPath + " already exists"
                remoteDir = remoteFolder + "/" + str(f) # This isn't a very good idea
                
            copyFolder(os.path.join(localFolder,f), remoteDir)
        else:
            print "what is "+ str(fullPath) + "?  Not copying."


def copyRoot():
    home = acr.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
    # home should be a directory !
    copyFolder(os.path.curdir,home)

if __name__ == "__main__":
    copyRoot()


    
        