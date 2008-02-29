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
# Start our test.
from startup import *

#
# Create the flat set of folders.
total = 0
for loop in range(1, outerloop) :

    logging.debug("Loop %d", loop)
    logging.info("CREATE [%d]", loop)

    #
    # Create the test folder.
    start = time.time()
    myspace.createFolder(
        ar,
        '%(root)s/test-%(loop)03X' % {
            'root':astroroot,
            'loop':loop
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
for loop in range(1, (outerloop - 1)) :

    #
    # Log the start of the loop.
    # This entry is the one that will be used for the stats.
    logging.info("MOVE   [%d]", loop)

    #
    # Move the test folder.
    start = time.time()
    myspace.moveFile(
        ar,
        '%(root)s/test-%(loop)03X' % {
            'root':astroroot,
            'loop':loop
            },
        '%(root)s/test-%(next)03X/test-%(loop)03X' % {
            'root':astroroot,
            'next':(loop + 1),
            'loop':loop
            }
    	)
    done  = time.time()
    diff  = done - start
    total = total + diff

    logging.debug("Time %f", diff)
    logging.debug("Aver %f", (total/loop))

#
# Delete the last top node.
if (testtidy):
    logging.info("Deleting tree")
    start = time.time()
    myspace.deleteFile(
        ar,
        '%(root)s/test-%(loop)03X' % {
            'root':astroroot,
            'loop':(outerloop - 1)
            },
    	)
    done  = time.time()
    diff  = done - start
    logging.debug("Time %f", diff)

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



