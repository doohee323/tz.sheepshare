var g_oConvert = "fw";                                             // 정방향, 역방향 값 
var isArk = true;                                                            // 자동완성 기능 사용 여부
var isKeydown = false;                                             // 브라우저가 파이어폭스, 오페라일 경우 keydown 사용 여부
var cursorPos = -1;                                                       // 자동완성 커서 위치 값
var formName = "#search";                                            // 검색 form의 name을 설정한다.
var queryId = "#query";                                             // 검색어 <input> 의 id을 설정한다
var wrapId = "#ark_sub_wrap";                                  // 자동완성 전체 <div> 의 id을 설정한다
var imgUpId = "#ark_img_up";                                  // 자동완성 up 이미지 id을 설정한다
var footerId = "ark_footer";                                  // 자동완성 Footer <div> 의 id을 설정한다
var imgDownId = "#ark_img_down";                            // 자동완성 down 이미지 id을 설정한다
var arkUpId = "#ark_up";                                            // 자동완성 up 이미지 <div> 의 id을 설정한다
var arkDownId = "#ark_down";                                  // 자동완성 down 이미지 <div> 의 id을 설정한다
var totalFwCount = 0;                                                  // 전방 검색 전체 개수
var totalRwCount = 0;                                                  // 후방 검색 전체 개수
var target = "korbook";                                                 // ARK 웹서버 설정파일의 목록에 있는 추천어 서비스 대상을 지정한다.
var charset = "";                                                            // 인코딩 설정 (인코딩이 utf-8이 아닐 경우 8859_1 로 설정해야함)
var arkPath = "/inc";                                                       // 자동완성 경로
var transURL = "/mok.front/search/initialSearch.ajax";       // trans 페이지의 URL을 설정한다.
var arkWidth = 1;                                                            // 자동완성 전체 넓이 값을 설정한다(변동폭).
var arkTop = 10;                                                            // 자동완성 상단에서의 위치 값을 설정한다.
var arkLeft = 0;                                                            // 자동완성 왼쪽에서의 위치 값을 설정한다.
var arkImgTop = -8;                                                       // 자동완성 화살표 이미지의 상단에서 위치 값을 설정한다.
var arkImgLeft = 334;                                                  // 자동완성 화살표 이미지의 왼쪽에서 위치 값을 설정한다.
var arkFootHeight = 18;                                             // 자동완성 풋터의 높이 값을 설정한다.


$(document).ready(function(){
    // 자동완성 기능 사용 여부 확인 한다.
    if(getCookie("ark")=="off") { 
        isArk = false;
        $(queryId).attr("autocomplete","on"); 
    } else {
        $(queryId).attr("autocomplete","off"); 
    }

    // 브라우저별 key event 체크 한다.
    if($.browser.opera==true || $.browser.mozilla==true) {
        $(document).keydown(function(event) { 
            var query = $(queryId).val();
            if (event.which == 38 || event.which == 40) {
                if (query != "")    {
                    showArk();
                }
                moveFocusEvent(event);
            } else {
                if ($(event.target).is(queryId)) {
                    isKeydown = true;
                    eventKeydown();
                }
            }
        });
    } else {
        $(document).keyup(function(event) {
            var query = $(queryId).val();
            if (event.keyCode == 38 || event.keyCode == 40) {
                if (query != "")    {
                    showArk();
                }
                moveFocusEvent(event);
            } else {
                if ($(event.target).is(queryId)) {
                    if (isArk && query != "") {
                        requestArk($(queryId).val());
                    } else if (query == "") {
                        hideArk();
                    }
                }
            }
        });
    }

    // 브라우저에서 일어나는 클릭 이벤트를 체크한다.
    $(document).click(function(event) {       
      if ($(event.target).is(imgDownId)) {  
            showArk()
            showArkGuide(); 
        } else if ($(event.target).is(imgUpId)) {  
            hideArk();   
        } else if ($(event.target).is(queryId)) {  
            if (isArk) {
                var query = $(queryId).val();
                if (query != "") {
                    requestArk(query);
                    keyword = query;
                } 
                isKeydown = true;
                eventKeydown();
            }
        } else if (!$(event.target).is(wrapId)) {
            hideArk();
        }
    });

    // 자동완성 이미지 설정
    setArkImage();

    // 자동완성 위치 설정
    setArkLocation();
    
    // 자동완성 넓이 및 높이 설정
    setArkSize();

});

