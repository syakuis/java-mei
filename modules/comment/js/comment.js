(function($) {

  jQuery.comment = {

    _set_params : _mei_gb_params , 

    form : function(target,params) {

      var url = "dispCommentList.me" + params;

      jQuery(target).html("<p style='text-align:center;font-size:24px;font-family: 돋움,Dotum,Tahoma,Geneva,sans-serif;'><span class='loadingGIF32'></span> 잠시만 기다려주세요.</p>").show();

      jQuery.ajax({
        url: url,
        cache : false,
        success: function(data){
          jQuery(target).html(data);
        }
      });

    },

    update : function(comment_orl) {
      var j = this;
      var comment_update = "c_content_" + comment_orl;
      
      var form_comment_update = "form_comment_update_" + comment_orl;
      if (jQuery('#'+form_comment_update).is('form')) return;
      
      // 내용 보유
      var j_comment_update = jQuery('#' + comment_update);
      var content = j_comment_update.html();
      j_comment_update.empty();

      var source = jQuery("#comment_update").clone();

      jQuery("form",source).attr("id",form_comment_update).attr("method","post").attr("action","procCommentUpdate.me");
      var content_script = content.replace(/<br>|<br\/>|<br \/>/ig,"\n"); // 줄바꿈 변경
      jQuery('#content',source).val(content_script);
      jQuery('#comment_orl',source).val(comment_orl);
      j_comment_update.append("<span style='display:none;'>" + content + "</span>");

      jQuery("#update_btn",source).click(function() { jQuery.comment.save("#" + form_comment_update); });
      jQuery("#btn_comment_update_close",source).click(function() { j.update_close(comment_orl); });
      
      j_comment_update.append(source.show());

    },

    update_close : function(comment_orl) {
      var comment_update = "c_content_" + comment_orl;
      var j_comment_update = jQuery('#' + comment_update);
      var content = jQuery('span',j_comment_update).html();
      j_comment_update.empty();
      j_comment_update.append(content);
    },

    reply : function(comment_orl,reply_group,reply_depth,reply_seq) {
      var j = this;

      var comment_reply = "comment_reply_" + comment_orl;
      var form_comment_reply = "form_comment_reply_" + comment_orl;
      if (jQuery('#'+form_comment_reply).is('form')) return;

      var source = jQuery("#comment_reply").clone();

      jQuery("#parent_orl",source).val(comment_orl);
      jQuery("#reply_group",source).val(reply_group);
      jQuery("#reply_depth",source).val(reply_depth);
      jQuery("#reply_seq",source).val(reply_seq);
      jQuery("form",source).attr("id",form_comment_reply).attr("method","post").attr("action","procCommentInsert.me");

      jQuery("#reply_btn",source).click(function() { jQuery.comment.save("#" + form_comment_reply); });
      jQuery("#btn_comment_reply_close",source).click(function() { j.reply_close(comment_orl); });
      
      jQuery("#comment_" + comment_orl).append(source.attr("id",comment_reply).show());

    },

    reply_close : function(comment_orl) {
      var comment_reply = "comment_reply_" + comment_orl;
      jQuery("#" + comment_reply).remove();
    },

    save : function(form) {

      jQuery(form).jaAction({ 
        filter : [
          { target : "#content", params : "&filter=notnull&title=내용" }
        ],
        param : this._set_params + '&sns_url=' + escape(document.URL) + '&sns_description=' + document.title, 
        ask : 'save',
        afterSend : function() {
          location.reload();
        }
      });
      
    },

    del : function(comment_orl) {
      jQuery('#form_comment_delete').jaAction({ 
        param : this._set_params + '&comment_orl=' + comment_orl, 
        ask : 'del' ,
        afterSend : function() {
          location.reload();
        }
      });
    },

    text_count : function(its,target) {
      jQuery(target).text(0);

      jQuery(its).bind("input blur keyup keydown paste" , function() {
          var str = jQuery(this).val();
//          var count = 130 - parseInt(String(str).length);
//          if (count < 0){ jQuery(target).css("color","red"); } else { jQuery(target).css("color","#c5c3c3"); }
          jQuery(target).text(String(str).length);
      });

    }

  }
})(jQuery);