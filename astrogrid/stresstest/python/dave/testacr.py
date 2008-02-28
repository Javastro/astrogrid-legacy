#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         ACR library for the test scripts.
#     </meta:title>
#     <meta:description>
#         Sets up the ACR connection, and configures the registry settings.
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
import logging
import xmlrpclib

#
# Import our test settings.
from settings import *

#
# Get the connection settings.
endpoint = file(
    ENDPOINT_FILE
    ).next().rstrip() + "xmlrpc"

#
# Connect to the ACR.
logging.debug(
	"Connecting to ACR at [%s]",
	endpoint
	)
ar = xmlrpclib.Server(
    endpoint
    )

#
# Check the root URL.
logging.debug("Checking ACR connection")
ar.system.webserver.getUrlRoot()

#
# Set the registry endpoints
logging.debug(
    "Setting ACR registry query endpoint [%s]",
    REGISTRY_QUERY
    )
ar.system.configuration.setKey(
    "org.astrogrid.registry.query.endpoint",
    REGISTRY_QUERY
    )
ar.system.configuration.setKey(
    "org.astrogrid.registry.query.altendpoint",
    REGISTRY_QUERY
    )
"""
logging.debug(
    "Setting ACR registry admin endpoint [%s]",
    REGISTRY_ADMIN
    )
ar.system.configuration.setKey(
    "org.astrogrid.registry.admin.endpoint",
    REGISTRY_ADMIN
    )
"""

