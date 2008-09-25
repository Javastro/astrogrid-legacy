#!/usr/bin/env python
# Produce a tree-view of the user's homespace

from astrogrid import acr


def show(folder, indentLvl=0):
    # should check that it is a folder
    folderInfo = acr.astrogrid.myspace.getNodeInformation(folder)
    if not folderInfo['folder']:
        raise RuntimeError, "This function should be called with a folder"
    indent = "  " * indentLvl
    print indent + "-" + folderInfo['name']
    contents = acr.astrogrid.myspace.listIvorns(folder)
    #directories first
    files = []
    for f in contents:
        fileInfo = acr.astrogrid.myspace.getNodeInformation(f)
        if fileInfo['file']:
            files.append(fileInfo['name'])
        else:
            show(f, indentLvl+1)
    #files next
    for f in files:
        print indent + "  " + f

def showRoot():
    home = acr.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
    # home should be a directory !
    show(home)

showRoot()


    
    