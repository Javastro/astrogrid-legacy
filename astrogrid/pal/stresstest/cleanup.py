#!/usr/bin/env python
# Deletes the contents of the specified folder (but not the folder)

from astrogrid import acr

#ROE
delfolder = "ivo://agtest.roe.ac.uk/KonaAndrews#test_folder"
#DAVE
#delfolder = "ivo://org.astrogrid.demo/frog#test_folder"
#DEV NETWORK
#delfolder = "ivo://org.astrogrid.test1.community1/KonaAndrews#test_folder"

def deleteFolder(bringoutyerdead):
    folderInfo = acr.astrogrid.myspace.getNodeInformation(bringoutyerdead)
    if folderInfo['file']:
        #delete me
        print "Deleting " + folderInfo['name']

    elif folderInfo['folder']:
        #recurse
        print "Drilling down into " + folderInfo['name']
        contents = acr.astrogrid.myspace.listIvorns(bringoutyerdead)
        for f in contents:
            deleteFolder(f)
        print "Deleting folder " + folderInfo['name']
    else: 
        print "Strange - what's a " + folderInfo['name'] + "?"
    try:
			if (folderInfo['file']):
				acr.astrogrid.myspace.delete(bringoutyerdead)
    except Exception,e:
        print "whoops "+str(e)

deleteFolder(delfolder)

