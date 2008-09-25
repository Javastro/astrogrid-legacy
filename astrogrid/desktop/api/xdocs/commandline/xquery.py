#!/usr/bin/env python
#Noel Winstanley, AstroGrid, 2006
#noel.winstanley@manchester.ac.uk
#Fire an xquery at a vo registry.
#Usage: start Workbench / AR
# 	execute 'python xquery.py myquery.xquery'
import xmlrpclib 
import sys
import os
import os.path
import optparse


#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

#parse options
parser = optparse.OptionParser(usage='%prog [options] <xquery> | <query-file>',
     description="Perform an XQuery against the registry server")
parser.add_option('-i','--inputfile',action='store_true',dest='fromFile', default=True
                      , help='read query from a file (default)')
parser.add_option('-q','--query',action='store_false',dest='fromFile'
                   , help='read query from commandline')
parser.add_option('-e','--examples',action='store_true',default=False
                  ,help='display some examples of use and exit')

(opts,args) = parser.parse_args()

if opts.examples:
    print """
xquery.py
    --- run with default parameters
xquery query.xq
    -- run a query stored in file 'query.xq'
xquery -q "<size>{count(//vor:Resource)}</size>"
   --- run a query provided on the commandline (counts number of records in registry)
    """
    sys.exit()

#a default xquery
xq="""
<contacts>
{
//vor:Resource[matches(vr:identifier,'ivo://uk.ac.le.star/.*')]/vr:curation/vr:contact

}
</contacts>
"""
#find the query to run
if len(args) == 0:
    sys.stderr.write("No query provided - using default")
elif opts.fromFile:
    xq = open(args[0]).read()
else:
    xq = args[0]

#doit and print the result.
print ar.ivoa.registry.xquerySearchXML(xq)



