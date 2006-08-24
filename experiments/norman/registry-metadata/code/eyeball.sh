#! /bin/sh -

EYEBALL=/Volumes/Lagrange/Data/tools/eyeball-2.0

CLASSPATH=
for j in $EYEBALL/lib/*.jar
do
    CLASSPATH=$CLASSPATH:$j
done
export CLASSPATH

exec java jena.eyeball "$@"
