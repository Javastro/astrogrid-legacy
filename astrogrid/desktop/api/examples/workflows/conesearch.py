#!/usr/bin/env python
#
# Perform a seriese of catalog searches for a list of 
#position. Both the position list and services to query
# can be configured
#
#Transcription of JES Conesearch workflow into a commandline python script.
# connects to a running Astro Runtime using XMLRPC
# REQUIRES: VOTable.py 
import os
import sys
import xmlrpclib
import optparse
import urlparse
import VOTable
#CONNECT TO AR
fname = os.path.expanduser("~/.astrogrid-desktop")
assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
prefix = file(fname).next().rstrip()
acr = xmlrpclib.Server(prefix + "xmlrpc")

#SETUP VARIABLES
user_votable = ""
output_dir = os.getcwd()
radius= 0.1
cones = [
      "ivo://sdss.jhu/services/DR4CONE"
         , "ivo://irsa.ipac/2MASS-PSC"
        , "ivo://ned.ipac/Basic_Data_Near_Position"
      ]

#PARSE COMMANDLINE OPTIONS
#define options
parser = optparse.OptionParser(usage="conesearch.py [options]",
                               description='''
Iterate through a list of sources, for each querying a sequence of catalog services.
Results are grouped by source, and maybe saved to local disk, or to myspace.
''')
parser.add_option('-r','--radius', type='float',default=radius, metavar="FLOAT",
                      help="search radius - decimal degrees (default: %s)" % radius)
parser.add_option('-f','--sourcefile', metavar="FILE",
                  help="URI/file location of sourcelist votable (default: prompt using dialogue)")
parser.add_option('-o','--outputdir',default=output_dir, metavar="DIR",
                  help="URI/file output location (default: current directory)")
parser.add_option('-s','--service',action="append", metavar="ID",
                  help="URL/registryID of a service to query - repeat for multiple services")
parser.add_option('-d','--defaultServices',action="store_true",
                  help="add default services to list of services to query (defaults: %s)" % "\n".join(cones))
# todo - an option to read service list from a file; an option to 'add' a service to existing list; an option
#  to display registry dialogue that allows user to select from list of available cones.


# converts a file parameter (which might be relative), url, or myspace refereces to an absolute URI */
def mkURI(input) :
    if  input[0] == '#' : # a short-cut notation for user's myspace - make absolute
        return acr.astrogrid.myspace.getHome() + input[1:] + "/"
    currentDir = os.getcwd() + "/"
    baseURI = urlparse.urlunsplit(["file","",currentDir,"",""])
    return urlparse.urljoin(baseURI,input) + "/" # if input is relative, resolve w.r.t current directory. if absolute, passes throug

#process options
(options, args) = parser.parse_args()
radius = options.radius

if options.service: # a custom list of services was specified.
    if not (options.defaultServices) : #default services not asked for - remove them
        cones = []
    cones += options.service
if options.sourcefile:
    user_votable = mkURI(options.sourcefile)
else :
    user_votable = acr.dialogs.resourceChooser.chooseResource("Select a votable of sources",True)

if options.outputdir:
    output_dir = mkURI(options.outputdir)
#chekc we've get everything we need.
assert user_votable and user_votable != 'NULL', "No sourcelist provided"
assert output_dir and output_dir != 'NULL', "No output directory provided" 
assert cones != [], "No services specified" 

#PARSE POSITIONS
print "Reading sources from " + user_votable
# a slightly idiomatic way of reading a table - but ensures the table gets standardized, cleaned up, etc.
votString = acr.util.tables.convertFromFile(user_votable,"votable","votable");
#parse table.
vot = VOTable.VOTable()
vot.parseText(votString)
#inspect the metadata forthe table.
# handy getColumnIdx will match on any metadata for a field - name, ucd, etc.

nameCol = vot.getColumnIdx('ID_IDENTIFIER')
raCol = vot.getColumnIdx('POS_EQ_RA_MAIN')
decCol = vot.getColumnIdx('POS_EQ_DEC_MAIN')
# verify we've found something
assert nameCol > -1, "Could not locate name column"
assert raCol > -1, "Could not locate RA column"
assert decCol > -1, "Could not locate DEC column"

rows = vot.getDataRows()
print "Number of sources read: %s " % len(rows)

# LOAD REGISTRY METADATA
# Get some metadata for these services.
coneDescriptions = []
for c in cones :
    res = acr.ivoa.registry.getResource(c)
    coneDescriptions.append(res)
    print "Will query %(title)s, results will be named '%(shortName)s.vot" % res

print "Results will be written to " + output_dir
# PERFORM QUERIES
# iterate through each row in the table.
cone = acr.ivoa.cone
for row in rows :
    # parse that row.
    rowData = vot.getData(row)
    srcname = rowData[nameCol].replace(" ","_").replace("\\","p")
    ra = rowData[raCol]
    dec = rowData[decCol]
    print "Querying for %s %s %s " % (srcname,ra,dec),
    saveDir = output_dir + srcname
    if saveDir.startswith("file:/")  : #work-around for AR bug - doesn't create directories on local filesystem.
        d = urlparse.urlparse(saveDir)[2]
        os.makedirs(d)
    
    # construct a query
    for service in coneDescriptions:
        print "%(shortName)s.." % service,
        try :
            query = cone.constructQuery(service['id'],ra,dec,radius)
            saveLocation = saveDir + "/" + service['shortName'] + ".vot"
            cone.executeAndSave(query,saveLocation)
            print "ok ",
        except Exception, e:
            print "FAILED - %s " %  e 
    print "" # onto next service

print "COMPLETED"
# end main script.