// 자동완성에서 사용하는 이미지를 설정한다.
function setArkImage() {
    // 검색창 아이콘 이미지 설정
  $(imgUpId).attr("src", arkPath + "/img/icon_up2.gif");
  $(imgDownId).attr("src", arkPath + "/img/icon_down2.gif");
}

// 자동완성 위치를 설정한다.
function setArkLocation() {
    // 자동완성 전체 위치 설정
    $(wrapId).css({"top" : arkTop + "px"});
    $(wrapId).css({"left" : arkLeft + "px"});

    // 화살표 닫기 이미지 위치 설정
    $(arkUpId).css({"top" : arkImgTop + "px"});
    $(arkUpId).css({"left" : arkImgLeft + "px"});

    // 화살표 열기 이미지 위치 설정
    $(arkDownId).css({"top" : arkImgTop + "px"});
    $(arkDownId).css({"left" : arkImgLeft + "px"});
}

// 자동완성 넓이 및 높이를 설정한다.
function setArkSize() {
    var queryWidth = parseInt($(queryId).width());
    $(wrapId).css({"width" : (queryWidth+arkWidth) + "px"});                                                    // 자동완성 넓이 설정
    $("#" + footerId).css({"height" : arkFootHeight + "px"});                                                    // 자동완성 풋터 높이 설정    
}

// 자동완성 결과를 요청한다.
function requestArk(query) { 
    $.ajax({
        url: transURL,
        data: {convert: g_oConvert, target: target, charset: charset, query: query},
        type: "POST",
        dataType: "json",
        success: function(data) {
            var str = "<div class=\"ark_content\">";
            data = eval("(" + data + ')');
            $.each(data.service, function(i, service){
                if (i == 0) {
                    $.each(service.result, function(j,result){
                        var totalCount = parseInt(result.totalcount);
                        if (j == 0) {
                            totalFwCount = totalCount;
                        } else {
                            totalRwCount = totalCount;
                        }
                        
                        // 정방향, 역방향 구분선
                        if (j > 0 && totalFwCount > 0 && totalRwCount > 0)
                        {
                            str += "<div class=\"line\"></div>"
                        }

                        if (totalCount > 0) {
                            str += "<div class=\"list\"><ul>";

                            // 자동완성 리스트 설정
                            $.each(result.items, function(num,item){
                                str += "<li id=\"bg"+ num +"\" onclick=\"onClickKeyword("+num+");\" onmouseover=\"onMouseOverKeyword("+num+");\"";
                                str += " onmouseout=\"onMouseOutKeyword("+num+");\">"+dispcolor(parseInt(item.type))+"&nbsp;&nbsp;"+item.hkeyword;
                                str += " <span id=\"f"+ num +"\" style=display:none;>"+item.keyword+"</span>";
                                str += " <span style=\"position:absolute; right:0;\">"+disprank(parseInt(item.count))+"&nbsp</span></li>";
                            });

                            str += "</ul></div>";
                        }
                    });
                }
            });

            if ((totalFwCount + totalRwCount) == 0) {
                str += "<div class=\"list\">해당 단어로 시작하는 검색어가 없습니다.</div>";
            }

            str += "</div>";

            str += setArkfooter();

            $(wrapId).html(str);

            showArk();
        }
        , error : function(xhr, status, error) {
          //alert(error);
        }
    });
}


// 브라우저가 파이어폭스, 오페라일 경우 한글 입력
var keyword = "";
function eventKeydown() {
    // 방향키 이동시 메소드 실행을 중지시킨다.
    if(!isKeydown) {
        return; 
    }
    if (keyword != $(queryId).val()) {

        keyword = $(queryId).val();

        if (keyword != "") {          
            requestArk(keyword);
        } else {
            hideArk();
        }
    } 
    setTimeout("eventKeydown()", 100);
}


