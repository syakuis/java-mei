(function($) {

jQuery.extend(true,jQuery.file , { 


  editor_file_input : function(swfu,editor) {
    try {
      var file_upload_multi = swfu.settings.file_upload_multi;
      var tag = "";

      if (file_upload_multi) {
        var ele_file = swfu.settings.ele_file;
        jQuery(ele_file + ' option:selected').each(function(i){
          var file_info = eval("(" + $(this).val() + ")");

          tag += jQuery.file.tag_view(file_info);
        });

      } else {
        var ele_file = swfu.settings.ele_file_orl;
        var value = jQuery(ele_file).val();
        var file_info = eval("(" + value + ")");
        tag = jQuery.file.tag_view(file_info);
      }

      editor.exec("PASTE_HTML", [tag]);
      editor.exec("UPDATE_CONTENTS_FIELD", []);

    } catch (e) { }
  } , 

  editor_file_remove : function(swfu,editor) {
    try {

      var file_upload_multi = swfu.settings.file_upload_multi;
      var tag = jQuery("<div>" + editor.getIR() + "</div>");

      if (file_upload_multi) {
        var ele_file = swfu.settings.ele_file;
        $(ele_file + ' option:selected').each(function(i){
          var file_info = eval("(" + $(this).val() + ")");
          var value = jQuery(ele_file + ' option:selected').val();
          jQuery('.file' + file_info.file_orl,tag).remove();
        });
      } else {
        var ele_file = swfu.settings.ele_file_orl;
        var value = jQuery(ele_file).val();
        var file_info = eval("(" + value + ")");
        jQuery('.file' + file_info.file_orl,tag).remove();
      }

      editor.exec("SET_CONTENTS", [tag.html()] );
      editor.exec("UPDATE_CONTENTS_FIELD", []);

    } catch (e) { }

  }

} );

})(jQuery);