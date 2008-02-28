#!/usr/bin/python
# <meta:header>
#     <meta:title>
#         AstroGrid test script settings.
#     </meta:title>
#     <meta:description>
#         AstroGrid test script settings.
#     </meta:description>
#     <meta:licence>
#         Copyright (C) AstroGrid. All rights reserved.
#         This software is published under the terms of the AstroGrid Software License version 1.2.
#         See [http://software.astrogrid.org/license.html] for details. 
#     </meta:licence>
#     <svn:header>
#         $LastChangedRevision: 461 $
#         $LastChangedDate: 2008-01-07 09:33:11 +0000 (Mon, 07 Jan 2008) $
#         $LastChangedBy: dmorris $
#     </svn:header>
# </meta:header>
#
#

#
# Import the os file tools
import os

#
# Registry settings.
REGISTRY_QUERY = 'http://casx019-zone1.ast.cam.ac.uk:8080/astrogrid-registry/services/RegistryQueryv1_0'
#REGISTRY_ADMIN = 'http://localhost:8080/astrogrid-registry/services/RegistryAdmin'

#
# Test settings.
TEST_AUTH = 'org.astrogrid.regtest'
#TEST_DSA  = 'ivo://org.astrogrid.demo/first-catalogue/ceaApplication'

#
# Test settings.
TEST_USER = 'alpha'
TEST_PASS =	'qwerty'

#
# The test logging directory.
TEST_LOGS = os.path.expanduser(
    '~/logs'
    )

#
# The local file to read for the endpoint.
ENDPOINT_FILE = os.path.expanduser(
    '~/.astrogrid-desktop'
    )

