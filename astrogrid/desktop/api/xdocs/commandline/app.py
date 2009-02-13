#!/usr/bin/env python
# execute a Remote Application (CEA)
# takes an application name as first argument, and builds specific commandline args
# for invoking this application
# Author: Noel Winstanley noel.winstanley@manchester.ac.uk 2006 - 2008
# run with --help for usage information.


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

def normspaces(s):
    """normalize spaces in a string - used to tidy up content from registry which already contains formatting"""
    return ' '.join(s.split())

#parse arguments - this script can be run in 2 different modes, depending
#whether an application name is provdied as the first parameter.
#so detect this first, to determine which mode we'r in.

#find the application name. - must be first argument, and first arg must not start wtih '-'
#likewise, interface name can be the second argument
application = None # if specified, will be a datastructure describing this application
appId = None
interfaces = None # if specified, will be a list of interfaces to document
selectedInterface = None # interface that has been selected.
startIx = 1 # where the options start on the commandline

if len(sys.argv) > 1 and  sys.argv[1][0] != '-' :
    try:
        startIx = startIx+1
        appId = sys.argv[1]
        application = apps.getCeaApplication(appId)
        #found an appication, lets see if we can find the interface too.
        interfaces= application['interfaces']        
        if len(sys.argv) > 2 and sys.argv[2][0] != '-'  : #interface parameter present
            startIx = startIx + 1
            iname = sys.argv[2]
            wanted = filter(lambda i : i['name'] == iname, interfaces)
            if len(wanted) ==1: # good, found the interface we're after.
                selectedInterface = wanted[0]
            else:
                sys.exit("Unknown Interface: " + iname)
        else: # selected interface is the first one.
            selectedInterface = interfaces[0]            
    except Exception:
        sys.exit("Failed to load application information for " + sys.argv[1])
        
parser = optparse.OptionParser()
parser.disable_interspersed_args()

#if an application id has been given, produce help and parameters specific to it.
if application:
    content = application['content']
    if len(interfaces) == 1: #only one interface - no need to specify it
        parser.set_description("Execute " + application['title'] 
                           + ('description' in content and (" -- " + normspaces(content['description'])) or''))            
        parser.set_usage('%prog ' + application['id'] + " <parameter options>")            
    else: #more than one interface - list all.     
        parser.set_description("Run " + selectedInterface['name'] 
                            + ' interface of ' + application['title'] 
                           + ('description' in content and (" -- " + normspaces(content['description'])) or''))            
        parser.set_usage('%prog ' + application['id'] + ' <' 
                      + "|".join(map(lambda i: i['name'],interfaces))
                      + "> <parameter options>")   
     
      #find the parameters involved in this interface.
    inputs = []
    outputs = []
    requiredParamNames = []
    repeatableParamNames = []
    for j in selectedInterface['inputs']:
            paramName = j['ref']
            inputs.append(paramName)
            if j['min'] > 0 :
                requiredParamNames.append(paramName)
            if j['max'] != 1:
                repeatableParamNames.append(paramName)
    for j in selectedInterface['outputs']:
            paramName = j['ref']
            outputs.append(paramName)  
            #don't check for optional parameters - all output are considered optional by this script.
            if j['max'] != 1:
                repeatableParamNames.append(paramName)            
                       
    wanted = inputs + outputs   
    applicableParams = filter (lambda p: p['name'] in wanted,application['parameters'])

    for p in applicableParams: # produce a commandline arg for each parameter
        n = p['name']
        opt = optparse.make_option('--' + n.lower(),dest=p['name'])
        
        doc = ""
        if 'uiName' in p and p['uiName'] != n: #if ui name is different (more descriptive, include it)
            doc = p['uiName'] + ": "
        
        if 'description' in p:
            doc += normspaces(p['description'])
        
        if 'options' in p and len(p['options']) > 0:
            opt.choices=p['options']        
            doc += " [" + '|'.join(p['options']) + "] "
            if len(p['options']) == 1: #handle degenerate case
                opt.default = p['options'][0]
                doc += " (default: %default)"
                if n in requiredParamNames:
                    requiredParamNames.remove(n) #not required - as has a default   
        
        if 'type' in p and p['type'] != 'text': # an interesting type.
            type = p['type']
            opt.metavar=type
            if type in ['RA','Dec','real']:
                opt.type='float'
            elif type =='integer':
                opt.type='int'        
            
        if 'defaultValue' in p and len(p['defaultValue']) > 0:
            opt.default=p['defaultValue']
            doc += " (default: %default)"
            if n in requiredParamNames:
                requiredParamNames.remove(n) #not required - as has a default            
            
        if n in outputs:
            doc += " (output param - only needed to write to myspace)"
            
        if n in requiredParamNames: #if this parameter is still required, document it.
            doc += " *mandatory*" 
        
        if n in repeatableParamNames:
            opt.action='append' # collect a list of values for a repeated parameter
            doc += " *repeatable*"
            
        if 'unit' in p :
            doc += " (" + p['unit'] + ")"
                        
        opt.help=doc                                            
        parser.add_option(opt)
