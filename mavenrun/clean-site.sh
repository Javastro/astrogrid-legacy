OLDDIR=$PWD

PROJECT_NAME_2GO=$1

DOCSLOC=/var/www/www/maven/build

echo "Cleaning old site from $DOCSLOC "
cd $DOCSLOC
rm -fr $PROJECT_NAME_2GO
echo "deleting $DOCSLOC/$PROJECT_NAME_2GO"

echo "back to start dir: $OLDDIR"
cd $OLDDIR
echo
