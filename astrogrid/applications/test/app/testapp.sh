#!/bin/sh
# $Id: testapp.sh,v 1.6 2003/12/12 21:30:46 pah Exp $
#Paul Harrison
#This is a small command line test application - it attempts to parse the major types of command line arguments
# p1 should be an integer which will be a wait time in seconds
#p2 should be number
p3 should be a filename - the output will be written to that file
p4 should be anything
#test for a large amount of output
for (( i = 1; i < 40 ; i++ )) ;do
echo "test on stdout" 
echo "test on stderr" 1>&2 
done

# parse the parameter values
# input raw
echo "application location = " $0

# treat the first paramters as positional only

P1=$1
shift
P2=$1
shift


# get the rest of the paramters with switch types
until [ -z "$1" ]  # Until all parameters used up...
do
   echo "parameter found " $1
    case $1 in
      -P3)
         P3=$2 
         shift 2
         
        ;;
      Parameter4=*)
         P4=`expr match "$1" '.*[0-9]+*="*\([^"]*\)"*'`
         shift
         
        ;;
         *)
        echo "unrecognised parameter " $1
       shift
      
    esac 
done


# show the parsed values
# sleep to simulate a process that takes a long time...
sleep $P1

echo "parsed parameters.....for $0." >> $P3

for i in `seq 1 10`
do

eval "echo P$i = \$P$i" >> $P3

done