else: # no application provided.
    parser.set_usage('%prog <application-id> [interface-name] <parameter options>')
    parser.set_description("""
Execute a remote application, and return results to local disk or myspace.
Use '--browse' or '--list' to select an application. 
Re-run this script with the selected application's ID  plus '--help' 
to receive application-specific help.
    """)
  
#thse options are always available.
parser.add_option('-b','--browse', action='store_true',default=False
                  , help='browse all known remote applications in a GUI, and exit')
parser.add_option('-l','--list',action='store_true',default=False
                  ,help='list all known remote applications, and exit')
parser.add_option('-e','--examples', action='store_true', default=False
                  , help='display some examples of use and exit.')


#choose where to parse rest of args from.
(opts, args) = parser.parse_args(args=sys.argv[startIx:])
	
#handle the big options first.
if opts.examples:
    parser.exit(0,'''
examples:
app.py --list
        : list all known applications

app.py ivo://org.astrogrid/HDFImager --help
        : display help about the Merlin HDFImage application
        
app.py ivo://org.astrogrid/SExtractor  simple --help
        : display help on how to execute the 'simple' interface of SExtractor 

app.py ivo://wfau.roe.ac.uk/first-dsa/wsa/ceaApplication ConeSearch --help
        : display help for the 'ConeSearch' interface to 'First Survey Catalogue'

app.py ivo://wfau.roe.ac.uk/first-dsa/wsa/ceaApplication ConeSearch  \\
    --format=HTML --ra=120.0 --dec=35.0 --radius=0.3
        : run a 'ConeSearch' on the First Survey Catalogue, return results as HTML

''')
elif opts.list:
    xq = apps.getRegistryXQuery()
    for i in registry.xquerySearch(xq):
        print i['title'], ":", i['id'] 
    #    for l in textwrap.wrap(i['content']['description'],80):
    #        print l
    #    print "-" * 80
    sys.exit()
elif opts.browse:
    rs = ar.dialogs.registryGoogle.selectResourcesXQueryFilter(
                "All Remote Applications",True,apps.getRegistryXQuery())
    if len(rs) > 0:
        print "You selected"
        for r in rs:
            print r['title']
            print " ", r['id']
    sys.exit()    

#if we get this far, we need an app ID. if there's none, display help and bail.
if not application:
    parser.print_help()
    parser.error("You must provide the ID of the remote application to execute.")

# ok, so we want to run an app.
#validate that we've got all the required parameters.
# i.e. those that aren't optional, and don't have defaults.

    
for n in requiredParamNames:
    if not (hasattr(opts,n) and getattr(opts,n) != None):
        parser.print_help()
        parser.error("Missing required parameter: " + n.lower())

#create a tool template.
tool = apps.createTemplateStruct(application['id'],selectedInterface['name'])

# populate this tool from commandline args            
for name in tool['input'].keys():
    if hasattr(opts,name) and getattr(opts,name) != None:
            v =getattr(opts,name) 
            if isinstance(v,list) and len(v) > 0: 
                #it's a repeated parameter, represenrted by a list of vals
                #convert list of input vals into a list of parameter structs
                tool['input'][name] = map(lambda i: {'value':i,'indirect':False},v)                
            else:
                tool['input'][name]['value'] = v
        #assume all input vars are direct
        
for name in tool['output'].keys():
    if hasattr(opts,name) and getattr(opts,name) != None:
            v =getattr(opts,name) 
            if isinstance(v,list) and len(v) > 0: 
                #it's a repeated parameter, represenrted by a list of vals
                #convert list of input vals into a list of parameter structs
                tool['output'][name] = map(lambda i: {'value':i,'indirect':True},v)                
            else:
                tool['output'][name]['value'] = v
                tool['output'][name]['indirect']=True
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
    sys.exit(e.faultString)
# take a nap while things kick off. if we don't, we may not find it's there yet.
time.sleep(30)

#check on the progress of the query.
progress = apps.getExecutionInformation(execId)
# loop round until the query completes.  
while progress['status'] not in ['ERROR','COMPLETED','UNKNOWN'] :
    sys.stderr.write('.')
    time.sleep(30)
    progress = apps.getExecutionInformation(execId)

#query finished
if progress['status'] == "ERROR":
    #even if there's an error, we try to get the results.
    sys.stderr.write("Application ended in error\n")
else:    
    sys.stderr.write("Application Completed\n")

results = apps.getResults(execId)
if len(results) == 1:
		print results.values()[0]
else:
        print "Results"
        for (k,v) in results.items():            
            print '-------------------------'
            print k, ':'
            print v