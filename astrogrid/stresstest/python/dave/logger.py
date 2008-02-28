#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         Logging library for the test scripts.
#     </meta:title>
#     <meta:description>
#         Python module to output timestamped logging information in a similar format to the log4j tools used in the AG Java components.
#         The logs can be processed after the tests complete and the relevant data inserted into the database.
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
import time
import socket
import getpass
import logging

#
# Import our test settings.
from settings import *

#
# Get the machine IP address.
testhost = socket.gethostbyname(
    socket.gethostname()
    )
#
# Get the current user name.
testuser = getpass.getuser()
#
# Get todays date.
testdate = time.strftime('%Y%m%d-%H%M%S')

#
# Create the test name.

#
# Set the default format.
logging.basicConfig()
"""
logging.basicConfig(
    level  = logging.DEBUG,
    format = (
        '%%(asctime)-15s ACR %%(levelname)-5s %(host)-10s %(user)-10s %%(message)s' % {
            'host':testhost,
            'user':testuser
            }
        )
    )
"""

#
# Setup the logfile name.
#filepath = TEST_LOGS
filename = '%(host)s-%(user)s-%(date)s-test.log' % { 
    'date':testdate,
    'host':testhost,
    'user':testuser
    }

#
# Create a file logger.
filehandler = logging.FileHandler(
    os.path.join(TEST_LOGS, filename),
    'a'
    )
#
# Set the logfile format.
filehandler.setFormatter(
    logging.Formatter(
        '%%(asctime)-15s ACR %%(levelname)-5s %(host)s %(user)-5.5s %%(message)s' % {
            'host':testhost,
            'user':testuser
            }
        )
    )
#
# Get the test logger.
logger = logging.getLogger('')
#
# Add the file logger.
logger.addHandler(filehandler)
#
# Set the logging level.
logger.setLevel(logging.DEBUG)
#
# Check it works.
logging.info("Logging started")