// 방향키 이벤트를 체크한다.
function moveFocusEvent(event) {
    isKeydown = false;

    if(event.keyCode == 38) {
        if(cursorPos==-1 || cursorPos==0) {
            cursorPos = -1;
            hideArk();
        }else {
            onMouseOutKeyword(cursorPos);
            cursorPos = cursorPos - 1;
            onMouseOverKeyword(cursorPos);
            $(queryId).val($('#f'+cursorPos).text());
        }
    }else if(event.keyCode == 40) {
        if ((totalFwCount + totalRwCount) > (cursorPos + 1))
        {
            onMouseOutKeyword(cursorPos);
            cursorPos = cursorPos + 1;
            onMouseOverKeyword(cursorPos);
            $(queryId).val($('#f'+cursorPos).text());
        } 
    }
}

// 마우스 오버시 선택한 배경을 설정한다.
function onMouseOverKeyword(curSorNum){ 
    clearCursorPos();
  cursorPos = curSorNum;
    $("#bg"+curSorNum).css({"backgroundColor" : "#eeeeee"});
    $("#bg"+curSorNum).css({"cursor" : "pointer"}); 
}

// 마우스 아웃시 선택되었던 배경을 초기화 한다.
function onMouseOutKeyword(curSorNum){
  cursorPos = curSorNum;
    $("#bg"+cursorPos).css({"backgroundColor" : "#ffffff"});
}


// 커서 위치가 움질일때마다 선택되지 않은 부부은 초기화 한다.
function clearCursorPos() {
  for(var i=0; i<(totalFwCount + totalRwCount); i++){
        $("#bg"+i).css({"backgroundColor" : "#ffffff"});
  }
}

// 마우스 클릭시 검색한다.
function onClickKeyword(cursorPos) {
  $(queryId).val($("#f"+cursorPos).text());
  $(formName).submit();
    showArk();
}

// 자동완성 상태 설정한다.
function showArkGuide(){ 
    var str;
    
    str = "<div id=\"ark_guide\">";

    if(isArk) {
        str += "현재 검색어 &nbsp;<b>자동 추천 기능</b>을 사용하고 있습니다.<br>검색어 입력시 자동으로 관련어를 추천합니다."; 
    } else {
        str += "<b>자동 추천 기능</b>을 사용해 보세요. <a href=\"#\" onClick=\"setArkOn()\">기능켜기</a><br>검색어 입력시 자동으로 관련어를 추천합니다.";
    }

    str += "</div>";

    str += setArkfooter();

    $(wrapId).html(str);
}

// 자동완성 푸터를 설정한다.
function setArkfooter(){
    var str = "";

    str += "<div id=\"ark_footer\">";

    if(isArk && $(queryId).val() != "") {
        if(g_oConvert == "fw"){
            str += "&nbsp;<img src=\""+arkPath+"/img/ark_end_icon.gif\" width=\"16\" height='14' style=\"vertical-align:-2px\"> <a href=\"#\" onclick=\"onConvert('rw');return false;\">끝단어 더보기</a>"; 
        }else if(g_oConvert == "rw"){
            str += "&nbsp;<img src=\""+arkPath+"/img/ark_start_icon.gif\" width=\"16\" height='14' style=\"vertical-align:-2px\"> <a href=\"#\" onclick=\"onConvert('fw');return false;\">첫단어 더보기</a>"; 
        } 
    }

    str += "&nbsp;<span style=\"float:right;\"><a href=\"#\" onClick=\"openHelp();\">도움말</a>&nbsp;<img src=\""+arkPath+"/img/ark_bar.gif\">&nbsp;";

    if(isArk) {
        if ($(queryId).val() != "") {      
            str += "<a href=\"javascript:setArkOff();\">기능끄기</a>&nbsp;";
        } else {
            str += "<a href=\"#\" onClick=\"showArk();\">닫기</a>&nbsp;"; 
        }        
    } else {
        str += "<a href=\"#\" onClick=\"showArk();\">닫기</a>&nbsp;"; 
  }
    str += "</span></div>";

    return str;

}

// 자동완성을 보여준다.
function showArk() {
    $(wrapId).show();
    $(arkUpId).show();
    $(arkDownId).hide(); 
}

