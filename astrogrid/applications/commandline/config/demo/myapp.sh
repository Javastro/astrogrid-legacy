#!/bin/sh 
#Paul Harrison
#A small demonstration application
# call with ./myapp.sh n1 n2 -op ADD

dlib=`dirname $0` # needs to be called with full path...
# first parse the commandline parameters

num1=$1
shift
num2=$1
shift
until [ ${#*} -eq 0 ]  # Until all parameters used up...
do

 eval ${1:1}=$2
 shift 2

done

dc -e "$num1 $num2 $op p" >answer.txt

