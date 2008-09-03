#!/bin/sh
# $Id$
#Paul Harrison
#TODO should probably be rewritten in python/perl to make more cross platform...
#This is a small command line test application - it attempts to parse the major types of command line arguments
# p1 should be an integer which will be a wait time in seconds
#p2 should be a filename - the parameters will be echoed to that file.
#p3 should be a filename - the output will be written to that file
#p4 should be anything
#test for a large amount of output

# parse the parameter values
# input raw
echo "application location = " $0

# treat the first paramters as positional only

P1=$1
shift
P2=$1
shift

# get the rest of the paramters with switch types
until [  ${#*} -eq 0 ]  # Until all parameters used up...
do
   echo "parameter found " $1
    case $1 in
      -P3)
         P3=$2
         shift 2

        ;;
      -P9)
         P9=$2
         shift 2

        ;;
      -P10)
         P10=$2
         shift 2

        ;;
      Parameter4=*)
         P4=`expr "$1" : '.*[0-9]+*="*\([^"]*\)"*'`
         shift

        ;;
         *)
        echo "unrecognised parameter " $1
       shift

    esac
done

#check that P1 is greater than 0 - throw and error if not
if [[ $P1 -lt 0 ]]; then
echo "error the wait time is less than 0" 1>&2 
exit 1
fi

if [[ $P10 != "y" ]]; then
echo "error P10 was not y but "$P10 1>&2 
exit 1
fi

# sleep to simulate a process that takes a long time...
sleep $P1

# show the parsed values
if [[ -n $P2 ]] ; then
echo $P2
echo "parsed parameters.....for $0." >> $P2

for i in 1 2 3 4 5 6 7 8 9 10
do

eval "echo P$i = \$P$i" >> $P2

done

else

 echo "P2 not specified" 1>&2
exit 1
fi
if [[ -n $P3 ]]; then
cat $P9 >> $P3
fi
echo "some local test content" > testlocalfile