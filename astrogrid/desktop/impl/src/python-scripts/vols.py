#!/usr/bin/env python
# list resource in myspace
# usage : vols.py [options] [resource]
# parameter : ivorn of resorce to list. if omitted, defaults to user's root homedir
#options : see vols.py --help


import xmlrpclib as x
import sys
import os
from optparse import OptionParser
import textwrap


parser = OptionParser(usage="usage: %prog [options] [ivorn or path]")
parser.add_option("-l","--long", action="store_true", default=False
        ,help="long format")
parser.add_option("-1","--one-per-line", action="store_true", default=False
        , help="list one file per line")
parser.add_option("-d","--directory", action="store_true", default=False
        , help="list directory instead of contents")
parser.add_option("-F","--classify", action="store_true", default=False
        , help="append indicator of type to entries")
parser.add_option("-Q","--quote", action="store_true", default=False
        , help="enclose filenames in double quotes")
# add later - hard to implement
#parser.add_option("-R","--recursive", action="store_true", default=False
#        , help="list subdirectories recursively")
parser.add_option("-i","--node-ivorn", action="store_true", default=False
        , help="list node-ivorns for each entry, instead of name")

(options, args) = parser.parse_args()
# check correctness of options.

if options.quote and options.classify :
        parser.error("options -F and -Q are mutually exclusive")


prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
s = x.Server(prefix + "xmlrpc")


if len(args) > 0:
	n = args[0]
else:
        n = s.astrogrid.myspace.getHome()

ni = s.astrogrid.myspace.getNodeInformation(n)

if ni['folder'] and not options.directory :
        r = s.astrogrid.myspace.listIvorns(n)
else:
        r = [n]

if options.node_ivorn:
        fmt = lambda x : x
else :
        fmt = lambda x : x.split('#').pop().split("/").pop()

if options.classify: # hope this doesn't get recursive.
        fmt_orig = fmt
        def f(x) :
		info = s.astrogrid.myspace.getNodeInformation(x)
                if info['folder']:
                        return fmt_orig(x) + "/"
                else:
                        return fmt_orig(x)
        fmt = f

if options.quote :
        fmt_orig = fmt
        fmt = lambda x : "'" + fmt_orig(x) + "'"

if options.long :
        for i in r :
		info = s.astrogrid.myspace.getNodeInformation(i)
		info['name'] = i
		if not info.has_key('contentLocation'):
			info['contentLocation'] = ""
                print "%(name)s\t%(createDate)s\t%(modifyDate)s\t%(size)s\t%(contentLocation)s" % info
elif options.one_per_line :
        for i in r :
                print fmt(i)
else :
        s = "\t".join(map(fmt,r))
        print textwrap.fill(s)