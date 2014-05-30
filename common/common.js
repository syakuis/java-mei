
jQuery.mei || (function($) {
  function mei() {
    this.version = '1.0';
  }

$.extend(mei.prototype, {

  filesize_unit : function(size,unit) {

    var unit_string = "";
    var ret_size = "";

    var size_kb = 1024;
    var size_mb = size_kb * 1024;
    var size_gb = size_mb * 1024;
    var size_tb = size_gb * 1024;

    switch (unit) {

      case 1 :
        unit_string = " B";
      break;

      case 2 :
        size = size / 1024;
        unit_string = " KB";
      break;
      case 3 :
        size = size / 1024 / 1024;
        unit_string = " MB";
      break;
      case 4 :
        size = size / 1024 / 1024 / 1024;
        unit_string = " GB";
      break;
      case 5 :
        size = size / 1024 / 1024 / 1024 / 1024;
        unit_string = " TB";
      break;

      case 0 :
      default : 
        if (size_tb <= size) {
          size = size / 1024 / 1024 / 1024 / 1024;
          unit_string = " TB";
        } else if (size_gb <= size && size_tb > size) {
          size = size / 1024 / 1024 / 1024;
          unit_string = " GB";
        } else if (size_mb <= size && size_gb > size) {
          size = size / 1024 / 1024;
          unit_string = " MB";
        } else if (size_kb <= size && size_mb > size) {
          size = size / 1024;
          unit_string = " KB";
        } else {
          unit_string = " B";
        }

      break;
    }

    var chk = "" + size;
    if ( chk.indexOf(".") > -1) { ret_size = size.toFixed(2); }
    if (ret_size == 0) { ret_size = "0"; }

    ret_size = ret_size + unit_string;

    return ret_size;
  }

});


  $.mei = new mei(); // Singleton instance

})(jQuery);
















function dialog(data,body) {
  if(jQuery.ja.isEmpty(body)) { body = jQuery("body"); }
  var url = data.url;
  var target = data.target;
  var title = data.title;
  var width = data.width;
  width = jQuery.ja.isEmpty(width) ? 'auto' : width;
  var height = data.height;
  height = jQuery.ja.isEmpty(height) ? 'auto' : height;
  var reload = data.reload;

  var j = "#" + target;
  var j2 = "#" + target + "2";

  var iframearea = jQuery(j,body);
  var iframearea2 = jQuery(j2,body);

  if (!iframearea.is(j)) {
    body.append("<div id='" + target + "'></div>");
    iframearea = jQuery(j,body);
    iframearea.dialog(jQuery.extend({
      bgiframe: true,
      autoOpen: false,
      width: width,
      height: height,
      modal: true,
      draggable: false,
      closeOnEscape: true,
      title: title,
      resizable: false
    },data.options));

    iframearea.append("<iframe id='" + target + "2' frameborder='0' width='100%' height='100%' src='" + url + "'></iframe>");
  } else {
    if (reload) {
      iframearea2.attr('src',url);
    }
  }

  if (data.options.autoOpen != true) {
    iframearea.dialog('open');
  }

  return iframearea;
}
/*
function targetOnClass(id,style,target) {
  var parameter = unescape(location.search).substring(1);
  var pathname = location.pathname;
  pathname = pathname.replace(/^\/|\.\.\/|\.\//,'');

  // unescape(location.search).substring(1)

  jQuery(id).each(function() {
    var href = jQuery('a',this).attr('href');
    href = href.replace(/^\/|\.\.\/|\.\//,'');

    if (href.indexOf(pathname) > -1) {

      var css = jQuery('a',this).attr('class');
      css = css.replace(target,'');
      var param = target + '=' + css;

      if (jQuery.ja.isEmpty(target) || parameter.indexOf(param) > -1) {
        jQuery(this).addClass(style);
      }

    }

  });

}
*/

