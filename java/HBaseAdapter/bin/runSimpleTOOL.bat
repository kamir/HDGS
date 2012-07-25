@echo off
FOR /R ./lib %%a in (*.jar) DO CALL :AddToPath %%a
ECHO %CLASSPATH%
GOTO :EOF

:AddToPath
SET CLASSPATH=%1;%CLASSPATH%
GOTO :EOF