#!/bin/bash
# $Id: testapp.sh,v 1.2 2003/12/07 01:09:48 pah Exp $
#This is a small command line test application

for (( i = 1; i < 40 ; i++ )) ;do
echo "test on stdout" 
echo "test on stderr" 1>&2 
done
