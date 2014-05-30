(function($) {

  jQuery.layout = {

    selectbox : function(target,name,first) {
      target = jQuery(target);

      $.ajax({
        url : _mei_http_cache + '/mei_layouts.xml',
        success : function(xml) {
          var ele = jQuery("<select name='" + name + "' id='" + name + "' ></select>");

          if (!jQuery.ja.isEmpty(first)) { ele.append("<option value=''>" + first + "</option>"); }

          $(xml).find('item').each(function (i) {
            var layout = $(this).find("layout").text();
            ele.append("<option value='" + layout + "'>" + layout+ "</option>");
          });

          target.append(ele);

        }

      });
    }

  }
})(jQuery);
