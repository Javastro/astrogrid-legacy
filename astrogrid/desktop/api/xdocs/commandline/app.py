#!/usr/bin/env python
# execute a long-running task (cea service)
# takes an application name as first argument, and builds specific commandline args
# for invoking this application
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.

# add support for enumeration parameters.
# add support for repeated parameters.

import xmlrpclib
import sys
import os
import os.path
import urlparse
import time
import optparse
import textwrap


#connect to acr
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
ar = xmlrpclib.Server(prefix + "xmlrpc")

#take references to acr servicces we'll be using
apps = ar.astrogrid.applications
registry = ar.ivoa.registry

#parse arguments
#find the application name. - must be first argument, and first arg must not start wtih '-'
#likewise, interface name can be the second argument
application = None # if specified, will be a datastructure describing this application
appId = None
interfaces = None # if specified, will be a list of interfaces to document
startIx = 1 # where the options start on the commandline


if len(sys.argv) > 1 and  sys.argv[1][0] != '-' :
    try:
        startIx = startIx+1
        appId = sys.argv[1]
        application = apps.getCeaApplication(appId)
        #found an appication, lets see if we can find the interface too.
        interfaces= application['interfaces']
        if len(sys.argv) > 2 and sys.argv[2][0] != '-'  :
            startIx = startIx + 1
            iname = sys.argv[2]
            wanted = filter(lambda i : i['name'] == iname, interfaces)
            if len(wanted) ==1: # good, found the interface we're after.
                interfaces = wanted
            else:
                sys.stderr.write("Interface %s unrecognized" % iname)
    except Exception:
        sys.exit("Failed to load application information for " + sys.argv[1])
        
        
parser = optparse.OptionParser()
parser.disable_interspersed_args()

#if an application has been provided, add additional arguments specific to it.
if application:
    
    parser.set_usage('%prog ' + application['id'] + ' [interface-name] [options]')
    parser.set_description("Invoke " + application['title'])
    if len(interfaces) > 1: # if we've got more than one interface, add an option for the user to select which
        inames = map(lambda i:i['name'],interfaces)
        parser.add_option('-i','--interface',default=inames[0] ,choices=inames,
                    help='select which interface to invoke %s, default: %s' % (inames,inames[0]))
    #find the parameters references in the selected  interfaces
    inputs = []
    outputs = []
    for i in interfaces:
        for j in i['inputs']:
            inputs.append(j['ref'])
        for j in i['outputs']:
            outputs.append(j['ref'])        
    applicableParams = filter (lambda p: p['name'] in inputs + outputs,application['parameters'])
    # helper function to prodiuce documentation    
    def doc(parameter):
        s = parameter['uiName']+" - "+parameter['description'] 
        if parameter['name'] in outputs:
            s = s + (" (output parameter - only required when staging to myspace)")
        return s
    for p in applicableParams: # produce a commandline arg for each parameter
        if p.has_key('defaultValue') and len(p['defaultValue']) > 0:
            parser.add_option('--' + p['name'],metavar=p['type'],default=p['defaultValue'],
                              help= doc(p)+ " (default : " + p['defaultValue'] + ")")
        else :
            if len(interfaces) > 1: #provide info on which interfaces this parameter is required for.
                name = p['name']
                required = filter(lambda i: 
                                  len(filter(lambda x:
                                          x['ref']==name
                                          ,i['inputs'] + i['outputs'])) > 0
                                  , interfaces)
                # we treat all args as repeatable - i.e. list typed, by default.
                parser.add_option('--' + name,metavar=p['type'],
                                  help=doc(p) +  " (required in interfaces %s)" % map (lambda i:i['name'], required))
            else : # straightforward info.
                parser.add_option('--' + p['name'],metavar=p['type'],help=doc(p))
    #application parameters.        
    parser.add_option('-a','--about',action='store_true',default=False
                  ,help='Display help for an application and exit')
else: # no application provided.
    parser.set_usage('%prog <application-id> [interface-name] [options]')
    parser.set_description("Invoke an application (which must be specified as first parameter)")
  
#thse options are always available.
parser.add_option('-l','--list',action='store_true',default=False
                  ,help='list all known long running tasks, and exit')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')

#choose where to parse rest of args from.
(opts, args) = parser.parse_args(args=sys.argv[startIx:])
	
#handle the big options first.
if opts.examples:
    print '''
app.py -l
    --- list all possible tasks
app.py ivo://org.astrogrid/Galaxev --help
    --- display help on how to invoke the Galaxev task
app.py ivo://org.astrogrid/SExtractor --about
    --- display fuller information about the SExtractor task
app.py ivo://org.astrogrid/SExtractor  simple --help
    --- display help on how to invoke the 'simple' interface of the SExtractor task
app.py ivo://org.astrogrid/MERLINImager --RA=83.633208  --Dec=22.014472
    --- invoke MerlinImager task with these parameters (note, parameter names are case-sensitive)
    '''
    sys.exit()
elif opts.list:
    xq = apps.getRegistryXQuery()
    for i in registry.xquerySearch(xq):
        print i['title'], ":", i['id'] 
    #    for l in textwrap.wrap(i['content']['description'],80):
    #        print l
    #    print "-" * 80
    sys.exit()
elif application and opts.about:
    print apps.getDocumentation(appId)
    sys.exit()
elif not application:
    parser.print_help()
    sys.exit()

# ok, so we want to run an app.

#first determine the interface to use.
if len(interfaces) > 1:
    if opts.interface:
        interfaces= filter(lambda i : i['name'] == opts.interface,interfaces)
        if len(interfaces) == 0:
            parser.error("Interface name %s not recognized" % opts.interface)
    else:
        parser.error("You must choose an interface")

#now interfaces is a single item list.
interface = interfaces[0]

#create a tool template.
tool = apps.createTemplateStruct(application['id'],interface['name'])
# populate this tool from commandline args
for (name,value) in tool['input'].items():
    # bit fiddly. got a string, but need a method name - so use eval.
    if eval('opts.' + name):
        value['value']= eval('opts.' + name)
        #assume all input vars are direct
for (name,value) in tool['output'].items():
    if eval('opts.' + name):
        value['value']= eval('opts.' + name)
        value['indirect']=True
        #assume all output vars are indirect

execId = None
try :
    # convert the structure to an xml document. could save it at this point
    toolDoc = apps.convertStructToDocument(tool)
    # validate the document - will throw an exception if not correct.
    apps.validate(toolDoc)
    #submit the query to the server
    execId = apps.submit(toolDoc)
except xmlrpclib.Fault, e :
    parser.error(e.faultString)
# take a nap while things kick off. if we don't, we may not find it's there yet.
time.sleep(30)

#check on the progress of the query.
progress = apps.getExecutionInformation(execId)
# loop round until the query completes.  
while progress['status'] not in ['ERROR','COMPLETED','UNKNOWN'] :
    sys.stderr.write('.')
    time.sleep(60)
    progress = apps.getExecutionInformation(execId)

#query finished
if progress['status'] == "ERROR":
    sys.exit("Application ended in error")
    
sys.stderr.write("Completed\n")

results = apps.getResults(execId)
if len(results) == 1:
		print results.values()[0]
else:
		for (k,v) in results.items():
			print "Result :", k
			print v