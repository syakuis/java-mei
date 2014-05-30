(function($) {

  jQuery.menu = {

    selectbox : function(target,name,first) {
      target = jQuery(target);

      $.ajax({
        url : 'procMenuXml.me',
        data : 'mod=menu',
        success : function(xml) {
          var ele = jQuery("<select name='" + name + "' id='" + name + "' ></select>");

          if (!jQuery.ja.isEmpty(first)) { ele.append("<option value=''>" + first + "</option>"); }

          $(xml).find('item').each(function (i) {
            var title = $(this).find("title").text();
            var menu_orl = $(this).find("menu_orl").text();
            ele.append("<option value='" + menu_orl + "'>" + title + "</option>");
          });

          target.append(ele);

        }

      });
    },

    // simpletree
    simpletree : function(options) {

      $.ajax({
        url : options.url,
        data : options.data,
        async : false,
        success : function(xml) {
          var item = $(xml).find('item');
          var count = item.length;

          $(options.target + ' > li').remove(); 

          var root = $("<li class='root' id='menu_0'><a href='" + options.root_path + "'><span>" + options.title +"</a></span></li>");
          $(options.target).append(root);

          $(xml).find('item').each(function (i) {

            var menu_item_orl = $.ja.defString($(this).attr("menu_item_orl"),"0");
            var parent_orl = $.ja.defString($(this).attr("parent_orl"),"0");
            var url =$.ja.defString($(this).attr("url"),"");
            var name = $.ja.defString($(this).attr("name"),"");
            var open_window = $.ja.defString($(this).attr("open_window"),"0");
            var expand = $.ja.defString($(this).attr("expand"),"0");

            var node = $("<li id='" + options.node + menu_item_orl + "'></li>");
            if (expand == "1") { node.addClass("open"); }
            var attr = $("<span>" + name +"</span>");

            if (!$.ja.isEmpty(url)) { var attr = $("<a href='" + url + "'><span>" + name +"</span></a>"); }
            if (open_window == "1") { attr.attr("target","_blank"); }
            node.append(attr);

            if(parent_orl > 0) {
              if($('#' + options.node +parent_orl+'>ul').length == 0) { $('#' + options.node +parent_orl).append($('<ul>')); }
              $('#' + options.node +parent_orl+'> ul').append(node);
            } else {
              if($(options.target + ' > li > ul').length == 0) { 
                $("<ul>").appendTo(options.target + ' > li'); 
              }
              $(options.target + ' > li > ul').append(node);
            }

          });

        if ($.isFunction(options.afterExecute)) {
          options.afterExecute(this);
        };
      }

    });

  } // simpletree

  }
})(jQuery);

/**
  jQuery(function () {
    jQuery.menu.simpletree({
      url : _mei_http_cache + '/mei_menu_tree.xml',
      data : '',
      node : 'menu_',
      target : '#tree-menu',
      title : 'SYSTEM',
      root_path : '/',

      afterExecute : function() {
        jQuery('#tree-menu').simpleTree({
          autoclose: false,
          afterMove:false,
          afterAjax:false,
          animate:false,
          drag:false
        })
      }

    });

  });

*/