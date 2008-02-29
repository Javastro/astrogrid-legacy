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
import sys
import time
import socket
import getpass
import logging

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
# Set the default format.
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
# Create the default formatter.
formatter = logging.Formatter(
    '%%(asctime)-15s %%(levelname)-5s %(host)s %(user)s %%(message)s' % {
        'host':testhost,
        'user':testuser
        }
    )
#
# Create our console logger.
livehandler = logging.StreamHandler(
    sys.stderr
    )
#
# Create our file logger.
filehandler = logging.FileHandler(
    os.path.join(
        os.path.expanduser(
            '~/logs'
            ),
        '%(host)s-%(user)s-%(date)s.log' % { 
            'date':testdate,
            'host':testhost,
            'user':testuser
            }
        ),
    'a'
    )
#
# Set the handler formats.
filehandler.setFormatter(
    formatter
    )
livehandler.setFormatter(
    formatter
    )
#
# Get the test logger.
logger = logging.getLogger()
#
# Add the handlers.
logger.addHandler(livehandler)
logger.addHandler(filehandler)

#
# Set the default logging level.
logger.setLevel(logging.INFO)
#
# Check it works.
logger.info("Logging started")

