#!/usr/bin/env python
# read contents of a myspace resource
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.

import xmlrpclib 
import sys
import os
import urllib2
import optparse

parser = optparse.OptionParser(usage="usage: %prog [options] <node-ivorn or path>"
                      ,description="Read contents of a myspace file")
parser.add_option('-u','--url',action='store_true',default=False
                  , help='display the download URL, instead of file contents')
parser.add_option('-o','--output',metavar="FILE"
                  ,help='Save contents to file')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#pare the options
(options, args) = parser.parse_args()
# check correctness of options.

if options.examples:
      print """
voget.py dsa/results.vot
        : display contents of results.vot
        
voget.py --url dsa/results.vot
        : display download url of results.vot

voget.py -o myresults.vot dsa/results.vot
        : download dsa/results.vot as ./myresults.vot
      """
      sys.exit()

fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")
myspace = ar.astrogrid.myspace

path = args[0]
if not (path.startswith("ivo://") or path.startswith('#')) :
        #assume it's a path - add the required hash to indicate this.
        path = '#' + path
url = None
try:
        url =  myspace.getReadContentURL(path)
except:
        sys.exit(path + ": No such file")
        
if options.url:
        print url
elif options.output != None:
        import shutil
        if os.path.exists(options.output):
                sys.exit(options.output + " : File already exists")
        output = open(options.output,'w')
        input = urllib2.urlopen(url)
        shutil.copyfileobj(input,output)
        output.close()
else:
        resource = urllib2.urlopen(url)
        print resource.read()