#/bin/sh -x
#Paul Harrison
#A wrapper to run the sextractor code in a way that is reasonably CEA Friendly
#note that this is here pricipally to make sure that links to the standard config files are copied locally

dlib=`dirname $0` # needs to be called with full path...
# first parse the commandline parameters

#make links to all of the data files
ls $dlib/config/*|grep -v default.sex|xargs -i ln -s {}
$dlib/sex $*