// 자동완성을 감춘다.
function hideArk() {
    $(wrapId).hide();
    $(arkUpId).hide();
    $(arkDownId).show(); 
}

// 도움말 팝업 
function openHelp(){
    window.open(arkPath+"/help/help_01.html","도움말","height=540,width=768,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,directories=no,status=no");
}

// 단어 입력 후 정방향 역방향 이미지 버튼 클릭시 이벤트 처리 한다.
function onConvert(convert){
    var query = $(queryId).val();

    if(convert=="fw"){
        g_oConvert = "fw";
    } else {
        g_oConvert = "rw";
    }

    requestArk(query);
    return;
}

// 자동완성 기능 끄기
function setArkOff() {
    $(queryId).attr("autocomplete","on");
  isArk = false;

  var today = new Date();
  var expire_date = new Date(today.getTime() + 365*60*60*24*1000);
  setCookie("ark","off",expire_date);
}

// 자동완성 기능 켜기
function setArkOn() {
    $(queryId).attr("autocomplete","on");
  isArk = true;

  var today = new Date();
  var expire_date = new Date(today.getTime() - 60*60*24*1000);
  setCookie("ark","on",expire_date);

    var query = $(queryId).val();
    if (query != "") {
        requestArk(query);
    }
}

// 쿠키 값을 설정 한다.
function setCookie(name, value, expire) {
  var expire_date = new Date(expire)
  document.cookie = name + "=" + escape(value) + "; path=/; expires=" + expire_date.toGMTString();
}

// 쿠키 값을 가져온다.
function getCookie(name) {
  var dc = document.cookie;
  var prefix = name + "=";
  var begin = dc.indexOf("; " + prefix);
  if (begin == -1) {
    begin = dc.indexOf(prefix);
    if (begin != 0) 
      return null;
  } else {
    begin += 2;
  }

  var end = document.cookie.indexOf(";", begin);
  if (end == -1)
    end = dc.length;
  return unescape(dc.substring(begin + prefix.length, end));
}

// font color 
function dispcolor(count){ 
  var color;
    var ret;
     if(count >= 0 && count <= 4){
        color = "#989898";
     }else{
        color = "#CC6633";
     }
    if(count == 0 || count == 5){
        ret = "<font style='font-size:11px;font-family:돋움;color:"+color+"'>사전</font>";
    }else if(count == 1 || count == 6){
        ret = "<font style='font-size:11px;font-family:돋움;color:"+color+"'>일반</font>";//색인
    }else if(count == 2 || count == 7){
        ret = "<font style='font-size:11px;font-family:돋움;color:"+color+"'>인기</font>";
    }else if(count == 3 || count == 8){
        ret = "<font style='font-size:11px;font-family:돋움;color:"+color+"'>테마</font>";
    }else if(count == 4 || count == 9){
        ret = "<font style='font-size:11px;font-family:돋움;color:"+color+"'>추천</font>";
    }
    
 return ret; 
}

// 추천어 리스트 우측에 Ranking Bar 출력 한다.
function disprank(count){ 
    var str;
    if(count >= 0 && count <= 20){
        str = "<font style=\"font-size:9px;color:#CC6633\">|</font><font style=\"font-size:9px;color:#C0C0C0\">||||</font>";
    }else if(count > 20 && count <= 40){
        str = "<font style=\"font-size:9px;color:#CC6633\">||</font><font style=\"font-size:9px;color:#C0C0C0\">|||</font>";
    }else if(count > 40 && count <= 60){
        str = "<font style=\"font-size:9px;color:#CC6633\">|||</font><font style=\"font-size:9px;color:#C0C0C0\">||</font>";
    }else if(count > 60 && count <= 80){
        str = "<font style=\"font-size:9px;color:#CC6633\">||||</font><font style=\"font-size:9px;color:#C0C0C0\">|</font>";
    }else if(count > 80 && count <= 100){
        str = "<font style=\"font-size:9px;color:#CC6633\">|||||</font>";
    }else{
        str = "<font style=\"font-size:9px;color:#CC6633\">|||||</font>";
    }
 return str; 
}