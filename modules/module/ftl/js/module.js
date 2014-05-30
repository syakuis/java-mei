(function($) {

  jQuery.module = {

    return_mid : '',
    return_module_orl : '',


    // 모듈검색 팝업 호출
    _open_search : function(options) {
      this.return_mid = options.return_mid;
      this.return_module_orl = options.return_module_orl;
      var sch_module = "";
      if (!$.ja.isEmpty(options.module)) {
        sch_module = "&sch_module=" + options.module;
      }

      jQuery.ja.popup({
        url : './dispModuleAdminPopupList.me?' + _mei_gb_params + sch_module,
        width : 500,
        height : 500,
        center : true,
        scrollbars : 'yes'
      });
    },

    _open_return : function(data) {
      jQuery(this.return_mid).val(data.mid);
      jQuery(this.return_module_orl).val(data.module_orl);
    },

    _grant_zone_show : function() {

      jQuery(".grant_selectbox").each( function() {
        var id = "#zone_"+this.name.replace(/_default$/,'');
        if(!jQuery(this).val()) jQuery(id).show();
        else jQuery(id).hide();
      });

    },
    
    _grant_value : function(target,val) {
      var t = "#" + target + "_default";
      var z = "#grant_" + target + "_";
      var zone = "#zone_" + target;

      var count = val.length;

      for (var i = 0; i < count; i++) {
        var value = parseInt(val[i]);

        if (value > 0) {
          jQuery.ja.setValue(t,"");
          var z_obj = z + value;
          jQuery.ja.setValue(z_obj,value);
          jQuery(zone).show();
        } else {
          jQuery.ja.setValue(t,value);
          jQuery(zone).hide();
        }

      }
    },
    _grant_values : function(obj) {
      var access = obj.access;
      this._grant_value("access",access);
      var list = obj.list;
      this._grant_value("list",list);
      var view = obj.view;
      this._grant_value("view",view);
      var write = obj.write;
      this._grant_value("write",write);
      var comment = obj.comment;
      this._grant_value("comment",comment);
      var manager = obj.manager;
      this._grant_value("manager",manager);

    }

  }
})(jQuery);
