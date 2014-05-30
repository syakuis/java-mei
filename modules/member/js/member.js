(function($) {

  jQuery.member = {

    user_id_regx : function(reg) {
      $.ja.regional.regx.user_id = function(val) {
        return reg.test(val);
      }
    },
    user_name_regx : function(reg) {
      $.ja.regional.regx.user_name = function(val) {
        return reg.test(val);
      }
    },
    nickname_regx : function(reg) {
      $.ja.regional.regx.nickname = function(val) {
        return reg.test(val);
      }
    },

    login : function(url,ret_url) {

      var URL = document.URL;
      var parameters = location.search;

      var query = '';

      var login = "./dispMemberLoginForm.me?mid=member";

      if (jQuery.ja.isEmpty(url)) { url = login; }

      var reg_get = /\&ret\_url\=.*/;
      var reg_url = /dispMemberLoginForm\.me(.*)/;

      if (jQuery.ja.isEmpty(ret_url) ) {

        if ( !reg_url.test(URL) && !reg_get.test(parameters) ) {
          query = "&ret_url=" + encodeURIComponent(URL);
        }

      } else {

        if ( !reg_url.test(ret_url) ) {
          query = "&ret_url=" + ret_url;
        }

      }

      if (jQuery.ja.isEmpty(query) ) { query = "&ret_url=" + encodeURIComponent(_mei_host_URL); }

      if (/\?$/.test(url)) {
        query = query.replace(/^[&]/,'');
      }

      location.href = url + query;

    },

    // 로그아웃
    logout : function(url,ret_url) {
      jQuery.ajax({
        url : './procMemberLogout.me',
        type: "post",
        data: _mei_gb_params,
        success: function(xml) {

          location.href = document.URL;
        }

      });

    }, // 로그아웃

    login_confirm : function(url,ret_url) {

      if (confirm("로그인이 필요합니다. 로그인 화면으로 이동하시겠습니까?")) {
        this.login(url,ret_url);
      }

    },

    // 중복 체크
    // mode = 중복체크 형식 , func = 함수를 이용한 메세지 출력 (함수) , prepare 초기화 작업 (함수)
    repeat_check : function(mode,func,prepare) {
      var target_1 = null;
      var target_2 = null;
      var target_url = null;
      var target_filter = null;

      switch (mode) {
        case 'user_id' :
          target_1 = "#user_id";
          target_2 = "#user_id2";
          target_url = "./getMemberUserIdCheck.me";
          target_filter = "&filter=user_id&length=6,15&title=아이디";
        break;
        case 'nickname' : 
          target_1 = "#nickname";
          target_2 = "#nickname2";
          target_url = "./getMemberNickNameCheck.me";
          target_filter = "&filter=notnull&length=2,20&filter=nickname&title=닉네임";
        break;
        case 'email' : 
          target_1 = "#email";
          target_2 = "#email2";
          target_url = "./getMemberEmailCheck.me";
          target_filter = "&filter=notnull&filter=email&title=이메일";
        break;

      }

      jQuery("#form " + target_1).on("blur" , function() {
         jQuery('#form').jaAction({
          filter : [
            { target : target_1 , params : target_filter , message : func }
          ],
          formAttr : 'action=' + target_url ,
          direct : _mei_gb_params + '&member_orl=' + jQuery('#form #member_orl').val() + '&' + mode + '=' + jQuery("#form " + target_1).val() ,
          loading : false,

          beforeSend : function() {

            if ( jQuery.isFunction( prepare ) ) {
              prepare();
            }

            var val = jQuery('#form ' + target_1).val();
            var val2 = jQuery('#form ' + target_2).val();

            if ( (val == val2) && ( !$.ja.isEmpty(val) && !$.ja.isEmpty(val2) ) ) {
              return false;
            }
          },

          setAjax : {
            success : function(xml) {
              var data = xml2json(xml, " ");
              data = eval("(" + data + ")");
              data = data.data.item;
              data.error = (data.error == 'true') ? true : false;
              data.target = jQuery('#form ' + target_1);
              func(data);
              
              if (data.error) {
                jQuery('#form ' + target_2).val('');
              } else {
                jQuery('#form ' + target_2).val( jQuery('#form ' + target_1).val() );
              }

            }
          }
          
        });


      });

    }

  };

})(jQuery);