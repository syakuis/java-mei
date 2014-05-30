@ECHO OFF

SET "SYS=E:\Service\webapps"
SET "MEI=mei"
SET "LOG=logs\mei.build.debug.log"
SET "BUILD=%SYS%\%MEI%\WEB-INF\mei.build.xml"
SET "ENCODING=UTF-8"
SET "BAT=mei2"

ECHO =======================================================================
ECHO MEI Ver 2.0 BUILD SYSTEM V1.0
ECHO =======================================================================
ECHO MEI DIR : %SYS%\%MEI%
ECHO -----------------------------------------------------------------------
ECHO TOMCAT RELOAD (reload) : TOMCAT ����� �մϴ�.
ECHO INSTALL (install) : MEI ��ġ�մϴ�.
ECHO RE-BUILD-COMPILE (r) : RESOURCE ������ �����ϰ� ��� ������ �ٽ� �����մϴ�.
ECHO BUILD-COMPILE (b) : MEI �����մϴ�.
ECHO COMPILE (c) : MEI �ڹ� �ҽ��� �������մϴ�.
ECHO RESOURCE BUILD (d) : MEI ���ҽ� ������ �缳���մϴ�. (������Ƽ�� , �׼� , ����)
ECHO EXIT (e) : �����մϴ�.
ECHO =======================================================================

SET/P CMD=���� �ɼ��� �Է��ϼ���:
if "%cmd%" == "install" goto install
if "%cmd%" == "r" goto r 
if "%cmd%" == "c" goto c 
if "%cmd%" == "b" goto b 
if "%cmd%" == "d" goto d 
if "%cmd%" == "reload" goto reload 
if "%cmd%" == "e" goto e 
goto def 

:install
call ant -buildfile=%BUILD% -logfile=%SYS%/%LOG% -Dpath=%SYS%/%MEI% install -Dencoding=%ENCODING%
TYPE %SYS%\%LOG%
call %BAT%

:r
call ant -buildfile=%BUILD% -logfile=%SYS%/%LOG% -Dpath=%SYS%/%MEI% r -Dencoding=%ENCODING%
TYPE %SYS%\%LOG%
call %BAT%

:b
call ant -buildfile=%BUILD% -logfile=%SYS%/%LOG% -Dpath=%SYS%/%MEI% b -Dencoding=%ENCODING%
TYPE %SYS%\%LOG%
call %BAT%

:c
call ant -buildfile=%BUILD% -logfile=%SYS%/%LOG% -Dpath=%SYS%/%MEI% c -Dencoding=%ENCODING%
TYPE %SYS%\%LOG%
call %BAT%

:d
call ant -buildfile=%BUILD% -logfile=%SYS%/%LOG% -Dpath=%SYS%/%MEI% d -Dencoding=%ENCODING%
TYPE %SYS%\%LOG%
call %BAT%

:reload
call %CATALINA_HOME%/bin/shutdown.bat
call %CATALINA_HOME%/bin/startup.bat
call %BAT%

:e
exit

:def
call %BAT%

:exit

pause>null