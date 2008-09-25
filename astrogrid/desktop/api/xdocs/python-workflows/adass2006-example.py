#!/usr/bin/env python
# example shown at ADASS 2006
#A simple workflow based around a SIAP query.
import xmlrpclib as x
import sys
import os
import time
prefix = file(os.path.expanduser("~/.astrogrid-desktop")).next().rstrip()
ar = x.Server(prefix + "xmlrpc")

# Resolve an object name to a position.\\
p = ar.cds.sesame.resolve("m32")  # returns a structure\\

# Build a Simple Image Access Protocol (SIAP) query. \\
s = 'ivo://adil.ncsa/targeted/SIA'  # resource ID of the SIAP service \\
q = ar.ivoa.siap.constructQuery(s,p['ra'],p['dec'],1.0)  # returns a query URL

# Execute the SIAP query
r = ar.ivoa.siap.execute(q) #returns an array of row structures \\
print "Rows returned: ", len(r) , "Column Names: ", r[0].keys() 

# Display the query response in a votable viewer \\
me = ar.plastic.hub.registerNoCallBack("script") #register with PLASTIC\\
m = 'ivo://votech.org/votable/loadFromURL'  # message to emit \\
ar.plastic.hub.requestAsynch(me,m,[q]) #sends the message\\

# Save images selected by the query to myspace  \\
home = ar.astrogrid.myspace.getHome() #path to user's myspace home (causes login)
dir = ar.astrogrid.myspace.createChildFolder(home,p['posStr'])  #create a new directory\\
ar.ivoa.siap.saveDatasets(q,dir) #save the images\\

# Save an ascii version of the SIAP query response 
ar.util.tables.convertFiles(q, "votable",dir + "/resp.txt","ascii")
