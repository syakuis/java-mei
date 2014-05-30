
/**
* jQuery simpleTreeExtend
* @depend jquery.action.js
* @brief simpleTree 의 커스텀 기능을 확장한 플러그인

* Copyright (c) 2010 Seok Kyun. Choi. 최석균
* GNU Lesser General Public License
* http://www.gnu.org/licenses/lgpl.html
*/

(function($) {
  $.simpleTreeExtend = {
    form : null, // simpleTree form element
    simpleTreeOptions : null, // simpleTree default setting
    target : '.simpleTree', // simpleTree element selecter
    node : 'simpleTreeNode_', // simpleTree element child node selecter
    loading : true,


    form_input : null,

    idx : 'simpleTree_idx', // simpleTree node idx
    parent_idx : 'simpleTree_parent_idx', // simpleTree node parent_idx
    num : 'simpleTree_num',
    name : 'simpleTree_name',

    title : '최상위', // root tree title
    custom : true, // add, del, modify control
    extend : true, // row tree extend

    ajax_data : '',
    ajax_move_url : '',
    ajax_url_load : '',

    max_node_depth : 10, // 하위 노드 최대 생성수

    _ajaxLoad : function() {
      var obj = this;
      jQuery.ajax({
        url : obj.ajax_url_load,
        data : obj.ajax_data,
        type : 'post',
        success : function(xml) {
          obj._build(xml);
        }
      });
    },

    _ajaxSave : function() {
    },

    _nodeSelect : function(is,id) {
    },

    _build : function(xml) {
      var item = $(xml).find('item');
      var count = item.length;
      var obj = this;

      $(this.target + ' > li').remove(); 

      var root = $("<li class='root' id='" + this.node +"0'><span>" + this.title +"</span></li>");

      if (this.custom) {
        $('<a href="#"><img src="/common/ui.simple.tree/images/iconAdd.gif" /></a>').bind("click",function(e){
          $('#' + this.node + '0 > span').addClass("select");
          $.simpleTreeExtend._add('0','0');
          return false;
        }).bind("focusout",function(e){
          $('#' + this.node + '0 > span').removeClass("select");
        }).appendTo(root);
      }

      $(this.target).append(root);

      $(xml).find('item').each(function (i) {

        var idx = $.ja.defString($(this).attr(obj.idx),"");
        var name = $(this).attr(obj.name);
        var parent_idx = $.ja.defString($(this).attr(obj.parent_idx),"0");
        var num = $.ja.defString($(this).attr(obj.num),"0");

        var node = $("<li id='" + obj.node + idx + "' class='open'><span>" + "("+idx+") " + name +"</span></li>");

        if (obj.custom) {

          if (obj.extend) {
            $('<a href="#" class="add"><img src="/common/ui.simple.tree/images/iconAdd.gif" /></a>').bind("click",function(e){
              $.simpleTreeExtend._add(idx,num);
              return false;
            }).appendTo(node); 
          }

          $('<a href="#" class="modify"><img src="/common/ui.simple.tree/images/iconModify.gif" /></a>').bind("click",function(e){
            $.simpleTreeExtend._modify(idx);
            return false;
          }).appendTo(node); 

          $('<a href="#" class="del"><img src="/common/ui.simple.tree/images/iconDel.gif" /></a>').bind("click",function(e){
            $.simpleTreeExtend._del(idx);
            return false;
          }).appendTo(node); 
        }

        if(parent_idx > 0) {
          if($('#' + obj.node +parent_idx+'>ul').length == 0) { $('#' + obj.node +parent_idx).append($('<ul>')); }
          $('#' + obj.node +parent_idx+'> ul').append(node);
        } else {
          if($(obj.target + ' > li > ul').length == 0) { $("<ul>").appendTo(obj.target + ' > li'); }
          $(obj.target + ' > li > ul').append(node);
        }

      });

      $(this.target + " li").each(function(){
        if($(this).parents('ul').size() > obj.max_node_depth) { $("a.add",this).hide(); }
        if($(">ul",this).size()>0) { $(">a.del",this).hide(); }
      });

      this._simpleTree();

    },

    _add : function(idx,num) {
    },

    _modify : function(idx) {
    },

    _del : function(idx) {
    },

    _simpleTree : function() {
      var obj = this;

      $(this.target).simpleTree($.extend({
        autoclose: false,
        drag:true,
        afterClick:function(){ },
        afterDblClick:function(){ },

        afterMove : function(destination, source, pos){

          if(destination.size() == 0) {
            obj._ajaxLoad();
            return;
          }

          var parent_idx = destination.attr('id').replace(/.*_/g,'');
          var idx = source.attr('id').replace(/.*_/g,'');
          var num = pos == 0 ? 0: source.prevAll("li:not(.line)").get(0).id.replace(/.*_/g,'');
          jQuery.ajax({
            url : obj.ajax_move_url,
            data : obj.ajax_data + '&' + obj.parent_idx + '=' + parent_idx + '&' + obj.idx + '=' + idx + '&'+ obj.num + '=' + num,
            type : 'post',
            success : function() {
              obj._ajaxLoad();
            }
          });
        },

        afterAjax:function() { },
        animate:true,
        docToFolderConvert:true
      },this.simpleTreeOptions));
    },

    _init : function() {

      // 로딩표시
      if (this.loading) {
        $(document).bind('ajaxStart', function() {
          jQuery.ja._loading('',null);
        }).bind('ajaxStop', function() {
          jQuery.ja._loading('hidden',null);
        });
      }

      this._ajaxLoad();
    }

  };

  $.fn.simpleTreeExtend = function(options) {
    $.extend($.simpleTreeExtend,{form : this , form_input : this },options);
    $.simpleTreeExtend._init();

    return this;
  };

})(jQuery);