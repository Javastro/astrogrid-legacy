#!/bin/csh
#trivial script to run the python script as the env method is not working for me
# expects to find the config file as the first parameter

set dirname=`dirname $0`
#need to set up the environment.
setenv bc03 $dirname/src/
echo $bc03
source $bc03/.bc_cshrc

python $dirname/runBC03reduced.py $1
