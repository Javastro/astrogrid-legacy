#! /bin/sh -

MYDIR="`dirname $0`"

J=@JENA.LIB@

#echo java -cp $MYDIR/../classes:$J/jena.jar:$J/commons-logging-1.1.1.jar:$J/xercesImpl.jar:$J/iri.jar:$J/icu4j_3_4.jar:$J/antlr-2.7.5.jar org.eurovotech.bibxml2rdf.GrokXML "$@" >&2
java -cp $MYDIR/../classes:$J/jena.jar:$J/commons-logging-1.1.1.jar:$J/xercesImpl.jar:$J/iri.jar:$J/icu4j_3_4.jar:$J/antlr-2.7.5.jar org.eurovotech.bibxml2rdf.GrokXML "$@"

# # The above is a minimal set, but does seem to be specific to one version of Jena.
# # A cruder approach is:
# CLASSPATH=$MYDIR/../classes
# for j in $J/*.jar; do
#     CLASSPATH=${CLASSPATH}:$j
# done
# export CLASSPATH
# java org.eurovotech.bibxml2rdf.GrokXML "$@"
