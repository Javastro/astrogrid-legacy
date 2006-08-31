#!/usr/bin/env python
# Recursively delete everything below your homespace
# handle with care!

from astrogrid import acr


def rmminusrstar(bringoutyerdead):
    folderInfo = acr.astrogrid.myspace.getNodeInformation(bringoutyerdead)
    if folderInfo['file']:
        #delete me
        print "Deleting " + folderInfo['name']

    elif folderInfo['folder']:
        #recurse
        print "Drilling down into " + folderInfo['name']
        contents = acr.astrogrid.myspace.listIvorns(bringoutyerdead)
        for f in contents:
            rmminusrstar(f)
        print "Deleting folder " + folderInfo['name']
        
    else: 
        print "Strange - what's a " + folderInfo['name'] + "?"
    try:
        acr.astrogrid.myspace.delete(bringoutyerdead)
    except Exception,e:
        print "whoops "+str(e)

if __name__ == "__main__":
    print "I hope you really meant to do that..."
    home = acr.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
    # home should be a directory !
    contents = acr.astrogrid.myspace.listIvorns(home)
    for f in contents:
        rmminusrstar(f)



    
    