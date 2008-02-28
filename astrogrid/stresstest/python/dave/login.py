#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Gradual stress test script.
#     </meta:title>
#     <meta:description>
#         Gradual stress test script.
#         Pauses, then loops for (100) sending an ADQL query and waiting for the results.
#         Shuts down the ACR when done.
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
# Get the command line options.
opts, args = getopt.getopt(
	sys.argv[1:],
	'a:u:p:',
    	[
    	'auth=',
    	'user=',
    	'pass='
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


