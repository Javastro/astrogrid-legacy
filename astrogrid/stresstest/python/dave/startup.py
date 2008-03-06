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

#
# Import the Python libraries.
import os
import sys
import time
import getopt
import logging
import xmlrpclib

#
# Import our MySpace wrapper.
from myspace import MySpace 

#
# Test registry settings.
astroreg = 'http://casx019-zone1.ast.cam.ac.uk:8080/astrogrid-registry/services/RegistryQueryv1_0'

#
# Service settings.
#astrodsa  = 'ivo://org.astrogrid.demo/first-catalogue/ceaApplication'

#
# Default test settings.
astroauth = 'org.astrogrid.regtest'
astrouser = 'alpha'
astropass =	'qwerty'

#
# Setup our logger.
from logger import *

#
# Set the default loop counters.
innerloop=5
outerloop=5
#
# Set the default wait.
testwait=5
#
# Set the tidy flag to false.
testtidy=False
#
# Set the exit flag to false.
testexit=False

# Set the DSA-related defaults
# NB: These will work on the Cam test rig only
rows=10000
ceaivorn="org.astrogrid.regtest.dsa/mysql-first/CatName_first/ceaApplication"
querytable="TabNameFirst_catalogue"

#
# Get the command line options.
opts, args = getopt.getopt(
	sys.argv[1:],
	'a:u:p:i:o:w:r:c:q:dtx',
    	[
    	'auth=',
    	'user=',
    	'pass=',
    	'inner=',
    	'outer=',
    	'wait=',
      'rows=',
      'cea=',
      'querytable=',
    	'debug',
    	'tidy',
    	'exit'
    	]
	)

for opt, arg in opts:

    if opt == "-a":
        astroauth = arg
    if opt == "--auth":
        astroauth = arg

    if opt == "-u":
        astrouser = arg
    if opt == "--user":
        astrouser = arg

    if opt == "-p":
        astropass = arg
    if opt == "--pass":
        astropass = arg

    if opt == "-i":
        innerloop = int(arg)
    if opt == "--inner":
        innerloop = int(arg)

    if opt == "-o":
        outerloop = int(arg)
    if opt == "--outer":
        outerloop = int(arg)

    if opt == "-w":
        testwait = int(arg)*60
    if opt == "--wait":
        testwait = int(arg)*60

    if opt == "-d":
        logger.setLevel(logging.DEBUG)
    if opt == "--debug":
        logger.setLevel(logging.DEBUG)

    if opt == "-t":
        testtidy = True
    if opt == "--tidy":
        testtidy = True

    if opt == "-x":
        testexit = True
    if opt == "--exit":
        testexit = True

    # These ones are for the DSA stresstests in ../keith/
    if opt == "-r":
        rows = int(arg)
    if opt == "--rows":
        rows = int(arg)

    if opt == "-c":
        ceaivorn = arg
    if opt == "--cea":
        ceaivorn = arg

    if opt == "-q":
        querytable = arg
    if opt == "--querytable":
        querytable = arg
	

logging.debug("Starting test [%s]", testname)
logging.debug("Astro user [%s]", astrouser)
logging.debug("Astro auth [%s]", astroauth)

#
# Pause for the delayed start.
if (testwait > 0):
    logging.info("Pausing for %ds", testwait)
    while (testwait > 39):
        time.sleep(20)
        testwait = testwait - 20
        logging.debug("........... %ds", testwait)

    while (testwait > 19):
        time.sleep(10)
        testwait = testwait - 10
        logging.debug("........... %ds", testwait)

    while (testwait > 0):
        time.sleep(1)
        testwait = testwait - 1
        logging.debug("........... %ds", testwait)

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
# Check the root URL.
logging.debug("Checking VOExplorer connection")
ar.system.webserver.getUrlRoot()
#
# Set the registry endpoints
logging.debug(
    "Setting VOExplorer registry endpoint [%s]",
    astroreg
    )
ar.system.configuration.setKey(
    "org.astrogrid.registry.query.endpoint",
    astroreg
    )
ar.system.configuration.setKey(
    "org.astrogrid.registry.query.altendpoint",
    astroreg
    )

#
# Set the test root.
astroroot='%(host)s-%(user)s-%(date)s' % { 
    'date':testdate,
    'host':testhost,
    'user':testuser
    }
#
# Create our myspace wrapper.
logging.debug("Creating myspace object")
myspace = MySpace(
    ar,
	astrouser,
	astropass,
	astroauth
	)
#
# Login to our myspace account.
myspace.login(
    ar
    )
#
# Create the test root folder.
myspace.createFolder(
    ar,
    astroroot
	)


