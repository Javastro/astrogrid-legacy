#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Gradual stress test script.
#     </meta:title>
#     <meta:description>
#         MySpace comparison test script.
#         Creates a set of files and copies them around within the myspace tree.
#         Designed to compare behaviour of FileManager and VOSpace services.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 286 $
#         $LastChangedDate: 2007-09-06 12:49:40 +0100 (Thu, 06 Sep 2007) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>
#
#

import os
import sys
import time
import getopt
import logging

#
# Import our test settings.
from settings import *

#
# Import our test tools.
from logger  import *
from testacr import *
#
# Import our ACR wrappers.
from myspace import MySpace 

#
# Set the default loop count.
TEST_LOOP=100
#
# Set the tidy flag to false.
TEST_TIDY=False
#
# Set the exit flag to false.
TEST_EXIT=False

#
# Get the command line options.
opts, args = getopt.getopt(
	sys.argv[1:],
	'u:p:l:t',
    	[
    	'user=',
    	'pass=',
    	'loop='
    	'tidy'
    	]
	)

for opt, arg in opts:
    if opt == "-u":
        TEST_USER = arg
    if opt == "--user":
        TEST_USER = arg
    if opt == "-p":
        TEST_PASS = arg
    if opt == "--pass":
        TEST_PASS = arg
    if opt == "-l":
        TEST_LOOP = int(arg)
    if opt == "--loop":
        TEST_LOOP = int(arg)
    if opt == "-t":
        TEST_TIDY = True
    if opt == "--tidy":
        TEST_TIDY = True
    if opt == "-x":
        TEST_EXIT = True
    if opt == "--exit":
        TEST_EXIT = True

logging.debug("Test user [%s]", TEST_USER)

#
# Create a myspace wrapper.
logging.debug("Creating myspace object")
myspace = MySpace(
    ar,
	TEST_USER,
	TEST_PASS,
	TEST_AUTH
	)

#
# Login to myspace account.
myspace.login(
    ar
    )

#
# Create the root folder.
testroot = 'test-%(host)s-%(user)s' % {
    'host':testhost,
    'user':testuser
    }
myspace.createFolder(
    ar,
    testroot
	)

#
# Create the flat set.
total = 0
for loop in range(1, TEST_LOOP) :

    logging.debug("Loop %d", loop)

    #
    # Create the test folder.
    start = time.time()
    myspace.createFolder(
        ar,
        '%(root)s/test-%(file)s' % {
            'root':testroot,
            'file':loop
            }
    	)
    done  = time.time()
    diff  = done - start
    total = total + diff

    logging.debug("Time %f", diff)
    logging.debug("Aver %f", (total/loop))

#
# Wrap the set into a tree.
total = 0
for loop in range(1, (TEST_LOOP - 1)) :

    logging.debug("Loop %d", loop)

    #
    # Move the test folder.
    start = time.time()
    myspace.moveFile(
        ar,
        '%(root)s/test-%(file)s' % {
            'root':testroot,
            'file':loop
            },
        '%(root)s/test-%(path)s/test-%(file)s' % {
            'root':testroot,
            'path':(loop + 1),
            'file':loop
            }
    	)
    done  = time.time()
    diff  = done - start
    total = total + diff

    logging.debug("Time %f", diff)
    logging.debug("Aver %f", (total/loop))

#
# Delete the last top node.
if (TEST_TIDY):
    start = time.time()
    myspace.deleteFile(
        ar,
        '%(root)s/test-%(file)s' % {
            'root':testroot,
            'file':(TEST_LOOP - 1)
            },
    	)
    done  = time.time()
    diff  = done - start
    logging.debug("Time %f", diff)

#
# Exit the AR.
if (TEST_EXIT):
    logging.info("Shutting down ACR ...")
    try:
        ar.builtin.shutdown.halt()
    except :
        logging.debug("Shutdown ...")

#
# Done ...
logging.info("Test done ...")



