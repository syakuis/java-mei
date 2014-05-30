var swfu = [ ]; // SWFUpolad


(function($) {

  jQuery.file = {

    swfupload : function(options) {

      var settings = {
        ele_file : '#form #file',
        ele_file_orl : '#form #file_orl',
        ele_preview : '#form #file_preview',
        ele_file_size : '#form #file_size',
        upload_url: _mei_path_relative + "/procFileUpload.me",
        post_params: {
          file_upload_multi : '0'// 0 : false , 1 : true
        },

        file_upload_multi : false,
        file_post_name : 'file_upload',
        file_size_limit : 1024 * 1024, // 10MB
        file_types : "*.*",
        file_types_description : "",
        file_upload_unlimited : true , // 무제한 여부
        file_upload_limit : "0",
        file_queue_limit : "0",

        // Event Handler Settings (all my handlers are in the Handler.js file)
        file_dialog_start_handler : fileDialogStart, // 첨부파일 버튼 누를때 호출
        file_queued_handler : fileQueued, // 첨부파일 선택 후 호출
        file_queue_error_handler : fileQueueError, // 첨부파일 선택 후 에러가 발생할 때 호출
        file_dialog_complete_handler : fileDialogComplete, // 선택 된 첨부파일이 업로드가 완료되었을때 호출
        upload_start_handler : uploadStart, // 첨부파일 업로드 시작할때 호출
        upload_progress_handler : uploadProgress, // 첨부파일 업로드가 진행될때 호출
        upload_error_handler : uploadError, // 첨부파일 업로드 에러가 발생할 때 호출
        upload_success_handler : uploadSuccess, // 첨부파일 업르드가 성공 되었을 때 호출
        upload_complete_handler : uploadComplete, // 첨부파일 업로드가 완료 되었을 때 호출

        /*
        button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
        button_cursor: SWFUpload.CURSOR.HAND,
        */

        // Button Settings
        button_action : -100, // SWFUpload.BUTTON_ACTION.SELECT_FILES || -100
        button_image_url : _mei_path_relative + "/modules/file/ftl/images/XPButtonNoText_61x24.png",
        button_text : "첨부파일",
        button_text_style : "text-align:center;color: #000000; font-size: 16pt;",
        button_text_top_padding : 5,
        button_text_left_padding : 4,
        button_placeholder_id : "swfu_button",
        button_width: 64,
        button_height: 24,
        //button_window_mode : 'opaque',
        button_cursor: -2,
        
        // Flash Settings
        flash_url : _mei_path_relative + "/modules/file/js/swfupload.swf",
        
        custom_settings : {
          progressTarget : null, // swfu_progress
          cancelButtonId : null //swfu_cancle
        },
        
        // Debug Settings
        debug: false
      };

      if (options.file_upload_multi) {
        options.post_params.file_upload_multi = "1";
        options.button_action = -110;
      } else {
        options.post_params.file_upload_multi = "0";
        options.button_action = -100;
      }

      return new SWFUpload(jQuery.extend(settings,options));

    },

    setDeleteLimit : function(swfu,count) {
      var file_upload_limit = parseInt(swfu.settings.file_upload_limit);
      if (file_upload_limit > -1 && !swfu.settings.file_upload_unlimited) { 
        file_upload_limit = file_upload_limit + count;
        swfu.setFileUploadLimit(file_upload_limit);
      }
      console.debug(file_upload_limit);
    },

    deleteSwfupload : function(swfu,ckeditor) {
      var ele_file = swfu.settings.ele_file;
      var ele_file_orl = swfu.settings.ele_file_orl;

      var file_upload_multi = swfu.settings.file_upload_multi;
      var module_orl = swfu.settings.post_params.module_orl;
      var target_orl = swfu.settings.post_params.target_orl;
      var sid = swfu.settings.post_params.sid;
      var member_orl = swfu.settings.post_params.member_orl;

      var file_orl = "";

      if (file_upload_multi) {
        var cnt = 0;
        $(ele_file + ' option:selected').each(function(i){
          var file_info = eval("(" + $(this).val() + ")");
          file_orl += file_info.file_orl + "|";
          cnt++;
        });

        if (cnt == 0) { alert("파일이 없습니다."); return false; }
        file_orl = file_orl.replace(/\|$/,'');

        if (!confirm("삭제하시겠습니까?")) return false;

        if (!jQuery.ja.isEmpty(ckeditor)) { this.editor_file_remove(swfu,ckeditor); }

        jQuery.ajax({
          url: _mei_path_relative + '/procFileDelete.me',
          type: 'post',
          data: _mei_gb_params + '&file_orl=' + file_orl,
          error : function(xhr, status, error) {
            if (xhr.readyState != 4 && xhr.status != 200) { alert("오류가 발생하였습니다." + "\n[ajax error]" + xhr.readyState + "JA" + xhr.status); }
          },
          success : function(xml) {
            if (jQuery(xml).find('error').text() != "true") {

//              if ($(ele_file + ' option').length == 0) {
//                jQuery(ele_file_orl).val('');
//                swfu.setPostParams($.extend(swfu.settings.post_params , { file_idx : '' }));
//              }
              jQuery(swfu.settings.ele_preview).html('');

              // SWFUpload cancel
              $(ele_file + ' option:selected').each(function(i){
                var file_info = eval("(" + $(this).val() + ")");
//                swfu.cancelUpload(file_info.file_id, true);
                $(this).remove(); 
              });
              jQuery.file.setDeleteLimit(swfu,cnt);
              jQuery.file.size_sum(swfu);
            } else {
              alert(jQuery(xml).find('message').text());
              return false;
            }
          }

        });

      } else {

        var file_info = eval("(" + jQuery(ele_file_orl).val() + ")");
        if (file_info.file_orl == "") { alert("파일이 없습니다."); return false; }

        var file_info = eval("(" + jQuery(ele_file_orl).val() + ")");

        if (!confirm("삭제하시겠습니까?")) return false;
        
        if (!jQuery.ja.isEmpty(ckeditor)) { this.editor_file_remove(swfu,ckeditor); }

        jQuery.ajax({
          url: _mei_path_relative + '/procFileDelete.me',
          type: 'post',
          data: _mei_gb_params + '&file_orl=' + file_info.file_orl,
          error : function(xhr, status, error) {
            if (xhr.readyState != 4 && xhr.status != 200) { alert("오류가 발생하였습니다." + "\n[ajax error]" + xhr.readyState + "JA" + xhr.status); }
          },
          success : function(xml) {
            if (jQuery(xml).find('error').text() != "true") {
              jQuery(ele_file).val('');
              jQuery(ele_file_orl).val('');

              // SWFUpload cancel
              swfu.cancelUpload(file_info.file_id, true);

              swfu.setPostParams($.extend(swfu.settings.post_params , { file_orl : '' }));

            } else {
              alert(jQuery(xml).find('message').text());
              return false;
            }
          }

        });

      }


    },

    download : function(url) {
      location.href = 'getFileDownload.me' + url;
    },

    file_link : function(file_info) {
      var file_orl = file_info.file_orl;
      if (jQuery.ja.isEmpty(file_orl)) { return null; }
      return '/' + _mei_path_relative + 'getFileDownload.me?' + _mei_gb_params + "&file_orl=" + file_orl
    },

    preview : function(swfu) {
      try {
        var ele_file = swfu.settings.ele_file;
        var preview = jQuery(swfu.settings.ele_preview);
        var tag = "";
        var file_info = eval("(" + jQuery(ele_file + ' option:selected').val() + ")");
        var filename = file_info.re_file;
        var file = this.file_link(file_info);
        if (jQuery.ja.isEmpty(filename)) { return false; }

        if(/\.swf$/i.test(filename)) {
          tag = "<embed src=\""+ file+"\" width=\"100%\" height=\"100%\" type=\"application/x-shockwave-flash\"></embed>";
        } else if(/\.(wmv|avi|mpg|mpeg|asx|asf|mp3)$/i.test(filename)) {
          tag = "<embed src=\""+ file+"\" width=\"100%\" height=\"100%\" autostart=\"true\" Showcontrols=\"0\"></embed>";
        } else if(/\.(jpg|jpeg|png|gif)$/i.test(filename)) {
          tag = "<img src=\""+ file +"\" width=\"100%\" height=\"100%\" border=\"0\" />";
        }

        preview.html(tag);
      } catch (e) {  }

    },

    tag_view : function(file_info) {
      var file_orl = file_info.file_orl;
      var filename = file_info.file;
        var file = this.file_link(file_info);
      if (jQuery.ja.isEmpty(filename)) { return null; }

      var tag = "";

      if(/\.swf$/i.test(filename)) {
        tag = "<div><embed class='file" + file_orl + "' src=\""+ file+"\" type=\"application/x-shockwave-flash\"></embed>" + filename + "</div>";
      } else if(/\.(wmv|avi|mpg|mpeg|asx|asf|mp3)$/i.test(filename)) {
        tag = "<div><embed class='file" + file_orl + "' src=\""+ file+"\" autostart=\"true\" Showcontrols=\"0\"></embed>" + filename + "</div>";
      } else if(/\.(jpg|jpeg|png|gif)$/i.test(filename)) {
        tag = "<img class='file" + file_orl + "' src=\""+ file+"\" border=\"0\" />";
      } else {
        tag = "<a class='file" + file_orl + "' href='" + file + "'>" + filename + "</a>";
      }

      return tag;
    },

    size_sum : function(swfu) {
      var ele_file = swfu.settings.ele_file;
      var ele_file_size = swfu.settings.ele_file_size;
      var file_upload_multi = swfu.settings.file_upload_multi;
      var size = 0;

      if (file_upload_multi) {
        $(ele_file + ' option').each(function(i){
          var file_info = eval("(" + $(this).val() + ")");
          size += parseInt(file_info.file_size);
        });
      }

      var file_size_unit = jQuery.mei.filesize_unit(size);
      jQuery(ele_file_size).text(file_size_unit);
    },

    editor_file_input : function(swfu,ckeditor) {  } ,
    editor_file_remove : function(swfu,ckeditor) {  }

  };

})(jQuery);
