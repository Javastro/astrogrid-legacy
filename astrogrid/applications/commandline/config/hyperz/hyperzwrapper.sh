# $Id: hyperzwrapper.sh,v 1.2 2004/01/16 21:53:48 pah Exp $
#Paul Harrison
#This is a wrapper for hyperz that makes it behave more like the generalized command line application

# parse the parameters

ConfigFile=conf.zphot
appDir=`dirname $0`
#make the local environment the same...
ln -s $appDir/filters
ln -s $appDir/templates

ZPHOT_FILE=$1
shift 1

until [ -z "$1" ]  # Until all parameters used up...
do
    case $1 in
      -FILTERS_RES)
         FILTERS_RES=$2
         shift 2

        ;;
      -FILTERS_FILE)
         FILTERS_FILE=$2
         shift 2

        ;;
      -CATALOG_FILE)
         CATALOG_FILE=$2
         shift 2
        ;;
      -OUTPUT_CATALOG)
         OUTPUT_FILE=$2
         shift 2
        ;;

      -TEMPLATES_FILE)
         TEMPLATES_FILE=$2
         shift 2
        ;;

         *)
        echo "unrecognised parameter " $1
       shift
      
    esac 
done



# first delete any lines with the specified parameters, then add them
sed -e '/^FILTERS_RES/D' -e '/^FILTERS_FILE/D' -e '/^CATALOG_FILE/D' -e '/^OUTPUT_FILE/D' -e '/^TEMPLATES_FILE/D' $ZPHOT_FILE | sed -e "1 a \FILTERS_RES ${FILTERS_RES:=./filters/FILTER.RES}" -e "1 a \FILTERS_FILE $FILTERS_FILE" -e "1 a \CATALOG_FILE $CATALOG_FILE"  -e "1 a \OUTPUT_FILE $OUTPUT_FILE" -e "1 a \TEMPLATES_FILE $TEMPLATES_FILE">$ConfigFile
HYPERZ=$appDir/hyperz
echo $HYPERZ
$HYPERZ << EOD
$ConfigFile
EOD





