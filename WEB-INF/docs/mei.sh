#!/bin/bash

SYS="/home/webapps"
MEI="mei"
LOG="logs/$MEI.build.debug.log"
DEBUG="logs/$MEI.debug.log"
BUILD="$SYS/$MEI/WEB-INF/mei.build.xml"
ENCODING="UTF-8"
SH="mei.sh"

echo "==================================================================================================================="
echo "MEI Ver 2.0 BUILD SYSTEM V1.0"
echo "==================================================================================================================="
echo "MEI DIR : $SYS/$MEI"
echo "INSTALL (install) : MEI 설치합니다."
echo "RECOVERY (recovery) : 모든 폴더를 삭제하고 빌드 합니다."
echo "RE-BUILD-COMPILE (r) : RESOURCE 폴더를 제외하고 모두 삭제후 다시 빌드합니다."
echo "BUILD-COMPILE (b) : MEI 빌드합니다."
echo "COMPILE (c) : MEI 자바 소스만 컴파일합니다."
echo "RESOURCE BUILD (d) : MEI 리소스 파일을 재설정합니다. (프로퍼티스 , 액션 , 맵퍼)"
echo "OTHER OPTION (state) : 톰캣 프로세서 정보를 확인합니다."
echo "OTHER OPTION (logd) : MEI 로그를 확인합니다."
echo "EXIT (e) : 종료합니다."
echo "=================================================================================================================="

read -p "빌드 옵션을 입력하세요:" cmd

case "$cmd" in

"install")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI install -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"recovery")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI recovery -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"r")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI r -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"c")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI c -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"b")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI b -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"d")
ant -buildfile $BUILD -logfile $SYS/$LOG -Dpath $SYS/$MEI d -Dencoding $ENCODING
cat $SYS/$LOG
. $SH
;;

"state")
ps -ef|grep java | grep tomcat
. $SH
;;

"logd")
tail -f $DEBUG
;;

"e")
exit
;;

"*")
echo "옵션을 정확하게 입력하세요."
;;

esac


