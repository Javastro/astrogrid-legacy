#!/usr/bin/env python
""" 
Backup your entire myspace to the current directory
Needs astrogrid.py
   Author: jdt@roe.ac.uk
"""
from astrogrid import acr
import os
import urllib

def copyFolder(folder, indentLvl=""):
    # should check that it is a folder
    folderInfo = acr.astrogrid.myspace.getNodeInformation(folder)
    if not folderInfo['folder']:
        raise RuntimeError, "This function should be called with a folder"
    
    indentLvl=os.path.join(indentLvl,folderInfo['name'])
    os.mkdir(indentLvl)
    print indentLvl
    contents = acr.astrogrid.myspace.listIvorns(folder)


    for f in contents:
        fileInfo = acr.astrogrid.myspace.getNodeInformation(f)
        if fileInfo['file']:
            print "Copying " + f + " to " + indentLvl
            try:
                contentsURL = acr.astrogrid.myspace.getReadContentURL(f)
                localFileName = os.path.join(indentLvl,fileInfo['name'])
                print urllib.urlretrieve(contentsURL, localFileName);

            except Exception, e:
                print "Whoops " + str(e)
                
        else:
            copyFolder(f, indentLvl)


def copyRoot():
    home = acr.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
    # home should be a directory !
    copyFolder(home,os.path.curdir)

if __name__ == "__main__":
    copyRoot()


    
    