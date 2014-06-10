java-mei
========

Copyright &copy; 2011 MEI By Syaku. All Rights Reserved.<br>
Powered by Seok Kyun. Choi. 최석균.<br>
MEI (Modularize. Extension. Interaction.)<br/>

※ 개발자 블로그 : http://syaku.tistory.com<br>
※  Github : https://github.com/syakuis/java-mei<br>

※ 개발 언어 : JAVA<br/>

※ 서버환경<br>
JAVA 1.5.x 이상<br>
Apache Tomcat 5.x 이상<br>
MySQL 5.x 이상<br/>


# 소개

*MEI (Modularize Extension Interaction)* 란 모듈기반 방식의 프로그래밍을 제시하는 프레임워크입니다.<br>
모듈기반 방식이란, 하나의 어플리케이션을 기능별로 세분화하여 모듈화하는 방식을 말합니다.<br>
게시판 어플리케이션을 모듈화하였을 때는 기본적으로 게시판 , 댓글 , 파일첨부 , 분류 4가지로 나눠지게 됩니다.<br>
이렇게 세분화된 프로그램을 모듈이라고 하며, 모듈 조각들이 모여 하나의 어플리케이션이 만들어지게 됩니다.<br/>

또한 MEI 는 다양한 UI를 제공하기 위해 레이아웃과 스킨 기능을 제공합니다.<br>
그래서 프로그램과 디자인의 완벽히 분리되어 있습니다.<br>
모듈의 디자인을 원하는 데로 만들어 변경할 수 있는 프레임워크입니다.<br/>

MEI 는 체계적이고 통일된 프로그램 방법론을 제시하며 우수한 오픈 프레임워크를 사용하여
누구나 접하기 용의하게 규격화된 프로그래밍 환경을 제공하는 데 목적을 두고 있습니다.<br/>

MEI는 다음과 같은 오픈 프레임워크를 이용하여 최적의 모듈기반 방식을 구현하였습니다.<br>
 * MVC 모델 2 패턴 스트럿츠2 
 * 템플릿엔진 프리마커
 * 데이터베이스 맵퍼 myBatis
<br/>

# 사용된 라이브러리

## Struts 2.3.1.2
 * commons-beanutils-1.7.0.jar
 * commons-fileupload-1.2.2.jar
 * commons-validator-1.3.1.jar
 * commons-chain-1.2.jar
 * commons-io-2.0.1.jar
 * freemarker-2.3.18.jar
 * ognl-3.0.4.jar
 * commons-collections-3.1.jar -> 3.2
 * commons-lang-2.5.jar -> 2.6
 * javassist-3.11.0.GA.jar
 * struts2-core-2.3.1.2.jar
 * commons-configuration-1.6.jar
 * commons-logging-1.1.1.jar
 * xwork-core-2.3.1.2.jar
 * commons-digester-2.0.jar
 * commons-logging-api-1.1.jar
 * log4j-1.2.16.jar
 * Xerces2 Java 2.11.0 ( xml-apis.jar , xercesImpl.jar )
<br/>

## mybatis 3.0.6
 * mybatis-3.0.6.jar
 * mysql-connector-java-5.1.18-bin.jar
<br/>


## Javascript
 * xml2json
 * jQuery
 * jQuery Action
 * jQuery Ui
 * jQuery simple tree
 * jQuery datepick
 * jQuery lightbox
 * SWFUpload
 * ckeditor
<br/>
