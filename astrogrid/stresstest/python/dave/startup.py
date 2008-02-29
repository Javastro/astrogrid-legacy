#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Gradual stress test script.
#     </meta:title>
#     <meta:description>
#         System stress test.
#         Pauses at the start.
#         Sends ADQL queries to the DSA and stores the results in myspace.
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

import sys
import time
import getopt

#
# Import our test tools.
from logger  import *

#
# Set the default loop count.
TEST_LOOP=100
#
# Set the default wait.
TEST_WAIT=5
#
# Set the tidy flag to false.
TEST_TIDY=False
#
# Set the exit flag to false.
TEST_EXIT=False
#
# Get the test script name.
TEST_NAME= os.path.basename(
    sys.argv[0]
    )

#
# Get the command line options.
opts, args = getopt.getopt(
	sys.argv[1:],
	'a:u:p:l:w:dtx',
    	[
    	'auth=',
    	'user=',
    	'pass=',
    	'loop=',
    	'wait=',
    	'debug',
    	'tidy',
    	'exit'
    	]
	)

for opt, arg in opts:

    if opt == "-a":
        TEST_AUTH = arg
    if opt == "--auth":
        TEST_AUTH = arg

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

    if opt == "-w":
        TEST_WAIT = int(arg)*60
    if opt == "--wait":
        TEST_WAIT = int(arg)*60

    if opt == "-d":
        logger.setLevel(logging.DEBUG)
    if opt == "--debug":
        logger.setLevel(logging.DEBUG)

    if opt == "-t":
        TEST_TIDY = True
    if opt == "--tidy":
        TEST_TIDY = True

    if opt == "-x":
        TEST_EXIT = True
    if opt == "--exit":
        TEST_EXIT = True

logging.debug("Test name [%s]", TEST_NAME)
logging.debug("Test user [%s]", TEST_USER)
logging.debug("Test auth [%s]", TEST_AUTH)

#
# If we have a startup delay.
if (TEST_WAIT > 0):
    wait = TEST_WAIT
    logging.info("Pausing for %ds", wait)
    while (wait > 39):
        time.sleep(20)
        wait = wait - 20
        logging.debug("........... %ds", wait)

    while (wait > 19):
        time.sleep(10)
        wait = wait - 10
        logging.debug("........... %ds", wait)

    while (wait > 0):
        time.sleep(1)
        wait = wait - 1
        logging.debug("........... %ds", wait)


