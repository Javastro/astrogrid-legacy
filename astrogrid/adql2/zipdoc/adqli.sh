# bin/sh
#
# Executes an interactive command line conversation with the ADQL base parser.
#
# Copy required jars to a suitable directory and place its path here...
ADQL=/home/YOUR_HOME_HERE/adql-2008-1-1

CLASSPATH2=.
CLASSPATH2=$CLASSPATH2:$ADQL/astrogrid-adqlparser-utils-2008.1.1.jar
CLASSPATH2=$CLASSPATH2:$ADQL/astrogrid-adqlparser-base-2008.1.1.jar
CLASSPATH2=$CLASSPATH2:$ADQL/astrogrid-adqlbeans-2008.1.1.jar
CLASSPATH2=$CLASSPATH2:$ADQL/xbean-2.3.0.jar
CLASSPATH2=$CLASSPATH2:$ADQL/jsr173_1.0_api-2.3.0.jar
CLASSPATH2=$CLASSPATH2:$ADQL/log4j-1.2.8.jar
CLASSPATH2=$CLASSPATH2:$ADQL/commons-logging-1.0.4.jar
CLASSPATH2=$CLASSPATH2:$ADQL/saxon-6.5.5.jar

CLASSPATH=$CLASSPATH2:$CLASSPATH

#
# Any arguments are passed through...
#
if [ "$#" -eq 0 ]
then
	java -Dlog4j.configuration=file:$ADQL/log4j.properties org.astrogrid.adql.Interactive 
else
	java -Dlog4j.configuration=file:$ADQL/log4j.properties org.astrogrid.adql.Interactive "$*"
fi