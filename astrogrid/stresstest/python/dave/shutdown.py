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
import xmlrpclib

#
# Import our test tools.
from logger  import *

#
# Default delay of 5 seconds.
delay = 5

#
# Get the command line options.
opts, args = getopt.getopt(
	sys.argv[1:],
	'-d:',
    	[
    	'delay='
    	]
	)

#
# Get the connection endpoint.
endpoint = file(
    os.path.expanduser(
        '~/.astrogrid-desktop'
        )
    ).next().rstrip() + "xmlrpc"
#
# Connect to the ACR.
logging.debug(
	"Connecting to VOExplorer at [%s]",
	endpoint
	)
ar = xmlrpclib.Server(
    endpoint
    )

#
# Close the AR.
logging.info("Shutting down ACR ...")
try:
    ar.builtin.shutdown.halt()
except :
    logging.debug("Shutdown ...")

#
# Done ...
logging.info("Test done ...")


