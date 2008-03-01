#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Gradual stress test script.
#     </meta:title>
#     <meta:description>
#         MySpace comparison test script.
#         Imports some data into a file and then reads the contents back.
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

import urllib
import httplib

#
# Start our test.
from startup import *

#
# Import the data several times.
for inport in range(1, outerloop) :
    #
    # Set the target file name.
    path = '%(root)s/test-%(loop)03X' % {
        'root':astroroot,
        'loop':inport
        }

    #
    # Inport data from the source URL.
    logging.info("INPORT start")
    start = time.time()
    endpoint = myspace.copyURLToContent(
        ar,
        path,
        'http://www.astrogrid.org/maven/test.html'
    	)
    done  = time.time()
    diff  = done - start
    logging.debug("INPORT done [%f]", diff)

    #
    # Read the data several times.
    for read in range(1, innerloop) :
        #
        # Get a URL to read the data from.
        logging.info("EXPORT start")
        start = time.time()
        endpoint = myspace.getReadContentURL(
            ar,
            path
        	)
        done  = time.time()
        diff  = done - start
        logging.info("EXPORT done [%f]", diff)

        #
        # Read the data back and check it.
        logging.info("READ   start")
        start = time.time()
        data = urllib.urlopen(
            endpoint
            ).read()
        done  = time.time()
        diff  = done - start
        logging.info("READ   done [%f]", diff)

#
# Delete the last top node.
if (testtidy):
    logging.info("Deleting tree")
    start = time.time()
    myspace.deleteFile(
        ar,
        astroroot
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


