@echo off

IF "%JAVA_HOME%"=="" SET LOCAL_JAVA=java
IF NOT "%JAVA_HOME%"=="" SET LOCAL_JAVA=%JAVA_HOME%\bin\java



@rem Define JS_HOME if not set
if "%JS_HOME%" == "" set JS_HOME=%DIRNAME%..


@rem dir /b "%JS_HOME%\squirrel-sql.jar" > temp.tmp
@rem FOR /F %%I IN (temp.tmp) DO CALL "%JS_HOME%\addpath.bat" "%JS_HOME%\%%I"
set TMP_CP="%JS_HOME%\squirrel-sql.jar"

dir /b "%JS_HOME%\lib\*.*" > temp.tmp
FOR /F %%I IN (temp.tmp) DO CALL "%JS_HOME%\addpath.bat" "%JS_HOME%\lib\%%I"

SET TMP_CP=%TMP_CP%;"%CLASSPATH%"

@rem Run with a command window.
"%LOCAL_JAVA%" -cp %TMP_CP% org.mozilla.javascript.tools.shell.JSConsole %TMP_PARMS% %1 %2 %3 %4 %5 %6 %7

