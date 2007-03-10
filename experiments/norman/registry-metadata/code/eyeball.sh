#! /bin/sh -

EYEBALL=/Volumes/Lagrange/Data/tools/eyeball-2.0/lib
JENA=/Volumes/Lagrange/Data/tools/Jena-2.3/lib

#CLASSPATH=
#for j in $EYEBALL/lib/*.jar
#do
#    CLASSPATH=$CLASSPATH:$j
#done

#CLASSPATH=$EYEBALL/eyeball.jar`ls $EYEBALL/*.jar | grep -v eyeball.jar | sed -n '1,$H;${g;s/\n/:/gp;}'`:`ls $JENA/*.jar | sed -n '1,$H;${g;s/\n/:/gp;}'`:/Volumes/Lagrange/Data/tools/eyeball-2.0


#CLASSPATH=`ls $EYEBALL/*.jar | sed -n '1,$H;${g;s/\n/:/gp;}'`

CP=$EYEBALL/eyeball.jar:$EYEBALL/extras.jar:$EYEBALL/antlr-2.7.5.jar:$EYEBALL/commons-logging.jar:$EYEBALL/jena.jar:$EYEBALL/log4j.jar:$EYEBALL/xercesImpl.jar:$EYEBALL/xml-apis.jar:$EYEBALL/iri.jar:$EYEBALL/icu4j_3_4.jar:$EYEBALL/concurrent.jar:$EYEBALL/..

# Following list is from eyeball-2.0/doc/classpath.text
#CLASSPATH=$EYEBALL/antlr-2.7.5.jar:$EYEBALL/arq.jar:$EYEBALL/commons-logging.jar:$EYEBALL/concurrent.jar:$EYEBALL/extras.jar:$EYEBALL/eyeball.jar:$EYEBALL/icu4j_3_4.jar:$EYEBALL/jakarta-oro-2.0.8.jar:$EYEBALL/jena.jar:$EYEBALL/jenatest.jar:$EYEBALL/junit.jar:$EYEBALL/log4j-1.2.12.jar:$EYEBALL/mysql-connector-java-3.1.7-bin.jar:$EYEBALL/napkinlaf.jar:$EYEBALL/pgjdbc2.jar:$EYEBALL/stax-api-1.0.jar:$EYEBALL/wstx-asl-2.8.jar:$EYEBALL/xercesImpl.jar:$EYEBALL/xml-apis.jar
#CLASSPATH=$EYEBALL/antlr-2.7.5.jar:$EYEBALL/arq.jar:$EYEBALL/commons-logging.jar:$EYEBALL/concurrent.jar:$EYEBALL/extras.jar:$EYEBALL/eyeball.jar:$EYEBALL/icu4j_3_4.jar:$EYEBALL/jakarta-oro-2.0.8.jar:$EYEBALL/jena.jar:$EYEBALL/jenatest.jar:$EYEBALL/junit.jar:$EYEBALL/log4j-1.2.12.jar:$EYEBALL/mysql-connector-java-3.1.7-bin.jar:$EYEBALL/napkinlaf.jar:$EYEBALL/pgjdbc2.jar:$EYEBALL/stax-api-1.0.jar:$EYEBALL/wstx-asl-2.8.jar:$EYEBALL/xercesImpl.jar:$EYEBALL/xml-apis.jar:$EYEBALL/iri.jar
#export CLASSPATH

#echo $CLASSPATH
echo "%" java -cp $CP jena.eyeball2 "$@"
exec java -cp $CP jena.eyeball2 "$@"