// 선택된 대상 효과주기 : 다중
function targetOnClass(ele,style,type,data) {
  var url = document.URL;

  var on_target;
  var def_target;

  jQuery(ele).each(function(i) {
    var cnt;
    var index;
    var def;
    var condition;

    if (data.target[i] == undefined) {
      cnt = data.target.search.length;
      search = data.target.search;
      def = data.target.def;
      condition = jQuery.ja.defString(data.target.condition,'and');
    } else {
      cnt = data.target[i].search.length;
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

function targetOnClassSingle (eles,sync,def_idx,func) {
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

// 구글지도표시
function google_map(options) {

  var myOptions = {
    zoom: 14,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };

  myOptions = jQuery.extend(myOptions, options);

  var map = new google.maps.Map(document.getElementById(myOptions.mapTarget), myOptions);

  var geocoder = new google.maps.Geocoder();

  if (geocoder) {
    geocoder.geocode( { 'address': myOptions.address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        jQuery(myOptions.map_xy).val(results[0].geometry.location);
        map.setCenter(results[0].geometry.location);
        var marker = new google.maps.Marker( jQuery.extend( myOptions.marker_options , {
            map: map, 
            position: results[0].geometry.location,
            flat: false
        }) );
      } else {
        alert("해당 위치를 찾을 수 없습니다. : " + status);
      }
    });
  }

  return map;
}

// 이미지 리사이즈
function image_resize(full_target,resize_target) {
  var sub_column_content = parseInt(jQuery(full_target).width());
  jQuery(resize_target + ' img').each(function() {
    var obj = jQuery(this);
    if (sub_column_content < parseInt(obj.width())) {
      obj.css('width','100%');
    }
  });
}

function social_link(tid,url,title) {
  tid = jQuery(tid);

  url = encodeURIComponent(url);
  title = encodeURIComponent(title);
  var tag = encodeURIComponent("봉안닷컴");

  /* 구글
  var urlshort_api = "https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyAfN_8r5tVkpiN0bPLvT0IHKT1ZDKOUEi8";

  jQuery.ajax({
    dataType : 'json',
    url : urlshort_api,
    contentType : 'application/json',
    data : {"longUrl": url} ,
    type : 'POST',
      async:false,
    success : function(data) {
      short_url = data.id;
    }
  }); */

/*
  jQuery.ajax({
    url : 'http://api.bitly.com/v3/shorten',
    data : 'format=xml&login=syakuis&apiKey=R_83133f6d32237352139a57efbaee9530&longUrl=http://naver.com/saddasd',
    type : 'GET',
    dataType : 'xml',
    async: false,
    error : function(xhr, status, error) {
     alert("[ajax error]" + xhr.readyState + "JA" + xhr.status);
    },
    success : function(xml) {
      short_url = jQuery(xml).find('response data url').text();
    }
  });
*/

  short_url = encodeURIComponent(url);

  var social = jQuery("<div></div>");
  jQuery("<span class='btn_sns_facebook mr5 cs-pointer'></span>").click(function() {
      jQuery.ja.popup({
          url : 'http://www.facebook.com/sharer/sharer.php?u='+ url
        , name : 'sns_facebook'
        , scrollbars : 'yes'
        , resizable : 'yes'
        , width : 500
        , height : 400
        , center : true
      });
  }).appendTo(social);

  jQuery("<span class='btn_sns_me2day mr5 cs-pointer'></span>").click(function() {
      jQuery.ja.popup({
          url : 'http://me2day.net/plugins/post/new?new_post[body]=' + title + ':' + url + '&new_post[tags]=' + tag
        , name : 'sns_me2day'
        , scrollbars : 'yes'
        , resizable : 'yes'
        , width : 500
        , height : 400
        , center : true
      });
  }).appendTo(social);

  jQuery("<span class='btn_sns_twitter mr5 cs-pointer'></span>").click(function() {
      jQuery.ja.popup({
          url : 'http://twitter.com/home?status=' + url
        , name : 'sns_twitter'
        , scrollbars : 'yes'
        , resizable : 'yes'
        , width : 500
        , height : 400
        , center : true
      });
  }).appendTo(social);

  jQuery("<span class='btn_sns_yozm cs-pointer'></span>").click(function() {
      jQuery.ja.popup({
          url : 'http://yozm.daum.net/api/popup/prePost/?link=' + url + "&prefix=" + title
        , name : 'sns_yozm'
        , scrollbars : 'yes'
        , resizable : 'yes'
        , width : 500
        , height : 400
        , center : true
      });
  }).appendTo(social);

  tid.html(social);
}

// 임시저장

(function($) {

  $.tempsave = {

      title : null,
      content : null,
      time : 60000, // 1분
      cookie : null,
      message : '#tempsave',

    _initialize : function(settings) {
      $.extend(this,settings);
      
      $(this.message).hide();

      var data = eval( jQuery.ja.getCookie(this.cookie) );
      if (!jQuery.ja.isEmpty(data)) {
        var title = data.title;
        var content = data.content;

        if (!jQuery.ja.isEmpty(title) || !jQuery.ja.isEmpty(content)) {
          if ( confirm("임시저장된 내용이 있습니다. 불러오시겠습니까?") ) {

            if (!jQuery.ja.isEmpty(title)) { jQuery(this.title).val(title); }
            if (!jQuery.ja.isEmpty(content)) { jQuery(this.content).val(content); }

          }
        }
      }

      setTimeout("$.tempsave._save()",this.time);

    },
    
    _save : function() {
      var obj = this;

      var title = jQuery(this.title).val();
      var content = jQuery(this.content).val();

      if (!jQuery.ja.isEmpty(title) || !jQuery.ja.isEmpty(content)) {
        var data = "({ title : '" + title + "' , content : '" + content + "' })";
        jQuery.ja.setCookie(this.cookie,data,1,"/");
        $(this.message).show();
        setTimeout(function() { $(obj.message).hide(); }, 5000 );
      }

      setTimeout("$.tempsave._save()",this.time);
    },

    _delete : function() {
      jQuery.ja.setCookie(this.cookie,"");
    }

  };

})(jQuery);




(function($) {



  // daoCode.getXmlCache("document","list")
  jQuery.xml2selectbox = {

    target_title : '선택' , 
    target_title2 : '선택2' , 

    /**
    jQuery.xml2selectbox.selectbox_to_once({
      xml : "XML LIST STYLE" , 
      title_name : '',
      target_name : '',
      title : '선택' , 
      target_orl : '#form #' , 
      val : ''
    });
    */

    selectbox_to_once : function(options) {
      $.extend( options , { title : '' } );

      var xml = options.xml;
      var title_name = options.title_name;
      var target_name = options.target_name;
      this.target_title = options.title;
      var o_target_orl = options.target_orl;
      var target_orl = options.val;

      var category_target = jQuery(o_target_orl);
      
      if ( isNaN(this.target_title) ) {
        jQuery("<option value=''>" + $.xml2selectbox.target_title + "</option>").appendTo(category_target); 
      }
      
      var data = xml;

      if (typeof data == 'string') {
        var json = xml2json(parseXml(xml), " ");
        data = eval("("+json+")");
      }

      data = data.data.item;

      /*
      var json = xml2json(parseXml(xml), " ");
      json = eval("("+json+")");
      var data = json.data.item;
      */
      var p_count = data.length;

      var child_item = [ ];

      jQuery(data).each(function(i) {
        var c_data = data[i];
        var title = c_data['@' + title_name];
        var category_orl = c_data['@' + target_name];
        var c_data_item = c_data.item;
        
        child_item[category_orl] = c_data_item;

        var p_option = jQuery("<option value='" + category_orl + "'>" + title + "</option>");
        if (category_orl == target_orl) { 
          p_option.attr("selected",true);
        }

        p_option.appendTo(category_target);

      });

    },

    /**
    jQuery.xml2selectbox.selectbox_to_depths({
      xml : "XML DEPTH STYLE" , 
      title_name : '',
      target_name : '',
      title : '선택' , 
      title2 : '나머지 선택' , 
      target_orl : '#form #' , 
      target2_orl : '#form #' , 
      val : '' , 
      val2 : ''
    });
    */
    selectbox_to_depths : function(options) {
      var xml = options.xml;
      var title_name = options.title_name;
      var target_name = options.target_name;
      this.target_title = options.title;
      this.target_title2 = options.title2;
      var o_target_orl = options.target_orl;
      var o_target2_orl = options.target2_orl;
      var target_orl = options.val;
      var target2_orl = options.val2;

      var category_target = jQuery(o_target_orl);
      var category2_target = jQuery(o_target2_orl);
      category2_target.append("<option value=''>" + $.xml2selectbox.target_title2 + "</option>");

      jQuery("<option value=''>" + $.xml2selectbox.target_title + "</option>").bind("click",function(e){
        category2_target.empty();
        category2_target.append("<option value=''>" + $.xml2selectbox.target_title2 + "</option>");
      }).appendTo(category_target); 

      var json = xml2json(parseXml(xml), " ");
      json = eval("("+json+")");

      var data = json.data.item;
      var p_count = data.length;

      var child_item = [ ];

      jQuery(data).each(function(i) {
        var c_data = data[i];
        var title = c_data['@' + target_name];
        var category_orl = c_data['@' + target_name];
        var c_data_item = c_data.item;
        
        child_item[category_orl] = c_data_item;

        var p_option = jQuery("<option value='" + category_orl + "'>" + title + "</option>");
        if (category_orl == target_orl) { 
          p_option.attr("selected",true);
          $.xml2selectbox.selectbox_to_depths_child(c_data_item,category2_target,target2_orl); 
        }

        p_option.appendTo(category_target);

      });

      category_target.change(function() {
        var category_orl = jQuery(this).val();

        if (!jQuery.ja.isEmpty(category_orl)) {
          var item = child_item[category_orl];
          $.xml2selectbox.selectbox_to_depths_child(item,category2_target,target2_orl); 
        } else {
          category2_target.empty();
          category2_target.append("<option value=''>" + $.xml2selectbox.target_title2 + "</option>");
        }

      });

    },

    selectbox_to_depths_child : function (c_data_item,category2_target,target2_orl) {
      if (jQuery.ja.isEmpty(c_data_item)){ 
        category2_target.empty();
        category2_target.append("<option value=''>" + $.xml2selectbox.target_title2 + "</option>");
        return; 
      }
      var c_count_item = c_data_item.length;

      category2_target.empty();
      category2_target.append("<option value=''>" + $.xml2selectbox.target_title2 + "</option>");

      if (jQuery.ja.isEmpty(c_count_item)){ 

        var title = c_data_item['@' + target_name];
        if (title == '') { return; }
        var category_orl = c_data_item['@' + target_name];
        var option = jQuery("<option value='" + category_orl + "'>" + title + "</option>");
        if (category_orl == target2_orl) { option.attr("selected","true"); }
        category2_target.append(option);

      } else {

        for (var c =0; c < c_count_item; c++) {
          var title = c_data_item[c]['@' + target_name];
          var category_orl = c_data_item[c]['@' + target_name];
          var option = jQuery("<option value='" + category_orl + "'>" + title + "</option>");
          if (category_orl == target2_orl) { option.attr("selected","true"); }
          category2_target.append(option);
        }

      }
    }


  };

})(jQuery);

