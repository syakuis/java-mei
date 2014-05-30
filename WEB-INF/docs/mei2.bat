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
ECHO TOMCAT RELOAD (reload) : TOMCAT 재시작 합니다.
ECHO INSTALL (install) : MEI 설치합니다.
ECHO RE-BUILD-COMPILE (r) : RESOURCE 폴더를 제외하고 모두 삭제후 다시 빌드합니다.
ECHO BUILD-COMPILE (b) : MEI 빌드합니다.
ECHO COMPILE (c) : MEI 자바 소스만 컴파일합니다.
ECHO RESOURCE BUILD (d) : MEI 리소스 파일을 재설정합니다. (프로퍼티스 , 액션 , 맵퍼)
ECHO EXIT (e) : 종료합니다.
ECHO =======================================================================

SET/P CMD=빌드 옵션을 입력하세요:
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