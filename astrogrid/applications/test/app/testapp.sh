#!/bin/sh
# $Id: testapp.sh,v 1.5 2003/12/11 13:23:02 pah Exp $
#Paul Harrison
#This is a small command line test application - it attempts to parse the major types of command line arguments

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
    case $1 in
      -P3)
         P3=$2 
         shift 2
        ;;
      P4=*)
         P4=`expr match "$1" 'P[0-9]+*="*\([^"]*\)"*'`
         shift
        ;;
         *)
        echo "unrecognised parameter " $1
       shift
    esac 
done


# show the parsed values
echo "parsed parameters......"

for i in `seq 1 10`
do

eval "echo P$i = \$P$i"

done
