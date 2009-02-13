#!/usr/bin/env python
# Search the registry.
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.
import xmlrpclib 
import sys
import os
import os.path
import optparse
import re
#verify we're running a suitable version of python
if not (sys.version_info[0] > 2 or sys.version_info[1] >= 5):
    print """This script runs best on python 2.5 or above. 
    You're running """ + sys.version
    
def mkQuery(kws):
    """ build an xquery out of one or more keyword terms"""
    xq = "//vor:Resource[not (@status='inactive' or @status='deleted') "
    for kw in kws:
        clause = """title &= '*%s*' or identifier &= '*%s*' or shortName &= '*%s*'
            or content/subject &= '*%s*' or content/description &= '*%s*'
            """ % (kw,kw,kw,kw,kw)
        
        xq += " and ( " + clause + ")"    
    return xq + "]"

def sendPlasticMessage(idList):
    """ send a plastic message to display a list of resouces"""
    plastic = ar.plastic.hub
    try:
        myId = plastic.registerNoCallBack("search.py")
        # broadcast a message. - tells the plastic apps display list list of resources
        plastic.requestAsynch(myId,'ivo://votech.org/voresource/loadList',[idList])            
    finally:
        plastic.unregister(myId)     

#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

defaultFormat="summary"
#parse options
parser = optparse.OptionParser(usage='%prog [options] <keywords> | <identifier>',
     description="Search the Registry by keyword or ID, and display results in various ways.")
parser.add_option('-i','--id',action='store_true',dest='identifier', default=False
                      , help='treat search term as a full registry identifier')
parser.add_option('-k','--keyword',action='store_false',dest='identifier'
                   , help='treat search term as keywords (default)')
parser.add_option('-f','--format', default=defaultFormat, choices=['identifier','summary','xml','plastic']
                  ,help='format to return results as: identifier, summary, xml, plastic (default: %s)' % defaultFormat)

parser.add_option('-e','--examples',action='store_true',default=False
                  ,help='display some examples of use and exit')

(opts,args) = parser.parse_args()

if opts.examples:
    parser.exit(0,"""
search.py abell
    : display summary for resources matching 'abell'

search.py "abell cluster" noras
    : display summary for resources matching 'abell cluster' and 'noras'

search.py --format==identifier abell
    : list identifiers of all resources matching 'abell'

search.py --format==plastic abell
    : display resources matching 'abell' in a PLASTIC viewer (e.g. VOExplorer)
    
search.py --format==xml "abell cluster" noras
    : display xml of all resources matching 'abell cluster' and 'noras'
    
search.py -i "ivo://nasa.heasarc/skyview/first"
    : display summary of the resource 'ivo://nasa.heasarc/skyview/first'

search.py --format=xml -i "ivo://nasa.heasarc/skyview/first" 
    : display xml of the resource 'ivo://nasa.heasarc/skyview/first'
""")
    
registry = ar.ivoa.registry

#take action
if len(args) == 0:
    parser.print_help()
    parser.error("no search terms provided")

if opts.format == 'identifier' or opts.format == 'plastic':
    if opts.identifier: #degenerate really
        r = registry.getResource(args[0])
        if opts.format == 'identifier':
            print r['id']
        else:
            sendPlasticMessage([r['id']])
    else:
        xq = mkQuery(args)
        result = registry.xquerySearchXML("""
<ul>
{
for $r in %s
return <li>{$r/identifier/text()}</li>
}
</ul>
         """ % xq)
        if opts.format == 'identifier':
            for id in re.findall("<li>(.*)</li>",result):
                print id
        else:
            rs = re.findall("<li>(.*)</li>",result)
            sendPlasticMessage(rs)
        
elif opts.format == 'summary':  
    if opts.identifier: 
        r = registry.getResource(args[0])
        print r['title'], r['id']
        print r['content']['description']
    else:
        xq = mkQuery(args)
        result = registry.xquerySearchXML("""
<ul>
{
for $r in %s
return <li>{$r/title}
            {$r/identifier}
            <description>{substring(normalize-space($r/content/description),1,200)}...</description>
        </li>
}
</ul>
            """ % xq)
        for l in re.finditer('<li>.*?<title>(.*?)</title>.*?<identifier>(.*?)</identifier>.*?<description>(.*?)</description>.*?</li>',result,re.DOTALL):
            print l.group(1), l.group(2)
            print l.group(3)
            print ""
    
elif opts.format == 'xml' :
    xq = None
    if opts.identifier:
        xq = "//vor:Resource[not (@status='inactive' or @status='deleted') and identifier='%s']" % args[0]
    else:
        xq = mkQuery(args)
    print registry.xquerySearchXML(xq)
    
    




