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

#config = ConfigObj(".config")
sys.path.append("../dave/")
#sys.path.append("/home/clq2/netbeansWS/20080225.clq/webapp/myspace/testing/migration1/python")
#path = config['includePath']
#print "path is: ", path
#sys.path.append(path)

from startup import *
from myspace import *
#from testacr import *
#from settings import *

#trying to see if this account is valid first, catching exception if we can't login and bail out
try:
    if ar.astrogrid.community.isLoggedIn():
        print "logged in"
        print astroauth, astrouser, astropass
        print "astrogrid.myspace.getHome() is ", ar.astrogrid.myspace.getHome()
    else:
        print "CANT login"
except Exception, e:
    print "there is a problem with this account, can't get myspace info, force exit! " + str(e)
    sys.exit()


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
    start = time.time()
    myspace.createFolder(ar, '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':loop})
    print "root is :", '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':loop}
    for loopfile in range(1, innerloop):
        myspace.createFile(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot, 'loop':loop, 'filename':loopfile})
        endpoint=myspace.copyURLToContent(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot, 'loop':loop, 'filename':loopfile}, 'http://www.astrogrid.org/maven/test.html')
        #print "ENDPOINT IS", endpoint

    done  = time.time()
    diff  = done - start
    total = total + diff

    logger.info("Time %f", diff)
    logger.info("Aver %f", (total/loop))

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
        print "can't find contents using ar.astrogrid.myspace.listIvorns(folder) " + str(e)

def copyRoot():
    home = ar.astrogrid.myspace.getHome()
    print "HomeSpace: " + home
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
        print "Whoops can't print modified file " + str(e)


itemsKey = []
itemsVal = []
myDict = {}

#config = ConfigObj("config")
fileLogName = config['logfile']
fileLog = open(fileLogName, 'w')
print "fileLog is: ", fileLogName

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
        print "myDict is:", myDict
        for key in myDict.keys():
            print "key=%s, value=%s" % (key, myDict[key])
            path = key
            contentURL = myDict[key]
            print "key is: ", key, "!!value = ", contentURL 
            
            if httpExists(contentURL) == 1:
                #create folder/file in vospace ready to take contents
                myspace1.createFile(ar, path)
                print "creating file:", path
                #now do the upload to vospace stuff
                print "contentURL: " +contentURL
                myspace1.copyURLToContent(ar, path, contentURL)
                print "after uploaded file"
            else:
                fileLog.write('%s%s%s' % ('content URL for file: ', path, os.linesep))
                print "url file does not exist&&&&"
    except Exception, e:
        print "Whoops " + str(e)
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
        print e.__class__,  e, url
    return found

if __name__ == "__main__":
 #   createRootFolder()
    copyRoot()
    ##if close here, the structure are radom read-in ones, need to organize it better
    fobj.close()
    reArrange()
#    myspace.logout(ar)

    print "OLD LOGIN INFO IS:", astroauth, astrouser, astropass
#myspace.logout(ar)
    try:
        if ar.astrogrid.community.isLoggedIn():
            print "after step 1 still logged in"
            print astroauth, astrouser, astropass
            print "astrogrid.myspace.getHome() is ", ar.astrogrid.myspace.getHome()
            myspace.logout(ar)
            print "astrogrid.myspace.getHome() after trying to logout is ", ar.astrogrid.community.isLoggedIn()
    except Exception, e:
        print "there is a problem with this account, can't get myspace info, force exit! " + str(e)
#################from migration2.py
#reset the login details
    astroauth = 'org.astrogrid.regtest'
    astrouser = 'anybody'
    astropass = 'qwerty'
   
    print "NEW LOGIN INFO IS: ", astroauth, astrouser, astropass
    myspace1 = MySpace(
        ar,
            astrouser,
            astropass,
            astroauth
            )

    myspace1.login(ar)
#    createRootFolder()
#create another root
#astroroot1='%(host)s-%(user)s-%(date)s' % {
#    'date':testdate,
#    'host':testhost,
#    'user':testuser
#    }

#total1 = 0
#for loop1 in range(1, outerloop) :

#    logger.info("Loop %d", loop)
#    logger.info("CREATE [%d]", loop)

    #
    # Create the test folder.
#    start1 = time.time()
#    myspace.createFolder(ar, '%(root)s/test-%(loop)03X' % {'root':astroroot1, 'loop':loop})
#    print "NEW ROOT X^&^(*&^*&^ root is :", '%(root)s/test-%(loop)03X' % {'root':astroroot1, 'loop':loop}
#    for loopfile in range(1, innerloop):
#        myspace.createFile(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot1, 'loop':loop, 'filename':loopfile})
#        endpoint1=myspace.copyURLToContent(ar, '%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot1, 'loop':loop, 'filename':loopfile}, 'http://www.astrogrid.org/maven/test.html')
        #print "ENDPOINT IS", endpoint

#    done1  = time.time()
#    diff1  = done1 - start1
#    total1 = total1 + diff1

#    logger.info("Time %f", diff1)
#    logger.info("Aver %f", (total1/loop))


    upload()
    #myspace1.logout(ar)
#testtidy=True
#
# Delete the last top node.
    if (testtidy):
        logging.info("Deleting tree")
        print "TESTTIDY ISSSSSSSSSSSSSSSSSSSS", testtidy
        print "AUTHHHHHHHHHHH",  astroauth, astrouser, astropass
        start = time.time()
#'%(root)s/test-%(loop)03X/filename-%(filename)s' % {'root':astroroot, 'loop':loop, 'filename':loopfile}
    #myspace.deleteFile(ar, '%(root)s/test-%(loop)03X' % {'root':astroroot, 'loop':(outerloop - 1)},)
        myspace.deleteFile(ar, '%(root)s' % {'root':astroroot})
        print "$$$$$$$$$$$$$$$$$$$$$$", '%(root)s' % {'root':astroroot}
        done  = time.time()
        diff  = done - start
        logging.debug("Time %f", diff)
    myspace1.logout(ar)
    print "LOGGINGGGGGGG :", testexit
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

