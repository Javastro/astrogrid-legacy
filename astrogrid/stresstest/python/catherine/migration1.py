'''
stresstest migration tool, create folder/file according to pre-set variables in startup.py and copy them to another user's account
test createFolder/createFile/copy/paste/delete/myspace.copyURLToContent/login-out from ar

at the moment
  running using -t (tidy) forcefully as need to figure out a way to create another directory in second account
  running second account - hard coded in code as anybody/qwerty - need to fix this later
  also need to double check the logging messages are correct format

'''
import sys
import os
import string
import logging
import time
import getopt
import re

import urllib
import httplib
import urlparse

from itertools import izip
from configobj import ConfigObj

sys.path.append("../dave/")

from startup import *
from myspace import *

#Global variables

config = ConfigObj("config")
filename = config['dataFileRaw']
foldernameMod = config['folderMod']
filenameMod = config['fileMod']
fobj = open( filename, 'w')
fobjMod = open ( filenameMod, 'w')
fobjFodMod = open ( foldernameMod, 'w')
file = open(filenameMod)
#remember this test should be able to run within same user account or transfer data to another user account
#
#def createRootFolder():
# Create the flat set of folders.
total = 0
for loop in range(1, outerloop) :

    logger.info("Loop %d", loop)
    logger.info("CREATE [%d]", loop)

    #
    # Create the test folder.
    #start = time.time()
    myspace.createFolder(ar, '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':loop})
    #print "root is :", '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':loop}
    for loopfile in range(1, innerloop):
        myspace.createFile(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot, 'loop':loop, 'filename':loopfile})
        endpoint=myspace.copyURLToContent(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot, 'loop':loop, 'filename':loopfile}, 'http://www.astrogrid.org/maven/test.html')
        #print "ENDPOINT IS", endpoint

    #done  = time.time()
    #diff  = done - start
    #total = total + diff

    #logger.info("Time %f", diff)
    #logger.info("Aver %f", (total/loop))

'''
copyOld will read info from the old myspace tree and produce a data structure as a file on the local disk
which includes folder/filename and url for file content, inspired from vorip.py by John Taylor
'''
def copyOld(folder, nodeInfo=""):
    try:
        # should check that it is a folder
        folderInfo = ar.astrogrid.myspace.getNodeInformation(folder)
        if not folderInfo['folder']:
            raise RuntimeError, "This function should be called with a folder"

        nodeInfo = os.path.join(nodeInfo,folderInfo['name'])
        #print "nodeInfo= ", nodeInfo

        aLine = nodeInfo
        fobj.write('%s%s%s' % ('FOLDER:', aLine, os.linesep))

        #print "before contents ="
        contents = ar.astrogrid.myspace.listIvorns(folder)
        #print "after contents"
    #except Exception, e:
        #print "after trying find contents:  " + str(e)

        for f in contents:
            fileInfo = ar.astrogrid.myspace.getNodeInformation(f)
            if fileInfo['file']:
         #       print "Copying " + f + " to " + aLine
                aLine = f
                fobj.write('%s%s%s' % ('FILE:', aLine, os.linesep))
          #      print "XXX aLine = ", aLine, ", f = ", f
                '''
                getting file data from url
                '''
                #get the url for the file content
                stuff = ar.astrogrid.myspace.getReadContentURL(f)
                fobj.write('%s%s' % (stuff, os.linesep))
            else:
                copyOld(f, nodeInfo)
           #     print "copyOld else: ", nodeInfo
    except Exception, e:
        logger.debug("can't find contents using ar.astrogrid.myspace.listIvorns(folder) " + str(e))

def copyRoot():
    home = ar.astrogrid.myspace.getHome()
    #print "HomeSpace: " + home
    # home should be a directory !
    copyOld(home,os.path.curdir)

'''
reArrange function will re-arrange the raw data out-put from copyOld function
exchange critical info with new vospace system, prepare for transfer
it produces 2 more files:
    one contains folder info
    one contains file info
'''
def reArrange():
#    for eachLine in fobj:
    #print "line in file is: ", eachLine
    newIvorn = config['new_ivorn']
    oldIvorn = config['old_ivorn']
#    newUser = config['new_user']
    try:
        f1 = open(filename)
        for line in f1:
          #  print "line in file is: ", line
            s = line
          #  print "s original: ", s

            '''for folders, they contain ./home in the old system. get rid of the ./home
                not pretty, will think of a better way'''
            if s[:13] == 'FOLDER:./home':
                '''make sure string is not empty'''
                if len(s) > 13:
           #         print "inside reArrange, s before =", s
                    '''getting useful stuff without ./home, still need the folder/file info '''
                    s = s[14:len(s)]
                    fobjFodMod.write('%s%s' % (s, os.linesep))
            #        print "inside reArrange, after fobjFodMod.write"
            #this if for files, they don't contain ./home, but like this: "ivo://uk.ac.le.star/user_name#toad/testfile"
            elif s[:5] == 'FILE:':
                '''getting ready to replace old ivorn to new ivorn '''
                s = s[24:len(s)]
             #   print "inside reArrange, elif, s = ", s
                '''trying to swtich user'''
                #s.partition("#")
                pos = string.find(s,"#")
                pos = pos+1
                filefull = s[pos: len(s)]
                '''re construct with new Ivorn, new user name and folder name and file name '''
                #newline = newIvorn +"/" + newUser +"#"  + filefull
                newline = filefull
              #  print "inside reArrange, elif, newline =", newline
                fobjMod.write('%s' % (newline))
               # print "inside reArrange, elif, after find newline"
            else:
               # print "inside reArrange, else before write"
                fobjMod.write('%s' % (s))
               # print "inside reArrange, else after write"
        fobjMod.close()
        fobjFodMod.close()
        f1.close()
    except Exception, e:
        logger.debug("Whoops can't print modified file " + str(e))


