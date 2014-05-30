(function($) {

  jQuery.category = {
    
    cache_load : function(module_orl,type) {
      var path = _mei_path_cache + '/category/category_' + module_orl + '.xml';
      if (type == 1) { path = _mei_path_cache + '/category/category_list_' + module_orl + '.xml'; }
      var data;
       jQuery.ajax({
        url : path,
        async : false,
        success : function(xml) { data = xml; }
      });

      return data;

    },

    select_box : function(target,module_orl,val) {

      var xml = this.cache_load(module_orl,1);
      
      var json = xml2json(xml, " ");
      json = eval("("+json+")");

      jQuery.xml2selectbox.selectbox_to_once({
        xml : json , 
        title_name : 'title',
        target_name : 'category_orl',
        target_orl : target , 
        val : val
      });

    },
    
    //<div><ul id="tree" class="simpleTree"></ul></div>
    select_tree : function(options) {
      var module_orl = options.module_orl;
      var set_category_orl = options.category_orl;
      var target = options.target;

      var node_name = options.node_name;
      var root_title = options.root_title;

      $(target + ' > li').remove();
      var root = $("<li class='root' id='" + node_name + "0'>" + root_title +"</span></li>");
      $(target).append(root);

      var xml = this.cache_load(module_orl,0);
      $(xml).find('item').each(function (i) {
        var module_orl =$.ja.defString($(this).attr("module_orl"),"0");
        var parent_orl = $.ja.defString($(this).attr("parent_orl"),"0");
        var category_orl = $.ja.defString($(this).attr("category_orl"),"0");
        var title = $.ja.defString($(this).attr("title"),"");
        var rdate = $.ja.defString($(this).attr("rdate"),"0");

        var depth = $.ja.defString($(this).attr("depth"),"0");
        var category_orls = $.ja.defString($(this).attr("category_orls"),"");

        var data = { module_orl : module_orl , parent_orl : parent_orl, category_orl : category_orl , title : title , rdate : rdate , depth : depth , category_orls : category_orls };

        var node = $("<li id='" + node_name + category_orl + "' class='open'></li>");
        var attr = $("<span>" + title +"</span>").click(function () { options.after_func(data); });
        node.append(attr);

        if(parent_orl > 0) {
          if($('#' + node_name +parent_orl+'>ul').length == 0) { $('#' + node_name +parent_orl).append($('<ul>')); }
          $('#' + node_name +parent_orl+'> ul').append(node);
        } else {
          if($(target + ' > li > ul').length == 0) { 
            $("<ul>").appendTo(target + ' > li'); 
          }
          $(target + ' > li > ul').append(node);
        }

      });

      $(target).simpleTree({
        autoclose: false,
        drag:false,
        afterClick: false,
        afterDblClick: false,
        afterMove : false,
        afterAjax: false,
        animate:true,
        docToFolderConvert:true
      });


    },


    select_view : function(options) {
      var module_orl = options.module_orl;
      var set_category_orl = options.category_orl;
      var target = options.target;

      var node_name = options.node_name;
      var root_title = options.root_title;

      $(target + ' > ul').remove();

      var xml = this.cache_load(module_orl,0);
      $(xml).find('item').each(function (i) {
        var module_orl =$.ja.defString($(this).attr("module_orl"),"0");
        var parent_orl = $.ja.defString($(this).attr("parent_orl"),"0");
        var category_orl = $.ja.defString($(this).attr("category_orl"),"0");
        var title = $.ja.defString($(this).attr("title"),"");
        var rdate = $.ja.defString($(this).attr("rdate"),"0");

        var node = $("<li id='" + node_name + category_orl + "' ></li>");
        var attr = $("<span>" + title +"</span>");
        node.append(attr);
        
        if(parent_orl > 0) {
          if($('#' + node_name +parent_orl+'>ul').length == 0) { $('#' + node_name +parent_orl).append($('<ul>')); }
          $('#' + node_name +parent_orl+'> ul').append(node);
        } else {
          if($(target + ' > ul').length == 0) { 
            $("<ul>").appendTo(target); 
          }
          $(target + ' > ul').append(node);
        }

      });

    }

  };

})(jQuery);
