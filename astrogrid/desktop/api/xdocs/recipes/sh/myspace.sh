#!/bin/sh
# Noel  Winstanley, AstroGrid, 2006
#example of calling AR functions directly from a shell script to work with myspace.
# uses the REST-like interface.
# (although in many cases, it may be more convenient to call one of the python helper utilities from a script)

# read config file to get endpoint
PREFIX=`cat ~/.astrogrid-desktop`
SERVER=${PREFIX}

#uses curl to do the work - consult manual for possibilties for parameter passing.

#input parameters.
FILE='#votable/shell-example-myspace-results.txt'
DATA='some data'

#check whether a file exists..
if [ `curl -s -d "ivorn=${FILE}" ${SERVER}astrogrid/myspace/exists/plain` = 'false' ]
then
  echo creating file
  curl -s -d "ivorn=${FILE}" ${SERVER}astrogrid/myspace/createFile/plain
else
  echo already exists
fi

#prefereable to fetch parameter data from a file - consult curl manual.
echo saving data to file
curl -s -d "ivorn=${FILE}" -d "data=${DATA}" ${SERVER}astrogrid/myspace/write/plain

echo reading file contents
echo `curl -s -d "ivorn=${FILE}" ${SERVER}astrogrid/myspace/read/plain`