itemsKey = []
itemsVal = []
myDict = {}

#config = ConfigObj("config")
fileLogName = config['logfile']
fileLog = open(fileLogName, 'w')

##In order to make a useful Dict, create 2 list, one contain Key, one contain Value for the files
##so we can extract useful info to upload into vospace. Key = filename (absolute); Value = URL that contains the file contents
def createList():
    filenameMod = config['fileMod']
    #print "filenameMod:", filenameMod
    file = open(filenameMod)

    seq = ''
    index = 0
    for line in file:
        if line.startswith("http://"):
            seq = line[:-1]
            itemsVal.append(seq)
        else:
            seq = line[:-1]
            itemsKey.append(seq)
    #makeDict()
    myDict = dict(izip(itemsKey,itemsVal))
    #for key in myDict.keys():
        #print "key=%s, value=%s" % (key, myDict[key])
    file.close()
    return myDict

def upload():
    try:
        myDict = createList()
        #print "myDict is:", myDict
        for key in myDict.keys():
            #print "key=%s, value=%s" % (key, myDict[key])
            path = key
            contentURL = myDict[key]
            #print "key is: ", key, "!!value = ", contentURL 
            
            if httpExists(contentURL) == 1:
                #create folder/file in vospace ready to take contents
                myspace1.createFile(ar, path)
                #print "creating file:", path
                #now do the upload to vospace stuff
                #print "contentURL: " +contentURL
                myspace1.copyURLToContent(ar, path, contentURL)
                #print "after uploaded file"
            else:
                fileLog.write('%s%s%s' % ('content URL for file: ', path, os.linesep))
                #print "url file does not exist&&&&"
    except Exception, e:
        logger.debug("Whoops " + str(e))
    fileLog.close()

def httpExists(url):
    #url = "http://www.astrogrid.org"
    #print "inside httpExists: ", url
    host, path = urlparse.urlsplit(url)[1:3]
    found = 0
    try:
        connection = httplib.HTTPConnection(host)  ## Make HTTPConnection Object
        connection.request("HEAD", path)
        #print "trying to get response..."
        #print "host is: ", host, "path:", path, "url = ", url
        responseOb = connection.getresponse()      ## Grab HTTPResponse Object

        if responseOb.status == 200:
            found = 1
        else:
            fileLog.write('Status %d %s : %s%s' % (responseOb.status, responseOb.reason, url, os.linesep))
            #print 'Status %d %s : %s' % (responseOb.status, responseOb.reason, url)
    except Exception, e:
        logger.debug(e.__class__,  e, url)
    return found

if __name__ == "__main__":
    #start timer in logs
    logging.info("EXPORT start from test: migration")    
    start = time.time()
    #   createRootFolder()
    copyRoot()
    ##if close here, the structure are radom read-in ones, need to organize it better
    fobj.close()
    reArrange()

    try:
        if ar.astrogrid.community.isLoggedIn():
            #print "after step 1 still logged in"
            #print astroauth, astrouser, astropass
            #print "astrogrid.myspace.getHome() is ", ar.astrogrid.myspace.getHome()
            myspace.logout(ar)
            #print "astrogrid.myspace.getHome() after trying to logout is ", ar.astrogrid.community.isLoggedIn()
    except Exception, e:
        logger.debug("there is a problem with this account, can't get myspace info, force exit! " + str(e))
    #################from migration2.py
    #reset the login details - hard coded for now
    astroauth = 'org.astrogrid.regtest'
    astrouser = 'anybody'
    astropass = 'qwerty'
   
    #    print "NEW LOGIN INFO IS: ", astroauth, astrouser, astropass
    myspace1 = MySpace(ar, astrouser, astropass, astroauth)

    myspace1.login(ar)
    #    createRootFolder()

    upload()
    testtidy=True
    #
    # Delete the last top node.
    if (testtidy):
        logging.info("Deleting tree")
        start = time.time()
        myspace.deleteFile(ar, '%(root)s' % {'root':astroroot})
        myspace1.deleteFile(ar, '%(root)s' % {'root':astroroot})
        done  = time.time()
        diff  = done - start
        logging.debug("Time %f", diff)
    myspace1.logout(ar)
    done  = time.time()
    diff  = done - start
    # myspace.deleteFile(ar, '%(root)s' % {'root':astroroot})
    logging.info('EXPORT ACTIOIN migration [%(time-delta)f] %(outerloop)s %(innerloop)s' % {'time-delta':diff, 'outerloop':outerloop, 'innerloop':innerloop})
    #
    # Exit the AR.
    if (testexit):
       logging.info("Shutting down ACR ...")
       try:
           ar.builtin.shutdown.halt()
       except :
           logging.debug("Shutdown ...")

    #
    # Done ...
    logging.info("Test done ...")
    

