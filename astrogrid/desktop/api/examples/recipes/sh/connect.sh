#!/bin/sh
# Noel  Winstanley, AstroGrid, 2006
#example of calling AR functions directly from a shell script
# uses the REST-like interface.
# (although in many cases, it may be more convenient to call one of the python helper utilities from a script)

# read config file to get endpoint
PREFIX=`cat ~/.astrogrid-desktop`
SERVER=${PREFIX}

#uses curl to do the work - consult manual for possibilties.

echo plaintext keyword search..
echo `curl -d "keywords=ROSAT Brera" -d "orValues=false" -s ${SERVER}astrogrid/registry/keywordSearchRI/plain`

echo
echo same keyword search, returning html
echo `curl -d "keywords=ROSAT Brera" -d "orValues=false" -s ${SERVER}astrogrid/registry/keywordSearchRI/html`

echo
echo retrive a registry record
echo `curl -d "ivorn=ivo://org.astrogrid/Pegase" -s ${SERVER}astrogrid/registry/getRecord/plain`

echo
echo resolve an identifier
echo `curl -d "ivorn=ivo://uk.ac.le.star/filemanager" -s ${SERVER}astrogrid/registry/resolveIdentifier/plain`
