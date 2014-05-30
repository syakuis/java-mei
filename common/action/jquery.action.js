/**
 * jQuery Action Library
 *
 * Copyright (c) Seok Kyun. Choi. 최석균
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
 *
 * registered date 20080327
 * http://syaku.tistory.com
 */


/**
* jQuery Action Core
* @file ja.core.js
* @depends jQuery 1.3.2+
* @brief jQuery Action 공통 라이브러리

 * Copyright (c) 2010 Seok Kyun. Choi. 최석균
 * GNU Lesser General Public License
 * http://www.gnu.org/licenses/lgpl.html
*/

/*
2013-06-21 Action prepare 메서드 추가
                   setDefaults 수정
2013-06-27 대상 엘리먼트가 없을 경우 필터링 무시함.
2013-06-29 jaFilter message ( alert 메세지 컨트롤 메서드 추가 )
2013-07-01 jaFilter 메서드 중복 삽입된 문제 처리 (왜이런;;)
2013-07-14 setValue 셀렉트 박스 중에 선택값이 없을 경우 선택안되게 수
           getElementType 엘리먼트 형식 리턴함.
*/

;jQuery.ja || (function($) {

  function ja() {
    this.version = '2.0.7';
    this.lang = 'ko';
    this.regional = { };

    this.regx = {
      'user_id' : function(val) {
        var reg = /^[a-zA-Z]+([_0-9a-zA-Z]+)*$/;
        return reg.test(val);
      }
      , 'dot_ip' : function(val) {
        reg = /^([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$/; 
        return reg.test(val);
      }
      , 'http_url' : function(val) {
        var reg = /^(http|https|ftp|mms|news|rss):\/\/[0-9a-z-]+(\.[_0-9a-z-\/\~]+)+(:[0-9]{2,4})*$/;
        return reg.test(val);
      }
      , 'url' : function(val) {
        var reg = /^[^(http|https|ftp|mms|news|rss):\/\/][0-9a-z-]+(\.[_0-9a-z-\/\~]+)+(:[0-9]{2,4})*$/;
        return reg.test(val);
      }
      , 'hyphen_contact' : function(val) {
        var reg = /^([0-9]+)*\-/; 
        return reg.test(val);
      }
      , 'replay_contact' : function(val) {
        var regExp = /([0]{3,4}|[1]{3,4}|[2]{3,4}|[3]{3,4}|[4]{3,4}|[5]{3,4}|[6]{3,4}|[7]{3,4}|[8]{3,4}|[9]{3,4})/;
        return !regExp.test(str);
      }
      , 'number' : function(val) {
        return !isNaN(val);
      }
      , 'en' : function(val) {
        var reg = /^[a-zA-Z]*$/; 
        return reg.test(val);
      }
      , 'en_number' : function(val) {
        var reg = /^[a-zA-Z0-9]*$/; 
        return reg.test(val);
      }
      , 'en_number_mix' : function(val) {
        var reg = /[a-zA-Z]+/; 
        var reg2 = /[0-9]+/; 

        if (reg.test(val) && reg2.test(val)) {
          return true;
        } else {
          return false;
        }
      }
      , 'email' : function(val) {
        var reg = /^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+\.[\.0-9a-zA-Z-]+$/;
        return reg.test(val);
      }
      , 'notnull' : function(val) {
        return !$.ja.isEmpty(val);
      }

    };
  }

  // settings
  $.extend(ja.prototype, {
    /**
    * @func xmldoc(xmlpath) return object
    * @brief xml 문서를 브라우저에 맞게 불러온다.
    */
      xmldoc : function(xml) {
      var obj;

      if ($.browser.msie || $.browser.mozilla) {
        obj = this.xmlDocument();
        obj.async = false;
        obj.load(xml);
        return obj.documentElement;
      } else {
        obj = this.xmlHttpRequest();
        obj.open('GET',xml,false);
        obj.send(null);
        return obj.responseXML.documentElement;
      }

      return null;
    }

    /**
    * @func xmlDocument() return object
    * @brief XMLDOM 객체를 생성한다.
    */
    , xmlDocument : function() {
      if (document.implementation && document.implementation.createDocument) {
        return document.implementation.createDocument('','',null);
      } else if (typeof ActiveXObject != 'undefined') {
        try {
          return new ActiveXObject("Msxml2.DOMDocument");
        }
        catch (e) {
          return new ActiveXObject("Msxml.DOMDocument");
        }
      }
      return null;
    }

    /**
    * @func xmlHttpRequest() return object
    * @brief XMLHTTP 객체를 생성한다.
    */
    , xmlHttpRequest : function() {
      if (window.ActiveXObject) {
        try {
          return new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {
          return new ActiveXObject("Msxml.XMLHTTP");
        }
      }
      else if (window.XMLHttpRequest) { return new XMLHttpRequest(); }
      else { return null; }
    }

    /**
     * @func replaceAll(value,value,value) return value
     * @brief 문자열 치환
     */
    , replaceAll : function(val,str,change) {
      try { return val.split(str).join(change); }
      catch (e) { alert(e); }
    }

    /**
     * @func isEmpty(value) return boolean
     * @brief 빈값 여부 확인
     */
    , isEmpty : function(val) {
      var is_type = typeof val;

      if(val == null || val == undefined) { return true; }
      else if(is_type == 'string') {
        val = this.replaceAll(val,/\s+/g,'');
        if(val == '') { return true; }
      }

      return false;
    }

    /**
     * @func defString(value,value) return value
     * @brief 빈값을 원하는 값으로 치환
     */
    , defString : function(val,str) {
      return (this.isEmpty(val)) ? str : val;
    }

    /**
    * @func params2json(string) return json
    * @brief 파라메터형 문자열을 받아 json 으로 변형하여 반환한다. name=value 를 { name : name , value : value }
    */
    , params2json : function(query) {
      query = query.replace(/^&|\?/,'').split('&');
      var cnt = query.length;

      var obj = [];

      for (var i = 0; i < cnt; i++) {
        var pattern = query[i].split('=');
        obj[i] = {name : pattern[0] , value : unescape(pattern[1])};
      }

      return obj;
    }

    /**
    * @func getType(string) return string
    * @brief 크로스브라우징을 위해 관련된 INPUT TYPE을 하나로 통합한다.
    */
    , getCrossType : function(type) {
      switch(type) {
        case 'select' :
        case 'select-one' :
        case 'select-multiple' :
           return 'select';
        break;
        case 'checkbox' :
          return 'checkbox';
        break;
        case 'radio' :
          return 'radio';
        break;
        case 'text' :
        case 'hidden' :
        case 'password' :
        case 'textarea' :
        case 'number' :
          return 'text';
        break;
        default :
          return type;
        break;
      }
    }

    /**
    * @func getElement(string | element) return element
    * @brief document 의 엘리먼트를 찾아 반환한다.
    */
    , getElement : function(target) {
      return (typeof target == 'string') ? $(target) : target;
    }
    
    , getElementType : function(target) {
      target = this.getElement(target);
      var type = target.attr('type');
      
      if (type == undefined) {
        if (target.is('select')) {
          type = 'select';
        }
      } else {
        type = this.getCrossType(type);
      }
      
      return type;
    }

    /**
    * @func getValue(string | element) return string | array
    * @brief 엘리먼트의 값을 알아온다.
    */
    , getValue : function(target) {
      var value = null;

      target = this.getElement(target);
      switch (target.attr('type')) {

        case 'checkbox' :
        case 'radio' :
          var count = target.length;

          if (this.isEmpty(count)) {
            if (target.attr('checked')) {
              value = target.val();
            }
          } else {
            value = [];
            for (var i = 0; i < count;i++) {
              if (target[i].checked) {
                value.push(target[i].value);
              }
            }

          }

        break;

        default : value = target.val(); break;
      }

      return value;
    }

    /**
    * @func setValue(string | element , value | array)
    * @brief 엘리먼트의 값을 삽입한다.
    */
    , setValue : function(target,value) {
      target = this.getElement(target);
      var type = this.getElementType(target);
      switch (type) {
        case 'checkbox' :
        case 'radio' :
          var count = target.length;
          if (this.isEmpty(count)) {
            if (target.val() == value) {
              target.attr('checked',true);
            } else {
              target.attr('checked',false);
            }
          } else {
            var val_count = value.length;
            for (var i = 0; i < count;i++) {

              if (!this.isEmpty(val_count) && target.attr('type') == 'checkbox') {

                for (var x = 0; x < val_count;x++) {
                  if (target[i].value == value[x]) {
                    target[i].checked = true;
                  }
                }


              } else {
                if (target[i].value == value) {
                  target[i].checked = true;
                }

              }

            }

          }

        break;
        
        case 'select' :
          if ( jQuery('option',target).is('[value=' + value + ']')) target.val(value); 
        break;

        default : target.val(value); break;
      }

    }

    /**
    * @func setValues(element | string ,object)
    * @brief 여러 엘리먼트의 값을 삽입한다. (다시정의 : 20100917)
    */
    , setValues : function(form,values) {
      form = this.getElement(form);

      var reg = /^#/;
      $.each(values, function(i, field){
        var name = field.name;
        name = (!reg.test(name)) ? '#' + name : name;

        var target = $(name,form);
        $.ja.setValue(target,field.value);
      });

    }


    /**
    * @func isRegnum(value) return boolean
    * @brief 대한민국 주민등록번호가 올바른지 판단한다.
    */
    , isRegnum : function(val) {
      val = val.replace(/[^0-9]+/g,'');

      var count = 1;
      var total = 0;

      for (var i =0; i < 12; i++) {
          if (count > 8) {
            count = 1;
            count++;
          } else {
            count++;
          }

          total +=parseInt(val.charAt(i))*count;
      }

      var mod = total%11;
      var check = 11 - mod;

      if (check == 10) { check = 0; }
      if (check == 11) { check = 1; }

      if (check == parseInt(val.charAt(12))) { return true; }
      else { return false; }
    }

    /**
    * @func isSelected( string|element , min|max , number|* ) return boolean
    * @brief 엘리먼트 선택수와 선택 요구수를 비교한다.
    */
    , isSelected : function(ele,mode,ea) {
      try {

        ele = this.getElement(ele);
        var count = ele.length;

        // 관련 엘리먼트의 조건이 성립하는 수
        var num = 0;

        for (var i = 0; i < count;i++) {
          var type = this.getCrossType(ele[i].type);
          switch (type) {
            case 'checkbox' :
            case 'radio' :
              if (ele[i].checked == true) { num++; }
            break;
            case 'select' :
              if (ele[i].selected == true) { num++; }
            break;
            default :
              if (!this.isEmpty(ele[i].value)) { num++; }
            break;
          }
        }

        switch (mode) {
          case 'min' :
            if (ea == '*' && count == num) { return true; }
            if (parseInt(ea) <= num) { return true; }
          break;
          case 'max' :
            if (ea == '*' && count == num) { return true; }
            if (parseInt(ea) >= num) { return true; }
          break;
        }

        return false;

      } catch (e) {
        alert('isSelected : ' + e);
        return false;
      }

    }

    /**
    * @func isCount(min|max|min_length|max_length , number , string ) return boolean
    * @brief 문자열 최대 길이와 최소길이를 판단한다.
    */
    , isCount : function(mode,num,val) {
      try {

        val = new String(val); 
        var len_val = val.length;
        if (len_val == 0) { return true; }

        num = parseInt(num);

        if ((len_val < num && (mode == 'min_length' || mode == 'min') ) || (len_val > num && (mode == 'max_length' || mode == 'max' ) ) )  { return false; }
        else { return true; }
      } catch (e) {
        alert('isCount : ' + e);
        return false;
      }

    }

    /**
    * @func href(value) event
    * @brief url 로 이동한다.
    */
    , href : function(url) {
      document.location.href = url;
    }

    /**
    * @func isReg(string,value) return boolean
    * @brief 정규표현식을 이용하여 문자열을 판단한다.
    */
    , isReg : function(filter,val) {
      try {
        var o = eval("$.ja.regional.regx." + filter);
        return o(val);
      } catch (e) { return alert(e); true; }
    }

    /**
    * @func date(string,string) return string
    * @brief 문자열 날짜를 임의의 포맷으로 변환후 반환한다.
    * $1 년 $2 월 $3 일 $4 시 $5 분 $6초
    */
    , date : function(format,date) {
      date = date.replace(/[-_:.\/\s]/,'-');
      var reg = /([0-9]{0,4})[-]{0,1}([0-9]{0,2})[-]{0,1}([0-9]{0,2})\s{0,1}([0-9]{0,2})[-]{0,1}([0-9]{0,2})[-]{0,1}([0-9]{0,2})(.*)$/;
      return date.replace(reg,format);
    }

    , setCookie : function(name, value, expires, path, domain, secure) {
      var todayDate = new Date();
      todayDate.setDate(todayDate.getDate() + expires);

      var curCookie = name + "=" + escape(value) +
      ((expires) ? "; expires=" + todayDate.toGMTString() : "") +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      ((secure) ? "; secure" : "");
      document.cookie = curCookie;
    }

    , getCookie : function(name) {
      var dc = document.cookie;
      var prefix = name + "=";
      var begin = dc.indexOf("; " + prefix);
      if (begin == -1) {
       begin = dc.indexOf(prefix);
       if (begin != 0) return null;
      } else
       begin += 2;
      var end = document.cookie.indexOf(";", begin);
      if (end == -1)
       end = dc.length;
      return unescape(dc.substring(begin + prefix.length, end));
    }
    

    /**
    * @func dateFormat(string,date) return string
    * @brief 날짜형식을 포맷으로 변환후 반환한다.
    yy - year (two digit)
    yyyy - year (four digit)
    m - month of the year (no leading zero)
    mm - month of the year (two digit)
    M - month name short
    MM - month name long
    d - day of the month (no leading zero)
    dd - day of the month (two digit)
    D - day name short
    DD - day name long
    */
    , dateFormat : function(format,date) {
      var monthNames = $.ja.regional.action.date.month;
      var monthNamesShort = $.ja.regional.action.date.month_short;
      var dayNames = $.ja.regional.action.date.day;
      var dayNamesShort = $.ja.regional.action.date.day_short;

      date = date || null;

      var reg = '';
      if (date == null) {
        date = new Date();
      }
      
      var year,month,day;

      reg = /yyyy/g;
      if (reg.test(format)) {
        format = format.replace(reg,date.getFullYear());
      }

      reg = /yy/g;
      if (reg.test(format)) {
        year = String(date.getFullYear()).substring(2);
        format = format.replace(reg,year);
      }

      reg = /mm/g;
      if (reg.test(format)) {
        month = this.leftPad(date.getMonth()+1,2,'0');
        format = format.replace(reg,month);
      }

      reg = /m/g;
      if (reg.test(format)) {
        month = date.getMonth()+1;
        format = format.replace(reg,month);
      }

      reg = /MM/g;
      if (reg.test(format)) {
        month = monthNames[date.getMonth()];
        format = format.replace(reg,month);
      }

      reg = /M/g;
      if (reg.test(format)) {
        month = monthNamesShort[date.getMonth()];
        format = format.replace(reg,month);
      }

      reg = /dd/g;
      if (reg.test(format)) {
        day = this.leftPad(date.getDate(),2,'0');
        format = format.replace(reg,day);
      }

      reg = /d/g;
      if (reg.test(format)) {
        day = date.getDate();
        format = format.replace(reg,day);
      }

      reg = /DD/g;
      if (reg.test(format)) {
        day = dayNames[day.getDay()];
        format = format.replace(reg,day);
      }

      reg = /D/g;
      if (reg.test(format)) {
        day = dayNamesShort[day.getDay()];
        format = format.replace(reg,day);
      }

      return format;
    }

    /**
    * @func hyphen(element,contact|zipcode|jumin|calendar,string)
    * @brief 자동으로 임의의 문자열을 삽입한다.
    */
    , hyphen : function(ele,mode,str) {
      ele = this.getElement(ele);
      if (this.isEmpty(str)) str = '-';
      var val = ele.val();
      val = this.replaceAll(val,str,'');
      var len = val.length;

      var reg;

      if (len > 4) {

        switch (mode) {
          case 'contact' :
            reg = /(^02|031|032|033|041|042|043|051|052|053|054|055|061|062|063|064|060|070|080|010|011|012|013|014|015|016|017|018|019)([0-9]{3,4})([0-9]{1,4})$/;
            if (reg.test(val)) { 
              reg.exec(val,"g");
              var val1 = RegExp.$1;
              var val2 = RegExp.$2;
              var val3 = RegExp.$3;
              var val_len = val2 + val3;
              if (val_len.length == 7) {
                val = val1 + str +  val_len.substring(0,4) + str +  val_len.substring(4,7);
              } else {
                val = val1 + str +  val2 + str +  val3;
              }
            }
          break;
          case 'zipcode' : 
            reg = /(^[0-9]{3})([0-9]{3})$/;
            if (reg.test(val)) {
              reg.exec(val,"g");
              val = RegExp.$1 + str + RegExp.$2;
            }
          break;
          case 'jumin' : 
            reg = /(^[0-9]{6})([0-9]{7})$/;
            if (reg.test(val)) {
              reg.exec(val,"g");
              val = RegExp.$1 + str + RegExp.$2;
            }
          break;
          case 'calendar' : 
            reg =  /(^[0-9]{4})([0-9]{1}[0-2]{0,1})([0-3]{0,1}[0-9]{1})$/;
            if (reg.test(val)) {
              reg.exec(val,"g");
              var val1 = RegExp.$1;
              var val2 = RegExp.$2;
              var val3 = RegExp.$3;
              var val_len = val2 + val3;
              if (parseInt(val2) > 12) { val2 = val_len.substring(0,1); val3 = val_len.substring(1,val_len.length);}
              if (val2.length < 2) { val2 = '0' + val2; }
              if (val3.length < 2) { val3 = '0' + val3; }
              val = val1 + str + val2 + str + val3;
            }
          break;
        }

        ele.val(val);
      }
    }

    /**
    * @func checked(element,boolean)
    * @brief 모든 체크박스를 선택한다.
    */
    , checked : function(target,is,obj) {
      if (this.isEmpty(is) || is == false) {
        if (confirm($.ja.regional.action.question.checkbox_change['#cdata'])) { is = true; }
      }

      var cnt = $(target).length;
      $(target).each(function (i) {
        if (!$.ja.isEmpty(obj)) {
          this.checked = $(obj).attr("checked");
        } else {
          (this.checked == true && is) ? this.checked = false : this.checked = true;
        }

      });
    }

    /**
    * @func result(object)
    * @brief 결과를 받아 처리한다.
    */
    , result : function(ja) {
      if (!this.isEmpty(ja.message)) { 

        switch (ja.display) {
          case 'alert' :
            alert(ja.message);
          break;
          case 'right-span' : 
            var show = ja.target.parent();

            if (!$('span',show).is('span')) {
              ja.target.after(" <span class='right-span'>" + ja.message + "</span>");
            } else {
              $('span',show).text(ja.message);
            }
          break;
          default : 
            if (/^(\#|\.|\:)/g.test(ja.display)) {
              $(ja.display).text(ja.message).show();
            }
          break;
        }
      }

      switch (ja.action) {
        case 'focus' :
          if (ja.target.attr('type') != 'hidden') { ja.target.focus(); }

        break;
        case 'url' :
          location.href = ja.source;
        break;
        case 'javascript' :
          eval(ja.source);
        break;
      }
    }

    , get_result : function(options) {
      var xml = options.data;
      var afterSend =options.afterSend;

      var error = $.ja.defString($(xml).find('error').text(),"false");
      var ja = {
          message : $(xml).find('message').text() 
        , error : error
        , action : $(xml).find('action').text() 
        , source : $(xml).find('source').text() 
        , target : $(xml).find('target').text()
        , display : $(xml).find('display').text() 
      };
      
      var con = true;

      if (error == 'false') {
        if ($.isFunction(afterSend)) {
          con = afterSend(xml); // 반환이 false 인 경우 종료.
        }
      }

      if (con) {
        $.ja.result(ja);
      }

      return eval(error);

    }

    /**
    * @func nudeHtml(string) return string
    * @brief HTML 소스에서 문자열을 제외하고 모두 지워서 반환한다.
    */
    , nudeHtml : function(source) {
      var reg = /(<{1}\/{0,1})[^<>]*(\/{0,1}>{1})/ig;
      return source.replace(reg,'');
    }

    /**
    * @func leftPad(string,int,char) return string
    * @brief 반복수만큼 왼쪽에 문자를 채워서 반환한다.
    */
    , leftPad : function(str,repeat,chr) {
      var ext = '';
      var count = String(str).length;

      count = (repeat+1) - count;
      if (count <= 0) { return str; }

      for (var i = 1; i < count; i++) {
        ext += chr;
      }

      return ext + str;
    }

    /**
    * @func offset(int,int,layer|popup) return object
    * @brief 해당 엘리먼트를 기준으로 offset 값을 반환한다.
    */
    , offset : function(w,h,m) {
      var width = 0;
      var height = 0;

      var obj = {width:0 , height:0 , scrollTop:0 , scrollLeft:0 , scrollWidth:0 , scrollHeight:0 , top:0 , left:0};

      // 현재 화면의 크기
      var dom = document.documentElement;
      var doc = $(document);

      try {

        if ($.browser.msie) {
          obj.width = parseInt(dom.offsetWidth);
          obj.height = parseInt(dom.offsetHeight);
        } else {
          obj.width = parseInt(window.innerWidth);
          obj.height = parseInt(window.innerHeight);
        }

      } catch (e) { // ie6
        obj.width = doc.width();
        obj.height = doc.css('height','100%').height();
      }

      // 스크롤의 위치
      if ($.browser.msie && Number($.browser.version) < 9) {
        obj.scrollLeft = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
        obj.scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
      } else {
        obj.scrollLeft = parseInt(doc.scrollLeft());
        obj.scrollTop = parseInt(doc.scrollTop());
      }

      // 현재 화면의 전체 크기 (스크롤 포함)
      if ($.browser.msie) { // 20 오차발생
        obj.scrollWidth = parseInt(doc.width()) - 20;
      } else {
        obj.scrollWidth = parseInt(doc.width());
      }
      obj.scrollHeight = parseInt(doc.height());

      // 모니터 중앙에 위치 값
      if (m == 'layer') {
        // 레이어창
        if (w > 0) obj.left = (obj.width - w)/2 + obj.scrollLeft;
        if (h > 0) obj.top = (obj.height - h)/2 + obj.scrollTop;
      } else {
        // 팝업창
        if (w > 0) obj.left = parseInt((obj.width - w)/2);
        if (h > 0) obj.top = parseInt((obj.height - h)/2);
      }

      if (obj.left < 0){ obj.left = -obj.left; }
      if (obj.top < 0){ obj.top = -obj.top; }

      return obj;
    }

    /**
    * @func popup(options)
    * @brief 팝업창을 호출한다.
    */
    , popup : function(options) {
      option = {
          url : null
        , name : null
        , width : 100
        , height : 100
        , scrollbars : 'no'
        , resizable : 'no'
        , toolbar : 'no'
        , directories : 'no'
        , top : 0
        , left : 0
        , status : 'no'
        , menubar : 'no'
        , center : false // 팝업창 호출을 중간에 할 경우
      };

      $.extend(option,options);

      if (!this.isEmpty(option.url)) { 

        // 위치 측정 후 top 과 left 적용
        if (option.center == true) {
          var xy = this.offset(option.width,option.height,"popup");
          option.top = xy.top;
          option.left = xy.left;
        }

        window.open(
            option.url
          , option.name
          , "width=" + option.width 
          + ",height=" + option.height 
          + ",scrollbars=" + option.scrollbars 
          + ",resizable=" + option.resizable 
          + ",toolbar=" + option.toolbar 
          + ",directories=" + option.directories 
          + ",status=" + option.status 
          + ",menubar=" + option.menubar 
          + ",top=" + option.top 
          + ",left=" + option.left
        );

      }
    }

    /**
    * @func layer(options)
    * @brief 레이어화면을 호출한다.
    */
    , layer : function(options) {
      o = {
          target : 'ja' // 레이어 id 명
        , width : 100
        , height : 100
        , top : 0
        , left : 0
        , type : 'iframe' // iframe | html
        , source : ''
        , open : true // 활성화 여부
        , center : false  // 레이어 위치를 중간에 할 경우
        , bgshow : true // 배경 활성화 여부
        , bgcolor : '#aaaaaa' // 배경색
        , reload : false // 프레임url 새로고침 여부
      };

      $.extend(o,options);

      var body = $(document.body);
      var ele = "#" + o.target;
      var t = $(ele,body);
      var b = $('#layer_bg',body);
      var ifr = null;
      var xy = this.offset(o.width,o.height,"layer");

      if (!b.is('#layer_bg')) {
        var w = xy.scrollWidth;
        var h = xy.scrollHeight;

        body.append("<div id='layer_bg' class='layerBg'></div>");
        b = $('#layer_bg',body);
        b.css('width',w);
        b.css('height',h);
        b.css('background',o.bgcolor);
      }

      b.hide();

      if (!t.is(ele)) {
        body.append("<div id='" + o.target + "'></div>");
        t = $(ele,body);
        t.css('position','absolute');
        t.css('width',o.width);
        t.css('height',o.height);
        t.css('z-index','1002');

        if (o.type == 'iframe') {
          t.append("<iframe id='ifr_" + o.target + "' frameborder='0' width='100%' height='100%' src='" + o.source + "'></iframe>");
        } else {
          t.append(o.source);
        }
      }

      t.hide();

      if (o.open) {
        if (o.reload) $('#ifr_' + o.target , t).attr('src',o.source);
        if (o.bgshow) b.show();

        // 위치 측정 후 top 과 left 적용
        if (o.center == true) {
          o.top = xy.top;
          o.left = xy.left;
        } else {
          o.top = parseInt(xy.scrollTop);
          o.left = parseInt(xy.scrollLeft);
        }

        t.css('top',o.top);
        t.css('left',o.left);

        t.show();
      }
    }

    , url2Json : function(url) {
      var domain = 'http://' + document.domain + '/';
      url = jQuery.ja.isEmpty(url) ? document.URL : url;
      url = url.replace(domain,'');
      var obj = {};
      obj['domain'] = domain;
      var url_div = url.split('?');
      var host = (url_div.length > 0) ? url_div[0] : null;
      obj['host'] = host;

      var parameter = (url_div.length > 1) ? url_div[1] : null;
      obj['parameter'] = parameter;
      if (parameter != null) {
        parameter_div = parameter.split('&');
        var parameter_count = parameter_div.length;
        
        for (var i = 0; i < parameter_count; i++) {
            var pattern = parameter_div[i].split('=');
            obj[pattern[0]] = pattern[1];
        }

      }

      return obj;
    }

    // loading show layer
    , _loading : function(mode,target) {
        
      if ($.ja.isEmpty(target)) {
        target = '#loading';
        if (!$("body " + target).is(target)) {
          var source = $($.ja.regional.action.loading.source_nuli['#cdata']);
          $('body').append(source);
        }
      }

      target = $(target);

      $('.contents',target).html($.ja.regional.action.loading.wait['#cdata']);

      var xy = this.offset(target.width(),target.height(),"layer");

      if (mode == 'hidden') {
        target.remove();
      } else if (mode == 'center') {
        target.css('left',xy.left);
        target.css('top',xy.top);
        target.show();
      } else if (mode == 'customer') {
        target.show();
      } else {
        target.css('left',"10px");
        target.css('top',xy.scrollTop + 10);
        target.show();
      }

    }

    , _loading_show : function(loading_target,mode) {
      $(document).bind('ajaxStart', function() {
        jQuery.ja._loading(mode,loading_target);
      }).bind('ajaxStop', function() {
        jQuery.ja._loading('hidden',loading_target);
      });
    }

    // settings
    , setDefaults : function(settings) {
      $.extend(true,this,settings);
      return this;
    }

  });

  $.ja = new ja(); // Singleton instance

})(jQuery);



/**
* jQuery Action 1.0.0
* @file ja.action.js
* @depends ja.core.js , ja.filter.js
* @brief 폼전송처리

* Copyright (c) 2010 Seok Kyun. Choi. 최석균
* GNU Lesser General Public License
* http://www.gnu.org/licenses/lgpl.html
*/

(function($) {

  // settings
  $.jaAction = {
      _default : { 
        send : 'ajax' // ajax | submit

      , form : null // form element (stter is not.)
      , formAttrAction : '?' // form attr action (stter is not.)
      , formAttrMethod : 'get' // form attr method (stter is not.)
      , filter : null // input filtering
      , result : { } // action result (stter is not.)
      , prepare : null // only function
      , beforeAction : null // only function
      , beforeSend : null // only function
      , afterSend : null // only function and ajax
      , formAttr : null // form attr changes
      , values : null // input setValues
      , direct : null // direct parameter only, ajax only
      , param : null // parameter append, ajax or submit method get only
      , redirect : null // ajax is succes. redirect url, ajax only
      , rollback : { form : '' , input : '' } // input or form attr value rollback (stter is not.)
      , ask : '' // "confirm alert" message (ask or ask_msg)
      , ask_msg : '' // "confirm alert" custom message (ask or ask_msg)

      , loading : true // show loading layer and ajax
      , loading_mode : ''
      , loading_target : null

      , setAjax : { // jQuery ajax setting
          dataType : 'xml'
        , success : function(xml) {
          var redirect = $.jaAction.redirect;
          var error = $.ja.defString($(xml).find('error').text(),$.jaAction.result.error);
          var action = $.ja.defString($(xml).find('action').text(),$.jaAction.result.action);
          var source = $.ja.defString($(xml).find('source').text(),$.jaAction.result.source);
          var message = $.ja.defString($(xml).find('message').text(),$.jaAction.result.message);
          if (error == 'false') {
            if (!$.ja.isEmpty(redirect)) {
              action = 'url';
              source = redirect;
            }
          }

          $.extend($.jaAction.result , {
              message : message 
            , error : error 
            , action : action 
            , source : source 
            , target : $.ja.defString($(xml).find('target').text(),$.jaAction.result.target) 
            , title : $.ja.defString($(xml).find('title').text(),$.jaAction.result.title) 
            , display : $.ja.defString($(xml).find('display').text(),$.jaAction.result.display) 
          });

          $.ja.result($.jaAction.result);

          if (error == 'false') {
            // ajax complete function
            if ($.isFunction($.jaAction.afterSend)) {
              $.jaAction.afterSend($.jaAction);
            }
          }

        }
      }

    }

      , _reset : function() {
        this.result = {
            message : '' 
          , error : 'false' 
          , action : '' 
          , source : '' 
          , target : '' 
          , title : '' 
          , display : '' 
        }
      }

    // form attr change
    , _formAttr : function() {

      if (this.formAttr != null) {
        var config = $.ja.params2json(this.formAttr);
        for (var i in config) { 
          this.rollback.form += '&' + config[i].name + '=' + this.form.attr(config[i].name);
          this.form.attr(config[i].name,config[i].value);
        }
      }

      this.formAttrAction = $.ja.defString(this.form.attr('action'),this.formAttrAction);
      this.formAttrMethod = $.ja.defString(this.form.attr('method'),this.formAttrMethod);
    }

    // input setValues
    , _values : function() {
      if (this.values == null) { return; }
      if (typeof this.values == 'string') this.values = $.ja.params2json(this.values);
      this.setValues(this.form,this.values);
    }

    // input value load
    , _parameter : function() {
      var parameter = this.direct;

      if (parameter == null) {
        parameter = $(':input',this.form).serialize();

        // 정적 파라메터 추가
        if (this.param != null) {
          parameter = parameter + '&' + this.param;
        }

      }

      return parameter;
    }

    , setValues : function(form,values) {
      $.each(values, function(i, field){
        var target = $('#'+field.name,form);
        $.ja.setValue(target,field.value);
      });

      return form;
    }

    , paramCreateInput : function(form,param) {
      if (typeof form == 'string') form = jQuery(form);
      if (typeof param == 'string') param = $.ja.params2json(param);
      if (param == null) { return; }
      $.each(param, function(i, field){
        var target = $('#'+field.name,form);
        if (!target.is('#'+field.name)) {
          $('<input type="hidden" />').attr('id',field.name).attr('name',field.name).attr('value',field.value).appendTo(form);
        }
      });
    }
    , paramRemoveInput : function(form,param) {
      if (param == null) { return; }
      if (typeof form == 'string') form = jQuery(form);
      if (typeof param == 'string') param = $.ja.params2json(param);

      $.each(param, function(i, field){
        var target = $('#'+field.name,form);
        if (!target.is('input')) {
          target.remove();
        }
      });
    }

    // confirm
    , _question : function(is) {
      if (!is) { return false; }
      var question = $('#ask',this.form).val() || this.ask;
      var isQuestion = !$.ja.isEmpty(question);
      var isAskMsg = !$.ja.isEmpty(this.ask_msg);

      if ((isAskMsg || isQuestion) && is == true) { 
        if (isQuestion) { question = eval("$.ja.regional.action.question." + question + "['#cdata']"); }
        if (isAskMsg) { question = this.ask_msg; }
        if (!confirm(question)) return false; 
      }
      return true;
    }

    // input or form attr rollback
    , _rollback : function() {
      try {
        // 롤백
        this.setValues(this.form,this.rollback.input);

        // 폼 속성 롤백
        if (this.rollback.form != null) {
          var config = $.ja.params2json(this.rollback.form);
          for (var i in config) { 
            this.form.attr(config[i].name,config[i].value);
          }
        }

        if (this.send == 'submit') {
          this.paramRemoveInput(this.form,this.param);
        }
      } catch (e) { }
    }

    // ajax
    , _ajax : function() {

      $.ajaxSetup(this.setAjax);

      var obj = this;

      $.ajax({
          url: this.formAttrAction
        , type: this.formAttrMethod
        , data: this._parameter()
        , error : function(xhr, status, error) {
          if (xhr.readyState != 4 && xhr.status != 200) {
            alert($.ja.regional.action.message.error['#cdata'] + "\n[ajax error]" + xhr.readyState + "JA" + xhr.status);
          }
        }
      });

    }

    // submit()
    , _submit : function() {
        this.form.submit();
    }

    // jaAction init
    , _init : function() {

      this._reset();

      var commit = true;
      this._formAttr();

      if ($.isFunction(this.prepare)) {
        this.prepare(this);
      }

      this.rollback.input = $(':input',this.form).serializeArray();
      this._values();

      // 액션이 시작되기전에 호출
      if ($.isFunction(this.beforeAction)) {
        if (this.beforeAction(this) == false) {
          this._rollback();
          return this;
        }
      }

      if (this.filter != null) {
        this.result = $(this.form).jaFilter({ filter : this.filter });
        commit = !this.result.error;
      }

      // 질문 출력 여부 확인
      commit = this._question(commit);

      if (commit) {
        
        // 액션이 전송되기 전에 호출
        if ($.isFunction(this.beforeSend)) {
          if (this.beforeSend(this) == false) {
            this._rollback();
            return this;
          }
        }

        if (this.send == 'submit') {
          this.paramCreateInput(this.form,this.param);
          this._submit();
        } else {

          // 로딩표시
          if (this.loading) {
            var obj = this;
            $(document).bind('ajaxStart', function() {
              jQuery.ja._loading(obj.loading_mode,obj.loading_target);
            }).bind('ajaxStop', function() {
              jQuery.ja._loading('hidden',obj.loading_target);
            });
          }
          this._ajax();
        }

      }

      this._rollback();
    }

    , setDefaults : function(settings) {
      $.extend(true,$.jaAction._default,settings);
      return this;
    }
  };

  // proc
  $.fn.jaAction = function(options) {

    $.extend($.jaAction,$.jaAction._default,{form : this},options);
    $.jaAction._init();

  };

})(jQuery);

/**
* jQuery Action Filter
* @file ja.filter.js
* @depend : ja.core.js
* @brief 폼데이터 유효성검사

* Copyright (c) 2010 Seok Kyun. Choi. 최석균
* GNU Lesser General Public License
* http://www.gnu.org/licenses/lgpl.html
*/
(function($) {
  $.jaFilter = {
      form : null
    , filter : null
    , target : null
    , params : null
    , value : null
    , type : null
    , ja : null
    , message : null
    , func_msg : null

     , _params2json : function(param) { // jaFilter only
        param = param.replace(/^&|^\?/,'').split('&');
        var count = param.length;

        var obj = {go : [] , message : 'message' ,title : '' , display : 'alert' };

       if (jQuery.ja.isEmpty(param)) { return obj; }

        for (var i = 0; i < count; i++) {
          var pattern = param[i].split('=');
          var filter = pattern[0];
          var value = pattern[1];

          switch (filter) {
            case 'message' : obj['message'] = value; break;
            case 'title' : obj['title'] = value; break;
            case 'display' : obj['display'] = value; break;
            default :
              obj['go'][i] = { filter : filter , value : value };
            break;
          }
        }

        return obj;
     }

    // ja sync
    , _checkbox : function(filter,value) {
      var ele_checked = this.target.attr('checked');
      switch (this.type) {
        case 'radio' :
        case 'checkbox' :
          if (filter == 'notnull') { if (ele_checked == false) { this.ja.error = false; } }
          if (filter == 'null') { if (ele_checked == true) { this.ja.error = false; } }
        break;
        default :
          if (filter == 'notnull') { if (!$.ja.isEmpty(value)) { this.ja.error = false; } }
          if (filter == 'null') { if ($.ja.isEmpty(value)) { this.ja.error = false; } }
        break;
      }
    }
    , _filter : function(filter,value) {
      var go_node = '';
      this.ja.error = !$.ja.isReg(value,this.value);
      if (value == 'notnull') { 

        switch (this.type)
        {
          case 'text' : this.ja.message = $.ja.regional.action.filter.notnull['#cdata']; break;
          default : this.ja.message = $.ja.regional.action.filter.select['#cdata']; break;
        }

      }
      else { this.ja.message = eval("$.ja.regional.action.filter." + value + "['#cdata']"); }
      this.ja.message = (this.ja.error) ? this.ja.message : '';
    }

    , _minSelected : function(filter,value) { // 최소 선택수 체크

      var is = (value != 0) ? $.ja.isSelected(this.target,'min',value) : true;

      if (!is) {
        this.ja.error = true;
        this.ja.message = $.ja.regional.action.filter.min_select['#cdata'];
        this.ja.message = this.ja.message.replace('{$min}',value);
      }

    }

    , _maxSelected : function(filter,value) { // 최대 선택수 체크

      var is = (value != 0) ? $.ja.isSelected(this.target,'max',value) : true;

      if (!is) {
        this.ja.error = true;
        this.ja.message = $.ja.regional.action.filter.max_select['#cdata'];
        this.ja.message = this.ja.message.replace('{$max}',value);
      }

    }

    , _selected : function(filter,value) { // 선택수 체크
      var reg = /^[0-9*]+,[0-9*]+$/;

      if (reg.test(value)) {
        var div_value = value.split(',');
        var min = div_value[0];
        var max = div_value[1];
      } else {
        var min = value;
        var max = 0;
      }

      var ret_min = (min != 0) ? $.ja.isSelected(this.target,'min',min) : true;

      reg = /^[0-1]{1}$/;
      var reg2 = /^[0-9]+$/;

      if (!ret_min) {
        this.ja.error = true;
        if (reg.test(value)) { this.ja.message = $.ja.regional.action.filter.select['#cdata']; }
        else if (reg2.test(value)) { // ~ {$num} 개를 선택하세요.
          this.ja.message = $.ja.regional.action.filter.num_select['#cdata']; 
          this.ja.message = this.ja.message.replace('{$num}',min); // 선택 수 치환
        }
        else if (value == '*,*') { this.ja.message = $.ja.regional.action.filter.all_select['#cdata']; }
        else { this.ja.message = $.ja.regional.action.filter.selected['#cdata']; }
      }

      var ret_max = (max != 0) ? $.ja.isSelected(this.target,'max',max) : true;

      if (ret_min && !ret_max) {
        this.ja.error = true;
        if (reg.test(value)) { this.ja.message = $.ja.regional.action.filter.over_select['#cdata']; }
        else if (value == '*,*') { this.ja.message = $.ja.regional.action.filter.all_select['#cdata']; }
        else { this.ja.message = $.ja.regional.action.filter.selected['#cdata']; }
      }

      this.ja.message = this.ja.message.replace('{$min}',min).replace('{$max}',max); // 선택 수 치환

    }
    , _length : function(filter,value) {
      var div_value = value.split(',');
      var min = parseInt(div_value[0]);
      var max = parseInt(div_value[1]);

      if (!isNaN(min)) {
        filter = 'min_length';
        this.ja.error = !$.ja.isCount(filter,min,this.value);
        if (this.ja.error) { value = min; }
      }

      if (!isNaN(max) && !this.ja.error) {
        filter = 'max_length';
        this.ja.error = !$.ja.isCount(filter,max,this.value);
        if (this.ja.error) { value = max; }

        this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.length['#cdata'] : '';
      } else {
        this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.num_length['#cdata'] : '';
      }

      this.ja.message = this.ja.message.replace('{$min}',min).replace('{$max}',max).replace('{$num}',min);
    }

    , _minLength : function(filter,value) {
      this.ja.error = !$.ja.isCount(filter,value,this.value);
      this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.min_length['#cdata'] : '';
      this.ja.message = this.ja.message.replace('{$min}',value);
    }

    , _maxLength : function(filter,value) {
      this.ja.error = !$.ja.isCount(filter,value,this.value);
      this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.max_length['#cdata'] : '';
      this.ja.message = this.ja.message.replace('{$max}',value);
    }

    , _num : function(filter,value) { // 숫자 체크
      var div_value = value.split(',');
      var min = parseInt(div_value[0]);
      var max = parseInt(div_value[1]);

      if (isNaN(max)) { // 범위를 입력하지 않을 경우 최대값을 확인함.
        this.ja.error = (parseInt(this.value) <= min) ? false : true;
        this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.max_num['#cdata'] : '';
        this.ja.message = this.ja.message.replace('{$max}',min);
      } else {
        this.ja.error = (parseInt(this.value) >= min && parseInt(this.value) <= max) ? false : true;
        this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.num['#cdata'] : '';
        this.ja.message = this.ja.message.replace('{$min}',min).replace('{$max}',max);
      }
    }

    , _value : function(filter,value) {
      var val = $.ja.getValue(this.target);
      if (val != value) {
        this.ja.error = true;
        this.ja.message = (this.ja.error) ? $.ja.regional.action.filter.notlike['#cdata'] : '';
      }
    }

    , _compare : function(filter,value) { // 값 비교
      if (this.form == null) { return; }

      var reg_first = /(^!)?(.*)/;
      var reg_second = /^!/;

      var val_first,val_second;

      if (reg_first.test(filter)) {
        reg_first.exec(filter);
        val_first = RegExp.$2;

        val_first = $(val_first,this.form).val();
      } else { val_first = filter; }

      if (reg_first.test(value)) {
        reg_first.exec(value);
        val_second = RegExp.$2;

        val_second = $(val_second,this.form).val();
      } else { val_second = value.replace(reg_second,''); }

      // ! 로 시작할 경우 두 값이 다를 경우 오류 발생
      if (reg_second.test(value)) {
        if (val_first != val_second) { this.ja.error = true; this.ja.message = $.ja.regional.action.filter.notlike['#cdata']; }
      } else {
        if (val_first == val_second) { this.ja.error = true; this.ja.message = $.ja.regional.action.filter.like['#cdata']; }
      }
    }

    , _filtering : function(target,params,message) {
      // 초기화
      this._reset();

      this.target = !$.ja.isEmpty(target) ? target : this.target;
      this.params = !$.ja.isEmpty(params) ? params : this.params;
      this.message = !$.ja.isEmpty(message) ? message : this.message;

      this.value = this.target.val();
      this.type = $.ja.getCrossType(this.target.attr('type'));

      var obj = this._params2json(this.params);

      // 타켓
      this.ja.target = this.target;
      var is_element = $(this.ja.target).length;
      if (is_element == 0) { return; }

      // 타이틀 변수
      this.ja.title = $.ja.defString(obj.title,$.ja.defString(this.target.attr('title'),''));

      // 메세지 출력
      this.ja.display = obj.display;
      // 엘리먼트를 체크하지 않아도 되는 경우 종료 (filter=notnull)
      var reg_pattern = /(filter=notnull|min_length=[0-9]+|length=[0-9,]+|selected=[0-9,]+|min_selected=[0-9]+)/;
      if (reg_pattern.test(this.params) == false) { if ($.ja.isReg('notnull',this.value) == false) { this.ja.error = false; return; } }

      var count = obj.go.length;
      var i = 0;

      // 필터 시작
      while (!this.ja.error && i < count) {
        var go_filter = obj.go[i].filter;
        var go_value = obj.go[i].value;

        switch (go_filter) {
          // what?
          case 'notnull' :
          case 'null' :
            this._checkbox(go_filter,go_value);
          break;
          case 'filter' : this._filter(go_filter,go_value); break;
          case 'selected' : this._selected(go_filter,go_value); break;
          case 'min_selected' : this._minSelected(go_filter,go_value); break;
          case 'max_selected' : this._maxSelected(go_filter,go_value); break;
          case 'min_length' : this._minLength(go_filter,go_value); break;
          case 'max_length' : this._maxLength(go_filter,go_value); break;
          case 'length' : this._length(go_filter,go_value); break;
          case 'num' : this._num(go_filter,go_value); break;
          case 'value' : this._value(go_filter,go_value); break;
          default : this._compare(go_filter,go_value); break;
        }

        if (this.ja.error) {
          if (!$.ja.isEmpty(this.type)) { this.ja.action = 'focus'; }
          var reg = /^filter/g;
          
          // 메세지를 완성시킨다.
          switch (obj.message) {
            case 'title' : this.ja.message = this.ja.title; break;
            case 'no' : this.ja.message = ''; break;
            default : 
              var m = eval("$.ja.regional.action." + obj.message + "['#cdata']");
              if ($.ja.isEmpty(m)) { m = this.ja.message; }
              this.ja.message = this.ja.title + (!reg.test(obj.message) ? m : '');
            break;
          }

          break;
        }

        i++;
      }

      return this.ja;
    }

    , _reset : function() {
      this.ja = { message : '' , title : '' , display : '' , error : false , target : null , action : null , source : '' };
    }

    // settings
    , setDefaults : function(settings) {
      $.extend(true,this,settings);
      return this;
    }

    , _init : function() {
      // 설정된 메세지 저장
      var message = this.message;

      for (var i in this.filter) {
        this.target = $(this.filter[i].target,this.form);
        this.params = this.filter[i].params;

        this._filtering();
        
        // 동적 메시지가 있을 경우
        if ($.isFunction( this.filter[i].message ) ) {
          this.func_msg = this.filter[i].message;
        } else {
          this.func_msg = message;
        }

        if (this.ja.error) { return false; break; } // 에러가 발생한 경우 false
      }
    }

  };

  $.fn.jaFilter = function(options) {
    $.extend($.jaFilter,{ form : this },options);
    if ($.ja.isEmpty($.jaFilter.form) || $.ja.isEmpty($.jaFilter.filter)) { return alert('error jaFilter'); }
    if (!$.jaFilter._init()) {
      if ($.isFunction( $.jaFilter.func_msg )) {
        $.jaFilter.func_msg($.jaFilter.ja);
      } else {
        $.ja.result($.jaFilter.ja); 
      }

    }
    return $.jaFilter.ja;
  };

})(jQuery);

/**
* jQuery Action PageNavigator
* @file ja.pagenavigator.js
* @depend ja.core.js
* @brief 페이지네비게이션

* Copyright (c) 2010 Seok Kyun. Choi. 최석균
* GNU Lesser General Public License
* http://www.gnu.org/licenses/lgpl.html
*/

(function($) {
  $.jaPageNavigator = {
      group : $('#page_group') // 페이지태그 영역
    , start : '.start' // 처음페이지 태그
    , startx : '.startx' // 처음페이지 없을경우 숨김 태그
    , prev : '.prev' // 이전페이지 태그
    , prevx : '.prevx' // 이전페이지 없을경우 숨김 태그
    , pageaction : '.pageaction' // 페이지 번호 노출 영역
    , now : '.now' // 현재페이지번호 태그
    , num : '.num' // 페이지번호 태그
    , div : '.div' // 페이지번호 분리 태그
    , next : '.next' // 다음페이지 태그
    , nextx : '.nextx' // 다음페이지 없을경우 숨김 태그
    , end : '.end' // 마지막페이지 태그
    , endx : '.endx' // 마지막페이지 없을경우 숨김 태그
    , prevpage : '.prevpage' // 이전 1 페이지 태그
    , prevpagex : '.prevpagex' // 이전 1 페이지 없을경우 숨김 태그
    , nextpage : '.nextpage' // 다음 1 페이지 태그
    , nextpagex : '.nextpagex' // 다음 1 페이지 없을경우 숨김 태그
    , pagemove : '.pagemove' // 페이지이동 태그
    , pagemovex : '.pagemovex' // 페이지이동 없을경우 숨김 태그
    , url : '' + document.location // 기본적인 url
    , page : '0' // 현재페이지번호
    , total_count : '0' // 총 레코드수
    , page_link : 10 // 페이지링크번호 노출 수
    , page_row : 10 // 레코드 노출 수
    , form : null // 타켓 폼
    , name : 'page' // 페이지 파라메터 명
    , values : null // 폼 전송시 넘겨줄 정적 파라메터
    , action : null // function : 페이지이동 이벤트 발생시 실행할 함수
    , autosort : false // 페이지번호 자동정렬 (선택된 번호가 중간에 위치)

    , move : function() {
      if (this.form != null) {
        $(this.form).jaAction({ 
            send : 'submit' 
          , values : this.values 
          , formAttr : 'method=get&action=' + this.url
        })
      }
    }

    , href : function(page) {
       if ($.isFunction(this.action)) {
         this.action(page);
      } else {

        $(this.form).jaAction({ 
            send : 'submit'
          , formAttr : 'method=get&action=' + this.url
          , values : this.name + '=' + page
        });

      }
    }

  };

  $.fn.jaPageNavigator = function(options) {

    function search_alink(target) {
      if (jQuery('a',target).is('a')) {
        return 1;
      } else if (jQuery(target).is('a')) {
        return 2;
      } else {
        return 0;
      }
    }

    function alink(target,url,num,page) {

      if (parseInt(page) > 0) {
        var item = target.html();
        target.html(item.replace('page',page));
      }

      if (num == 1) {
        jQuery('a',target).attr('href',url);
      } else if (num == 2) {
        jQuery(target).attr('href',url);
      } else {
      }

      return target;
    }

    var p = $.extend($.jaPageNavigator,{group : this},options);

    if ($(p.group).text() == '') {
      return;
    }

    var start = $(p.start,p.group);
    var startx = $(p.startx,p.group);
    // 이전 페이지 이동 ( -page_link)
    var prev = $(p.prev,p.group);
    var prevx = $(p.prevx,p.group);
    var pageaction = $(p.pageaction,p.group);
    var now = $(p.now,p.group);
    var num = $(p.num,p.group);
    var div = $(p.div,p.group);
    // 다음 페이지 이동 ( +page_link)
    var next = $(p.next,p.group);
    var nextx = $(p.nextx,p.group);
    var end = $(p.end,p.group);
    var endx = $(p.endx,p.group);
    // 다음 페이지 이동 (-1)
    var prevpage = $(p.prevpage,p.group);
    var prevpagex = $(p.prevpagex,p.group);
    // 다음 페이지 이동 (+1)
    var nextpage = $(p.nextpage,p.group);
    var nextpagex = $(p.nextpagex,p.group);
    var pagemove = $(p.pagemove,p.group);
    var pagemovex = $(p.pagemovex,p.group);

    var page_index = '';

    var page = parseInt(p.page);
    var total_count = parseInt(p.total_count);
    var page_row = p.page_row;
    var page_link = p.page_link;
    var total_page = Math.floor((total_count - 1) / page_row) + 1;
    var start_page = Math.floor((page - 1) / page_link) * page_link + 1;
    var end_page = start_page + (page_link - 1);
    var now_page = Math.floor(start_page / page_link) + 1;
    var next_page = end_page + 1;
    var prev_page = start_page - 1;
    var url = p.url;

    // 총 페이지수 노출
    pagemove.hide();
    if (pagemove.is(p.pagemove)) { 
      var pagemove_html = pagemove.html(); pagemove.empty(); 
      pagemove.html(pagemove_html.replace('total',total_page));

      if (total_page > 1) {
        pagemove.show();
      }
    }

//    var reg_url = /\?|&.*={0,}.*$/;

  //  if (!$.ja.isEmpty(url) && !reg_url.test(url)) { p.url += '&'; } // 구버전을 위해 ( move 버튼을 사용할 경우를 대비 , 문제점 발생안할 경우 제거)
  //  if (!$.ja.isEmpty(url) && reg_url.test(url)) { url += '&'; }
    if (url.indexOf('?') > -1) { url += '&'; } else { url += '?'; }

    if (!isNaN(total_page) && total_page > 0) {
      if (end_page > total_page ) { end_page = total_page; }

      var s,e;

      if (p.autosort) {
        var link = Math.floor(page_link/2);
        var n = page + link;
        s = page - link;
        e = n;
        if (0 >= s) { s = 1; }
        if (total_page <= n) { e = total_page; }
        prev_page = s;
        next_page = e;
      } else {
        s = start_page;
        e = end_page;
      }

      var num_link = search_alink(num);
      for (var i = s; i <= e; i++) {

      if (i == page) { 
        var h = jQuery(now).clone();
        var item = h.html();
        h.html(item.replace('page',i));
        pageaction.before(h);
      } else { 
        var h = jQuery(num).clone();
        pageaction.before(alink(h,url + p.name + '=' + i,num_link,i));
      }

        if (i != e) { pageaction.before(jQuery(div).clone()); }
      }

      pageaction.remove();
      now.remove();
      num.remove();
      div.remove();
      
      // 처음 페이지
      if (start.is(p.start)) {
      start.hide();
      startx.hide();
      if (page != 1) { 
        alink(start,url + p.name + '=1',search_alink(start),1);
        start.show();
      } else { startx.show(); }
      }

      // 이전 1 페이지
      if (prevpage.is(p.prevpage)) {
      prevpage.hide();
      prevpagex.hide();
      if(page > 1) { 
        alink(prevpage,url + p.name + '=' + (page-1),search_alink(prevpage),(page-1));
        prevpage.show();
      } else { prevpagex.show(); }
      }
      
      // 이전 페이지
      if (prev.is(p.prev)) {
      prev.hide();
      prevx.hide();
      if(now_page != 1 && total_page > 1) { 
        alink(prev,url + p.name + '=' + prev_page,search_alink(prev),prev_page);
        prev.show();
      } else { prevx.show(); }
      }
      
      // 다음 1 페이지
      if (nextpage.is(p.nextpage)) {
      nextpage.hide();
      nextpagex.hide();
      if (page < total_page) { 
        alink(nextpage,url + p.name + '=' + (page+1),search_alink(nextpage),(page+1));
        nextpage.show();
      } else { nextpagex.show(); }
      }
      
      // 다음 페이지
      if (next.is(p.next)) {
        next.hide();
        nextx.hide();
        if (end_page < total_page) { 
          alink(next,url + p.name + '=' + next_page,search_alink(next),next_page);
          next.show(); 
        } else { nextx.show(); }
      }
      
      // 마지막 페이지
      if (end.is(p.end)) {
        end.hide();
        endx.hide();
        if (page != total_page) { 
          alink(end,url + p.name + '=' + total_page,search_alink(end),total_page);
          end.show();
        } else { endx.show(); }
      }

      // 토탈 페이지수가 링크수보다 적기때문에 처음과 마지막 페이지 필요없음
      if (page_link > total_page) {
        start.remove();
        end.remove();
      }
      this.show();
    } else {
      this.hide();
    }

    return this;
  };

})(jQuery);

/**
* jQuery Action monthpick
* @file ja.monthpick.js
* @depend ja.core.js
* @brief 년월 선택기

* Copyright (c) 2010 Seok Kyun. Choi. 최석균
* GNU Lesser General Public License
* http://www.gnu.org/licenses/lgpl.html
*/
(function($) {

  $.monthpick = {
      target : null
    , type : null // text | input
    , mode : 'month' // year | month
    , afterSend : null // function only
    , altField : null // string
    , altSend : false
    , offset : null
    , value : null
    , date : null
    , format : 'yyyy-mm'
    , tid : null

    , _default : { 
        mode : 'month' // year | month
      , afterSend : null // function only
      , altField : null // return target
      , altSend : false // location event
      , format : 'yyyy-mm' // $.ja.dateFormat call
      , close : '닫기'
    }

    , create : function() {
      // filter
      //var val = this.value.replace(/[-_:.\/\s]/g,"-");
      var th_cursor = '';
      var td_cursor = '';
      if (this.mode == 'year') {
        this.value = this.value + '01';
        th_cursor = "style='cursor:pointer;'";
      } else {
        td_cursor = "style='cursor:pointer;'";
      }
      var val = this.value || null;
      if (val == null) {
        val = $.ja.dateFormat('yyyy-mm-dd');
      }
      val = val.replace(/[^0-9]/g,"");
      
      // date YYYY MM
      var year = "";
      var month = "";

      var reg = /(^[0-9]{4})([0-9]{0,2})([0-9]{0,2})$/;
      if (reg.test(val)) {
        reg.exec(val,"g");
        year = parseInt(RegExp.$1);
        month = RegExp.$2;
      }

      var start_year = year - 2;
      var end_year = year + 2;

      var disp = $("<div class='monthpick' style='position:absolute; z-index: 1000'></div>");

      var html = "<table class='monthpick-table'>"
                    + "<thead>"
                    + "<tr>"
                    + "<td colspan='11'></td>"
                    + "<td colspan='2' onclick='$.monthpick._destroy()' style='cursor:pointer;'>" + $.ja.regional.action.monthpick.close + "</td>"
                    + "</tr></thead><tbody>";
      for (start_year; start_year <= end_year; start_year++) {
        html += "<tr>"
                + "<th " + th_cursor + ">" + start_year + "</th>"
                + "<td " + td_cursor + ">01</td>"
                + "<td " + td_cursor + ">02</td>"
                + "<td " + td_cursor + ">03</td>"
                + "<td " + td_cursor + ">04</td>"
                + "<td " + td_cursor + ">05</td>"
                + "<td " + td_cursor + ">06</td>"
                + "<td " + td_cursor + ">07</td>"
                + "<td " + td_cursor + ">08</td>"
                + "<td " + td_cursor + ">09</td>"
                + "<td " + td_cursor + ">10</td>"
                + "<td " + td_cursor + ">11</td>"
                + "<td " + td_cursor + ">12</td>"
                + "</tr>";
      }

      html += "</tbody></table>";

      disp.append(html);
      disp.css("left",this.offset.left);
      disp.css("top",this.offset.top + this.target.outerHeight());
      $('body').append(disp);

      $('#ismonthpick').val('1');

      this.tid = $('.monthpick');
    }

    , _year : function() {
      var obj = this;
      $('.monthpick .monthpick-table tbody th').click(function() {
        var year = $(this).text();
        year = parseInt($.trim(year));
        var date = new Date(year,0,1);
        obj._after($.ja.dateFormat(obj.format,date));
      });
    }

    , _month : function() {
      var obj = this;
      $('.monthpick .monthpick-table tbody td').click(function() {
        var year = $('th',$(this).parent()).text();
        year = parseInt($.trim(year));
        var month = $(this).text()
        month = parseInt($.trim(month));
        var date = new Date(year,month-1,1);
        obj._after($.ja.dateFormat(obj.format,date));
      });
    }

    , _destroy : function() {
      $('body .monthpick').remove();
      this.tid = null;
    }

    , _show : function() {
      if (this.tid == null ) { 
        this.create(); 
      } else {
        this._destroy();
      }

    }

    , _after : function(date) {
      this.date = date;
      if (this.altField != null) {
        $(this.altField).val(this.date);
      }

      if (this.target.is(":input")) {
        this.target.val(this.date);
      } else {
        this.target.text(this.date);
      }

      this._show();

      if ($.isFunction(this.afterSend)) {
        this.afterSend(this);
      }

      if (this.altSend) {
        $.ja.href('?' + $(this.altField).attr('id') + '=' + this.date);
      }
    }

    , _init : function() {
      this._destroy();

      if (this.target.is(":input")) {
        this.value = this.target.val();
        this.type = 'input';
      } else {
        this.value = this.target.text();
        this.type = 'text';
      }

      this.offset = this.target.offset();
      this._show();

      if (this.mode == 'year') {
        this._year();
      } else {
        this._month();
      }
      
    }
  };

  $.fn.monthpick = function(options) {
    $.extend($.monthpick,$.monthpick._default,{target : this},options);
    $.monthpick._init();
    return this;
  };
})(jQuery);


(function($) {

  $.jaUtils = {

    toggleSyncSingle : function(eles,sync,def_idx,func) {
    if (jQuery.ja.isEmpty(sync)) { return; }
    var url = document.URL;

    var url_obj = jQuery.ja.url2Json();
    var url_syne = url_obj[sync];

    var obj = jQuery(eles);
    var cnt = obj.length;
    func(obj,false);

    for (var i = 0; i < cnt; i++ ) {
      var href = jQuery(eles + ":eq(" + i + ")").attr("href");
      if (!jQuery.ja.isEmpty(href)) {
        var obj = jQuery.ja.url2Json(href);
        if (url_syne == obj[sync]) { return func(jQuery(eles + ":eq(" + i + ")"),true); break; }
      }
    }
    if (def_idx == null) { return; } else { func(jQuery(eles + ":eq(" + def_idx + ")"),true); }
  }

  , toggleSyncMulti : function(ele,style,type,data) {
    var url = document.URL;

    var on_target;
    var def_target;

    jQuery(ele).each(function(i) {
      var cnt;
      var index;
      var def;
      var condition;

      if (data.target[i] == undefined) {
        try{ cnt = data.target.search.length; } catch (e){ cnt = 1; }
        search = data.target.search;
        def = data.target.def;
        condition = jQuery.ja.defString(data.target.condition,'and');
      } else {
        try{ cnt = data.target[i].search.length; } catch (e){ cnt = 1; }
        search = data.target[i].search;
        def = data.target[i].def;
        condition = jQuery.ja.defString(data.target[i].condition,'and');
      }

      if (def) { def_target = jQuery(this); }

      var is = true;
      var c = 0;

      for (var c = 0; c < cnt; c++ ) {
        var command;

        if (cnt == 1) { command = search; } else { command = search[c]; }
        
        if (condition == 'or') {
          if (url.indexOf(command) > -1) { is = true; break; } else { is = false; }
        } else {
          if (url.indexOf(command) == -1) { is = false; break; }
        }

      }

      if (is) { on_target = jQuery(this); }

    });

    if (on_target == null) {
      
      if (def_target != null) {

        if (jQuery.isFunction(style)) {
          style(def_target);
        } else {
          def_target.addClass(style);
        }
      }

    } else {

      if (jQuery.isFunction(style)) {
        style(on_target);
      } else {
        on_target.addClass(style);
      }


    }

  }


  }

})(jQuery);