#!/usr/bin/env python
# list resources in myspace
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.


import xmlrpclib
import sys
import os
from optparse import OptionParser
import textwrap


parser = OptionParser(usage="usage: %prog [options] <node-ivorn or path>"
                      ,description="list contents of myspace directories")
parser.add_option("-l","--long", action="store_true", default=False
        ,help="long format")
parser.add_option("-1","--one-per-line", action="store_true", default=False
        , help="list one file per line")
parser.add_option("-d","--directory", action="store_true", default=False
        , help="list directory instead of contents")
parser.add_option("-F","--classify", action="store_true", default=False
        , help="append indicator of type to entries")
parser.add_option("-Q","--quote", action="store_true", default=False
        , help="enclose filenames in quotes")
# add later - hard to implement
#parser.add_option("-R","--recursive", action="store_true", default=False
#        , help="list subdirectories recursively")
# not correct at the mooment.
#parser.add_option("-i","--node-ivorn", action="store_true", default=False
#        , help="list node-ivorns for each entry, instead of name")
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')
#pare the options
(options, args) = parser.parse_args()
# check correctness of options.

if options.examples:
      print """
vols.py
        : list contents of user's home myspace directory
        
vols.py foo/bar
        : list contents of subdirectory 'foo/bar'
        
vols -l foo/bar
        : list long details of subdirectory 'foo/bar'
        
vols -F foo
        : list directory, indicating subdirectories

      """
      sys.exit()
        
if options.quote and options.classify :
        parser.error("options -F and -Q are mutually exclusive")


#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

myspace = ar.astrogrid.myspace

if len(args) > 0:
        n = args[0]
        if not (str(n).startswith("ivo://") or str(n).startswith('#')) :
                #assume it's a path - add the required hash to indicate this.
                n = '#' + n
else:
        n = myspace.getHome()

ni = None
try:
        ni = myspace.getNodeInformation(n)
except:
        sys.exit(n + ": No such file or directory")
        
if ni['folder'] and not options.directory :
        r = myspace.listIvorns(n)
else:
        r = [n]

#if options.node_ivorn:
#        fmt = lambda x : x
#        options.one_per_line = True
#else :
fmt = lambda x : x.split('#').pop().split("/").pop()

if options.classify: # hope this doesn't get recursive.
        fmt_orig = fmt
        def f(x) :
                info = myspace.getNodeInformation(x)
                if info['folder']:
                        return fmt_orig(x) + "/"
                else:
                        return fmt_orig(x)
        fmt = f

if options.quote :
        fmt_orig = fmt
        fmt = lambda x : "'" + fmt_orig(x) + "'"

if options.long :
        import datetime
        from time import strptime
        for i in r :
                info = myspace.getNodeInformation(i)
                cDate = datetime.datetime(* (strptime(str(info['createDate']),"%Y%m%dT%H:%M:%S")) [0:6])
                mDate = datetime.datetime(* (strptime(str(info['modifyDate']),"%Y%m%dT%H:%M:%S")) [0:6]) 
                print "%s\t%s\t%s\t%s" % (cDate.strftime('%d-%b-%y %H:%M'),mDate.strftime('%d-%b-%y %H:%M')
                                          ,info['size'],info['name'])
elif options.one_per_line :
        for i in r :
                print fmt(i)
else :
        s = "\t".join(map(fmt,r))
        print textwrap.fill(s)