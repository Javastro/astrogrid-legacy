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

if %1x==x goto usage
if %2x==x goto usage

set POLICY_FILE=%OPENJMS_HOME%\src\etc\openjms.policy

set CP=%OPENJMS_HOME%\lib\xerces-2.3.0.jar
set CP=%OPENJMS_HOME%\lib\$XSLP$;%CP%
set CP=%CP%;%CLASSPATH%


%JAVA% -Djava.security.policy=%POLICY_FILE% -classpath %CP% com.kvisco.xsl.XSLProcessor -s convertcfg.xsl -i %1 -o %2

exit %ERRORLEVEL%

:usage
echo.
echo Converts an existing OpenJMS configuration to the latest release format
echo.
echo usage: convertcfg [input] [output]
echo  input  - the configuration file to convert
echo  output - the output file to create
echo.
exit 1
