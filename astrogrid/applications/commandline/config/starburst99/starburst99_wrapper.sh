#/bin/sh
#Paul Harrison
#A wrapper to run the starburst99 code in a way that is reasonably CEA Friendly
# note that this does away with the normal scripts that wrap the fortran executable

dlib=`dirname $0` # needs to be called with full path...
# first parse the commandline parameters

until [ ${#*} -eq 0 ]  # Until all parameters used up...
do

 eval ${1:1}=$2
 shift 2

done

cat > fort.1 <<EOD
MODEL DESIGNATION:                                           [NAME]
$NAME
CONTINUOUS STAR FORMATION (>0) OR FIXED MASS (<=0):          [ISF]
$ISF
TOTAL STELLAR MASS [1E6 M_SOL] IF 'FIXED MASS' IS CHOSEN:    [TOMA]
$TOMA
SFR [SOLAR MASSES PER YEAR] IF 'CONT. SF' IS CHOSEN:         [SFR]
$SFR
IMF EXPONENT (2.35 = SALPETER):                              [ALPHA]
$ALPHA
UPPER MASS LIMIT FOR IMF [SOLAR MASSES]:                     [UPMA]
$UPMA
LOWER MASS LIMIT FOR IMF [SOLAR MASSES]:                     [DOMA]
$DOMA
SUPERNOVA CUT-OFF MASS [SOLAR MASSES]:                       [SNCUT]
$SNCUT
BLACK HOLE CUT-OFF MASS [SOLAR MASSES]:                      [BHCUT]
$BHCUT
METALLICITY + TRACKS:                                        [IZ]
92-94 STD. MASS-LOSS: 11=0.001; 12=0.004; 13=0.008; 14=0.020; 15=0.040
92-94 HIGH MASS-LOSS: 21=0.001; 22=0.004; 23=0.008; 24=0.020; 25=0.040
$IZ
WIND MODEL (0=MAEDER; 1=EMP.; 2=THEOR.; 3=ELSON):            [IWIND]
$IWIND
INITIAL TIME [1.E6 YEARS]:                                   [TIME1]
$TIME1
TIME STEP [1.e6 YEARS]:                                      [STEP]
$STEP
LAST GRID POINT [1.e6 YEARS]:                                [TMAX]
$TMAX
SMALL (=0) OR LARGE (=1) MASS GRID; 
ISOCHRONE ON  LARGE GRID (=2) OR FULL ISOCHRONE (=3):        [JMG]
$JMG
LMIN, LMAX (ALL=0):                                          [LMIN,LMAX]
$LMIN,$LMAX
TIME STEP FOR PRINT OUT OF SYNTH.SPECTRUM AND LINE [1.e6YR]: [TDEL]
$TDEL
ATMOSPHERE: 1=PLA, 2=LEJ, 3=LEJ+SCH, 4=LEJ+HIL, 5=PAU+HIL:   [IATMOS]
$IATMOS
METALLICITY OF THE UV LINE SPECTRUM: (1=SOLAR, 2=LMC/SMC)    [ILINE]
$ILINE
RSG FEATURE: MICROTURB. VEL (1-6), SOL/NON-SOL ABUND (0,1)   [IVT,IRSG]
$IVT,$IRSG
OUTPUT FILES (NO<0, YES>=0)                                  [IO1,...]
+1,+1,+1,+1,+1,+1,+1,+1,+1,+1,+1,+1,+1,+1
EOD

#link up standard models
	# Tracks + Spectral type calibration:
if [ ! -e tracks ] ; then
 ln -s $dlib/tracks
fi
	# Atmosphere models:
if [ ! -e lejeune ] ; then
ln -s $dlib/lejeune 
fi
	# Spectral libraries:
if [ ! -e auxil ] ; then
ln -s $dlib/auxil
fi



# run the program

$dlib/galaxy_linux
