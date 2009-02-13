#!/usr/bin/env python
# delete a vospace file
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.
import xmlrpclib 
import sys
import os
import optparse

#verify we're running a suitable version of python
if not (sys.version_info[0] > 2 or sys.version_info[1] >= 5):
    print """This script runs best on python 2.5 or above. 
    You're running """ + sys.version


parser = optparse.OptionParser(usage="usage: %prog [options] <myspace ivorn or path>"
                      ,description="Delete a myspace file or directory")
parser.add_option('-d','--directory',action='store_true', default=False,
                  help='allow removal of directories')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#pare the options
(options, args) = parser.parse_args()
# check correctness of options.

if options.examples:
      parser.exit(0,"""
vorm.py  dir/file.txt
        : delete file.txt from myspace.
vorm.py -d dir/subdir
        : delete the directory 'subdir' and all it's contents.
""")

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")
myspace = ar.astrogrid.myspace

if len(args) != 1:
    parser.print_help()
    parser.error("Incorrect number of parameters.") 

path = args[0]
if not (path.startswith("ivo://") or path.startswith('#')) :
        #assume it's a path - add the required hash to indicate this.
        path = '#' + path
        
if not (myspace.exists(path)):
    sys.exit(path + " : No such myspace file or directory")

if not options.directory:
    ni = myspace.getNodeInformation(path)
    if ni['folder']:
        sys.exit(path + ": is a directory")
try:
    myspace.delete(path)
except xmlrpclib.Fault,e:
      sys.exit(e.faultString)