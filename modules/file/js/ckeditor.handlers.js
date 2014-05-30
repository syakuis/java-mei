(function(jQuery) {

$.extend(true,$.file , { 


  editor_file_input : function(swfu,ckeditor) {
    if (ckeditor.mode != 'wysiwyg') { alert('텍스트(소스) 모드에서는 사용할 수 없습니다.'); return false; }

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

      ckeditor.insertHtml(tag);
      ckeditor.updateElement();

    } catch (e) { }
  } , 

  editor_file_remove : function(swfu,ckeditor) {
    try {
      var file_upload_multi = swfu.settings.file_upload_multi;
      var tag = jQuery("<div>" + ckeditor.getData() + "</div>");

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

      ckeditor.setData(tag.html());
      ckeditor.updateElement();

    } catch (e) { }

  }

} );

})(jQuery);