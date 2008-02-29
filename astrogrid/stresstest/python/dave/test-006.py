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
# Import our MySpace wrapper.
from myspace import MySpace 
#
# Import our startup params.
from startup import *

#
# Create our myspace wrapper.
logger.debug("Creating myspace object")
myspace = MySpace(
    ar,
	TEST_USER,
	TEST_PASS,
	TEST_AUTH
	)

#
# Login to our myspace account.
myspace.login(
    ar
    )

#
# Create the root folder.
myspace.createFolder(
    ar,
    '%(host)s-%(user)s-%(date)s.log' % { 
        'date':testdate,
        'host':testhost,
        'user':testuser
        }
	)

"""
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

"""

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



