rem @echo off

rem ###########################################################
rem # Batch Script
rem # usage : laucher.bat win deploy
rem ###########################################################

set arg1=%1
set arg2=%2

if "%arg1%" equ "" (set arg1="win")
if "%arg2%" equ "" (set arg2="deploy")
echo %arg1% %arg2%

set BASE_DIR="%cd%"

set JAVA_HOME=C:\TZ_IDE\tools\jdk1.7.0_21
set HOME_DIR=C:\TZ_IDE\workspace\tz.deployClient

set ADDED_LIB=%HOME_DIR%\lib

set MAIN_CLASS=tz.launcher.cmd.LaunchCmd
set PATH=%PATH%;%JAVA_HOME%\bin

rem ======= working dir =================
cd %WORK_DIR%

rem ======= CLASSPATH =================

set CLASSPATH=.
FOR %%F IN (%ADDED_LIB%\*.jar) DO call :addcp %%F
goto extlibe

:addcp
set CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe
set CLASSPATH=%CLASSPATH%;%HOME_DIR%\bin

rem ======= =================
rem echo java %MAIN_CLASS% %arg1% %arg2% %3 %4 %5
java %MAIN_CLASS% %arg1% %arg2% %3 %4 %5

echo "====>" %CLASSPATH%

echo "BASE_DIR : %BASE_DIR%"
cd "%BASE_DIR%"
