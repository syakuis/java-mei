/**
  Korean initialisation for the jQuery JavaScript Action extension.
  Written by Seok Kyun. Choi. 최석균 (http://syaku.tistory.com)
*/

(function($) {

  // xml sync : #cdate : text
  $.ja.regional['ko'] = {
    action : {

      filter : {
          notnull : { '#cdata' : '을(를) 입력하세요.' }

        , num_length : { '#cdata' : '은(는) {$num}자로 입력하세요.' }
        , length : { '#cdata' : '은(는) {$min}자 이상 {$max}자 이하로 입력하세요.' }
        , min_length : { '#cdata' : '은(는) {$min}자 이상으로 입력하세요.' }
        , max_length : { '#cdata' : '은(는) {$max}자 이하로 입력하세요.' }
        , num : { '#cdata' : '은(는) {$min} 이상 {$max} 이하 수로 입력하세요.' }
        , max_num : { '#cdata' : '은(는) {$max} 이하 수로 입력하세요.' }

        , num_select : { '#cdata' : '은(는) {$num} 개를 선택하세요.' }
        , min_select : { '#cdata' : '은(는) {$min} 개 이상으로 선택하세요.' }
        , max_select : { '#cdata' : '은(는) {$max} 개 이하로 선택하세요.' }
        , selected : { '#cdata' : '을(를) 최소 {$min}개 이상 {$max}개 이하로 선택하세요.' }
        , select : { '#cdata' : '을(를) 선택하세요.' }
        , all_select : { '#cdata' : '은(는) 모두 선택하세요.' }

        , notlike : { '#cdata' : '이(가) 일치하지 않습니다.' }
        , like : { '#cdata' : '이(가) 일치합니다.' }
        , email : { '#cdata' : '을(를) 올바르게 입력하세요. (예 : user@domain.com)' }
        , user_id : { '#cdata' : '을(를) 올바르게 입력하세요. 영문으로 시작하세요.' }
        , user_name : { '#cdata' : '을(를) 정확하게 입력하세요.' }
        , nickname : { '#cdata' : '을(를) 정확하게 입력하세요.' }
        , dot_ip : { '#cdata' : '을(를) 정확하게 입력하세요. (예 : 127.0.0.1)' }
        , http_url : { '#cdata' : '을(를) 정확하게 입력하세요. (예 : http://domain.co.kr)' }
        , url : { '#cdata' : '을(를) 정확하게 입력하세요. (예: domain.com / http:// 등 들어가면 안됩니다.)' }
        , hyphen_contact : { '#cdata' : '을(를) 연락처 형식으로 입력하세요. (예 : 02-1111-1111)' }
        , replay_contact : { '#cdata' : '을(를) 반복해서 같은 숫자를 입력할 수 없습니다.. (예 : 1111-1111-1111)' }
        , hyphen_jumin : { '#cdata' : '을(를) 정확하게 입력하세요. (예 : 123456-1234567)' }
        , ko : { '#cdata' : '을(를) 한글로 입력하세요.' }
        , ko_number : { '#cdata' : '을(를) 한글과 숫자로 입력하세요.' }
        , en : { '#cdata' : '을(를) 영문으로 입력하세요.' }
        , en_number : { '#cdata' : '을(를) 영문으로 입력하세요.' }
        , en_number_mix : { '#cdata' : '을(를) 영문/숫자 조합으로 입력하세요.' }
        , number : { '#cdata' : '을(를) 숫자로 입력하세요.' }
        , usable : { '#cdata' : '은(는) 사용할 수 있습니다.' }
        , unfit : { '#cdata' : '은(는) 사용할 수 없습니다.' }
      }

      , question : {
        save : { '#cdata' : '저장하시겠습니까?' }
        , insert : { '#cdata' : '등록하시겠습니까?' }
        , del : { '#cdata' : '삭제하시겠습니까?' }
        , update : { '#cdata' : '수정하시겠습니까?' }
        , execute : { '#cdata' : '진행하시겠습니까?' }
        , cancel : { '#cdata' : '취소하시겠습니까?' }
        , apply : { '#cdata' : '적용하시겠습니까?' }
        , enter : { '#cdata' : '참여하시겠습니까?' }
        , approval : { '#cdata' : '결재하시겠습니까?' }
        , logout : { '#cdata' : '로그아웃하시겠습니까?' }
        , move : { '#cdata' : '이동하시겠습니까?' }
        , checkbox_change : { '#cdata' : '이미 체크된 것을 반전하시겠습니까?' }
      }

      , message : {
        save : { '#cdata' : '저장되었습니다.' }
        , insert : { '#cdata' : '등록되었습니다.' }
        , del : { '#cdata' : '삭제되었습니다.' }
        , update : { '#cdata' : '수정되었습니다.' }
        , execute : { '#cdata' : '진행되었습니다.' }
        , cancel : { '#cdata' : '취소되었습니다.' }
        , apply : { '#cdata' : '적용되었습니다.' }
        , enter : { '#cdata' : '참여되었습니다.' }
        , approval : { '#cdata' : '결재되었습니다.' }
        , logout : { '#cdata' : '로그아웃되었습니다.' }
        , error : { '#cdata' : '오류가 발생했습니다.' }
      }

      , date : {
        /*
        month : ['January','February','March','April','May','June','July','August','September','October','November','December']
        , month_short : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        , day : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
        , day_short : ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
        */
          month : ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
        , month_short : ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12']
        , day : ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일']
        , day_short : ['일', '월', '화', '수', '목', '금', '토']
      }

      , monthpick : {
        close : '닫기'
      }

      , loading : {
        source : { '#cdata' : '' + 
        '<div id="loading" class="loadingArea" style="position: absolute; display:none;">' +
        '<div class="style">' +
        '<div class="contents"> </div>' +
        '</div>' +
        '</div>' },

        source_nuli : { '#cdata' : '<div id="loading" class="ld_line">' +
        '<div class="lft"></div>' + 
        '<div class="cont"><div class="contents"></div></div>' + 
        '<div class="rgt"></div>' + 
        '</div>' },

        wait : { '#cdata' : '잠시만 기다려주세요.' }
      }

    },

    regx : { 
        'user_name' : function(val) {
        var reg = /^[a-zA-Z가-힣]*$/;
        return reg.test(val);
      }
      , 'nickname' : function(val) {
        var reg = /^[a-zA-Z가-힣0-9]*$/;
        return reg.test(val);
      }
      , 'ko' : function(val) {
        var reg = /^[가-힣]*$/; 
        return reg.test(val);
      }
      , 'hyphen_jumin' : function(val) {
        var reg = /[0-9]{6}-[0-9]{7}$/;
        if (reg.test(val) && this.isRegnum(val)) { return true; }
        else { return false; }
      }
    }

  };

  if ($.ja.lang == 'ko') $.extend(true,$.ja.regional,{ regx : $.ja.regx }, $.ja.regional['ko']);

})(jQuery);