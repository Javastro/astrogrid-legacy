@echo off

if not %JAVA_HOME%x==x goto set1

echo JAVA_HOME not set
exit 1

:set1

set JAVA=%JAVA_HOME%\bin\java

if not %OPENJMS_HOME%x==x goto set2

rem OPENJMS_HOME environment variable not set, so default to the parent dir
set OPENJMS_HOME=..

:set2

set CP=%OPENJMS_HOME%\lib\openjms-0.7.5-rc1.jar
set CLASSPATH=%CLASSPATH%;%CP%

%JAVA_HOME%\bin\rmiregistry %1 %2 %3 %4 %5 %6 %7 %8 %9
