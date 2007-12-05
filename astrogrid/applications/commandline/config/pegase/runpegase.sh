#!/bin/sh -x
#Paul Harrison
#A wrapper to run the pegase code in a way that is reasonably CEA Friendly
# note that this does away with the normal scripts that wrap the fortran executable

dlib=`dirname $0` # needs to be called with full path...
# first parse the commandline parameters

#make links to all of the data files
ls $dlib/*.dat|grep -v my_scenario.dat|xargs -i ln -s {}

until [ ${#*} -eq 0 ]  # Until all parameters used up...
do

 eval ${1:1}=$2
 shift 2

done

$dlib/SSPs << EOD1
$IMF
$LMASS
$UMASS
$SNMODEL
$WINDS
SSP_out
EOD1

scin=./scin.txt

cat > $scin << EOD2
my_scenario.dat
SSP_out_SSPs.dat
$BINFRAC
spectra1.dat
$METALICITY
$INFALL
EOD2

if [ $INFALL == "y" ] ; then
echo $INFALLTIME >> $scin
echo $INFALLMETAL >> $scin
fi

echo $SFSCENARIO >> $scin
echo $CONEVOL >> $scin

if [ $CONEVOL == "n" ] ; then
echo $SMETAL >> $scin
fi
echo $FRACSUB >> $scin
echo $GALWIND >> $scin

if [ $GALWIND == "y" ] ; then
echo $GALWINDAGE >> $scin
fi
cat >> $scin << EOD2e
$NEBEMISS
$GLOBALEXTINCTION
end
EOD2e
$dlib/scenarios < $scin



$dlib/spectra << EOD3
my_scenario.dat
EOD3

$dlib/colors << EOD4
spectra1.dat
colours1.dat
EOD4

if [ -f $dirname/galevot/Galevot.jar ]; then
 mv spectra1.dat spectra1.dat.original; java -jar $dirname/galevot/Galevot.jar spectra1.dat.original spectra1.dat;
else
 echo -e "the galevot converter is not present";exit 1;
fi
