(function($) {
  
  var snsauth;

  jQuery.snsauth = {
    sns_btn_twitter : '#sns_btn_twitter', // twitter button id
    sns_btn_yozm : '#sns_btn_yozm', // yozm button id
    sns_btn_me2day : '#sns_btn_me2day', // me2day button id
    sns_btn_facebook : '#sns_btn_facebook', // facebook button id
    sns_menu_list : '#sns_menu_list', // menu list id
    sns_menu_list_clone : '#sns_menu_list_clone', // menu list id
    sns_me_photo : '#sns_me_photo', // sns user photo
    sns_me_name : '#sns_me_name',

    sns_main_target : '#sns_main',

    sns_main : '',

    yozm_uid : '',
    yozm_nickname : '',
    yozm_photo : '',
    yozm_send : '',

    twitter_uid : '',
    twitter_nickname : '',
    twitter_photo : '',
    twitter_send : '',

    me2day_uid : '',
    me2day_nickname : '',
    me2day_photo : '',
    me2day_send : '',

    facebook_uid : '',
    facebook_nickname : '',
    facebook_photo : '',
    facebook_send : '',

    settings : function(settings) {

      snsauth = jQuery.extend(true , this , settings);


    },

    initialize : function(settings) {
      this.settings(settings);

      var obj = this;

      this.sign_button(this.yozm_uid,'YOZM');
      this.sign_button(this.twitter_uid,'TWITTER');
      this.sign_button(this.me2day_uid,'ME2DAY');
      this.sign_button(this.facebook_uid,'FACEBOOK');

      if (!jQuery.ja.isEmpty(this.sns_main)) {
        this.photo_change(this.sns_main);
      }

    },

    sns_change : function(sns) {

      var obj = this;

      jQuery.ajax({
        dataType : 'xml',
        url : 'procSnsAuthMainUpdate.me?sns_name=' + sns,
        success : function(xml) {
          var error = $(xml).find('error').text();
          if (error == 'false') {
            obj.photo_change(sns);
          }
        }

      });

    },

    photo_change : function(sns) {
      switch (sns) {
      case 'TWITTER' :
        jQuery(this.sns_me_photo).attr("src",this.twitter_photo);
        jQuery(this.sns_me_name).text(this.twitter_nickname);
      break;
      case 'YOZM' :
        jQuery(this.sns_me_photo).attr("src",this.yozm_photo);
        jQuery(this.sns_me_name).text(this.yozm_nickname);
      break;
      case 'ME2DAY' :
        jQuery(this.sns_me_photo).attr("src",this.me2day_photo);
        jQuery(this.sns_me_name).text(this.me2day_nickname);
      break;
      case 'FACEBOOK' :
        jQuery(this.sns_me_photo).attr("src",this.facebook_photo);
        jQuery(this.sns_me_name).text(this.facebook_nickname);
      break;
      
      }

    },

    sign_button : function(uid,sns) {
      var obj = this;

      var sns_btn;
      if (sns == 'YOZM') {
        sns_btn = this.sns_btn_yozm;
      } else if (sns == 'TWITTER') {
        sns_btn = this.sns_btn_twitter;
      } else if (sns == 'ME2DAY') {
        sns_btn = this.sns_btn_me2day;
      } else if (sns == 'FACEBOOK') {
        sns_btn = this.sns_btn_facebook;
      }

      if (!jQuery.ja.isEmpty(uid)) {
        var src = jQuery(sns_btn).attr("src");
        src = src.replace('_off','_on');
        jQuery(sns_btn).attr("src",src);

        jQuery(sns_btn).click(function() {

          if (jQuery(obj.sns_menu_list_clone).is('div') ) {

            jQuery(obj.sns_menu_list_clone).remove();
          } else {
            obj.menu_list(sns);
          }

        });
      } else {
        jQuery(sns_btn).click(function() {
          obj.signin(sns);
        });
      }

    },

    menu_list : function(sns) {
      var obj = this;

      var sns_btn;
      var sns_send;
      if (sns == 'YOZM') {
        sns_btn = this.sns_btn_yozm;
        sns_send = this.yozm_send;
      } else if (sns == 'TWITTER') {
        sns_btn = this.sns_btn_twitter;
        sns_send = this.twitter_send;
      } else if (sns == 'ME2DAY') {
        sns_btn = this.sns_btn_me2day;
        sns_send = this.me2day_send;
      } else if (sns == 'FACEBOOK') {
        sns_btn = this.sns_btn_facebook;
        sns_send = this.facebook_send;

      }

      var menu = jQuery(this.sns_menu_list).clone().show();
      jQuery(menu).attr('id',obj.sns_menu_list_clone.replace('#',''));
      jQuery('#sns_btn_main',menu).click(function() {
        obj.sns_change(sns);
      }).removeAttr('id');
      
      if (sns_send == 'Y') {
        jQuery('#sns_btn_not_send',menu).click(function() {
          obj.post_send(sns,'N');
        }).removeAttr('id');
        jQuery('#sns_btn_send',menu).hide();
      } else {
        jQuery('#sns_btn_send',menu).click(function() {
          obj.post_send(sns,'Y');
        }).removeAttr('id');
        jQuery('#sns_btn_not_send',menu).hide();
      }

      jQuery('#sns_btn_close',menu).click(function() {
        if (confirm("연결을 끊으시겠습니까?")) {
          obj.signout(sns);
        }
      }).removeAttr('id');

      jQuery(menu).click(function() { 
        jQuery(this).remove();
      });

      jQuery(sns_btn).after(menu);

    },

    signin : function(sns) {
      jQuery.ja.popup({ name : 'sns_access' , 'url' : '/dispSnsAuthSignIn.me?sns_name=' + sns , width:800, height:600, resizable : 'yes' });
    },

    signout : function(sns) {

      jQuery.ajax({
        dataType : 'xml',
        url : 'procSnsAuthSignOut.me?sns_name=' + sns,
        success : function(xml) {
          var error = $(xml).find('error').text();
          if (error == 'false') {
            document.location.reload();
          }
        }

      });

    },
    post_send : function(sns,post_send) {
      jQuery.ajax({
        dataType : 'xml',
        url : 'procSnsAuthPostSend.me?sns_name=' + sns + '&post_send=' + post_send,
        success : function(xml) {
          var error = $(xml).find('error').text();
          if (error == 'false') {
            document.location.reload();
          }
        }
      });
    }

  }
})(jQuery);
