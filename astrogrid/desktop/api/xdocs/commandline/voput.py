#!/usr/bin/env python
# write data to a myspace file
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.
import xmlrpclib
import sys
import os
import os.path
import urlparse
import optparse
#verify we're running a suitable version of python
if not (sys.version_info[0] > 2 or sys.version_info[1] >= 5):
    print """This script runs best on python 2.5 or above. 
    You're running """ + sys.version

parser = optparse.OptionParser(usage="usage: %prog [options] <source: local file> <dest: myspace ivorn or path>"
                      ,description="Write to a myspace file")
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#pare the options
(options, args) = parser.parse_args()
# check correctness of options.

if options.examples:
      parser.exit(0,"""
voput.py localdir/localfile.txt myspacedir/subdir/myspacefile.txt
	: copy localfile.txt from localdisk to myspace as myspacefile.txt
	: creating myspacedir and subdir if necessary
""")
      
if len(args) != 2:
    parser.print_help()
    parser.error("Incorrect number of parameters.")      

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")
myspace = ar.astrogrid.myspace



# build url from local file reference.
if not os.path.exists(args[0]):
	sys.exit(args[0] + ": file does not exist")
	
f = file(args[0])

path = args[1]
if not (path.startswith("ivo://") or path.startswith('#')) :
        #assume it's a path - add the required hash to indicate this.
        path = '#' + path

url = urlparse.urlunsplit(["file","",os.path.abspath(f.name),"",""])


try:
# create node if needed.
	if not myspace.exists(path):
		myspace.createFile(path)
	myspace.copyURLToContent(url,path)
except xmlrpclib.Fault,e:
      sys.exit(e.faultString)
