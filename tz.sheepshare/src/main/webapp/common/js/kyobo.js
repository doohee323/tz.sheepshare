// 모바일 상단 주소창 사라지기
window.addEventListener('load', function(){
  setTimeout(scrollTo, 0, 0, 1);
}, false);

// 공통 스크립트 실행
$(function(){	
	
	// 대학교제 검색결과 노출
	$('#uSearchStart').click(function(){
		$('#uSearchResult').css('display','block');
	});	
	
	// search
	$(".search_view li").each(function(index){
		
		$(this).click(function(e){			
			e.preventDefault();
			for(var i = 0 ; i < $(".search_view li").length ; i++){
				var tempObj = $(".search_view li").eq(i);
				tempObj.addClass("ddt").css("backgroundColor","");
				tempObj.find("a").css("backgroundImage", tempObj.find("a").css("backgroundImage").replace("_on.png",".png"));
			}
			$(this).css("backgroundColor","#095fb6");
			$(this).find("a").css("backgroundImage", $(this).find("a").css("backgroundImage").replace(".png","_on.png"));
			
			var disChk = $(".searchBox").eq(index).css("display");
			$(".searchBox").css("display","none");
			if (disChk == "block"){
				$(".searchBox").eq(index).css("display","none");
				$(this).find("a").css("backgroundImage", $(this).find("a").css("backgroundImage").replace("_on.png",".png"));
				$(this).css("backgroundColor","");
			}
			else{$(".searchBox").eq(index).css("display","block");}	
		})
	})
	
	// share btn
	$('.btn_sub .btn_share').click(function() {
		$('#share_btn').toggle();
	});
	
	// $("#cart").css("display","none");
	
	// member review
	$('.list_book_info').live('click',function() {
		var $sel_div = $(this).children('div.memeber_review_detail');
		if ($sel_div.hasClass('disb')){
		$('div.memeber_review_detail').removeClass('disb');
			// $sel_div.html('');

		} else {
		$('div.memeber_review_detail').removeClass('disb');
			// $sel_div.html('테스트입니다.');
			$sel_div.addClass('disb');
		}
	});

	// $('.pop_btn_close,pop_btn_close_m').click(function(){
	// $('#cart').hide();
	// })
	//mainTab();	
	
	// book_list
	$(".list_type02 .list").bind("click",function(){
		// this = list_type 의 li a
		if ($(this).parent().attr("class") == "ct_view")
		{
			// 로딩 전: 기존 이미지를 감추고 로딩스피너를 삽입..
			// $(this).find('img').css('opacity', '0');
			// $(this).find('.loadingIndicator').html('<img
                        // src="http://image.kyobobook.com/mimages/static/images/common/ico_spinner.gif"/>');
			// //로딩 전
		    $(".list_type02 .ct_view_on").attr("class","ct_view").find(".book_info").css("display","none");
			if ($(this).parent().attr("class","ct_view_on").find("ul").hasClass("book_info") == true)
			{
				$(this).parent().attr("class","ct_view_on").find("ul.book_info").css("display","block");
				$(".list_type").css("display","block");
			}
			else{
				$(this).parent().attr("class","ct_view_on").find("ol.book_info").css("display","block");
				$(".list_type").css("display","block");
				$(".book_info li.addMinus").removeClass("addMinus").addClass("sub");
			}
			
			// 로딩 후: 기존 이미지를 보여주고 로딩스피너를 지움.
			// 테스트용
			// setTimeout(function(obj){
			// $(obj).find('.loadingIndicator').empty();
			// $(obj).find('img').css('opacity', '1');
			// }, 1000, this);
			
			// 실제
			// $(this).find('.loadingIndicator').empty();
			// $(this).find('img').css('opacity', '1');
			// //로딩 후
		}
		else{
			if ($(this).parent().attr("class","ct_view_on").find("ul").hasClass("book_info") == true)
			{
				$(this).parent().attr("class","ct_view").find("ul.book_info").css("display","none");
				$(".list_type").css("display","none");
			}
			else{
				$(this).parent().attr("class","ct_view").find("ol.book_info").css("display","none");
				$(".list_type").css("display","none");
			}
		}
	});

	$(".book_info > li > a.text_st2").click(function(){
		if ($(this).parent().attr("class") == "sub"){
			// 로딩 전: 기존 이미지를 감추고 로딩스피너를 삽입.
			// $(this).find('.loadingIndicatorSub').html('<img
                        // src="http://image.kyobobook.com/mimages/static/images/common/ico_spinner.gif"/>');
			// //로딩 전
			$(".book_info .addMinus").attr("class","sub").find(".book_info").css("display","none");
			$(this).parent().attr("class","addMinus").find("ol.book_info").css({"display":"block","backgroundColor":"#fff"}).find(".list_more").css("border-bottom","none");
			$(this).find("a").css("border","1px solid red");
			$(".list_type").css("display","block");
			
			// 로딩 후: 기존 이미지를 보여주고 로딩스피너를 지움.
			// 실제
			// $(this).find('.loadingIndicatorSub').empty();
			
			// 테스트용
			// setTimeout(function(obj){
			// $(obj).find('.loadingIndicatorSub').empty();
			// }, 1000, this);
			// //로딩 후
		}
		else{
			$(this).parent().attr("class","sub").find("ol.book_info").css({"display":"none","background":"#fff"});
			$(".list_type").css("display","block");
			$(this).find("a").css("border","1px solid red");
		}
	});

	/* radio , input, select */
	/*	*/
	var radioDiv = $(".inputRadio");
	var checkDiv = $(".inputCheckbox");
	select = $(".inputSelect .styled");
	
    $('label').bind('click', function(event) {
        event.stopPropagation();
    });

    select.each(function() {
        select.change(function() {
            $(this).parent().find("span").text($(this).find(':selected').text());
            $(this).parent().find("span").attr("value", $(this).val());
        });
    });

    radioDiv.each(function() {
        if ($(this).find("input[type=radio]").attr('checked')) {
            $(this).find("span.radio").css("backgroundPosition", "0 -20px");
        }
    });

    radioDiv.toggle(function() {
        if ($(this).find("span").hasClass("radio")) {
            if (!$(this).find("input[type=radio]").attr('disabled')) {
                $(this).parent().find("input[type=radio]").removeAttr("checked");
                $(this).parent().find("span.radio:not(.check_off)").css("backgroundPosition", "0 0");
                $(this).find("span.radio").css("backgroundPosition", "0 -20px");
                $(this).find("input[type=radio]").attr("checked", "checked");
            }
        } else {
            return;
        }
    }, function() {
        if ($(this).parent().find("span").hasClass("radio")) {
            if (!$(this).find("input[type=radio]").attr('disabled')) {
                $(this).parent().find("input[type=radio]").removeAttr("checked");
                $(this).parent().find("span.radio:not(.check_off)").css("backgroundPosition", "0 0");
                $(this).find("span.radio").css("backgroundPosition", "0 -20px");
                $(this).find("input[type=radio]").attr("checked", "checked");
            }
        } else {
            return;
        }
    });

    checkDiv.change(function(e) {
        changeCheckBoxLabel(this);
    });

    checkDiv.each(function() {
        changeCheckBoxLabel(this);
    });
	
	// 마이룸 장바구니 펼침/닫힘
	$(".view_Info").bind('vclick', function(){
		if ($(this).children().attr("src").indexOf("plus") > 0 ){
			$(this).children().attr("src", $(this).children().attr("src").replace("plus.png", "minus.png"));
			$(this).closest('.box105').find('.tableHidden').css("display", "table");
		}
		else{
			$(this).children().attr("src", $(this).children().attr("src").replace("minus.png", "plus.png"));
			$(this).closest('.box105').find('.tableHidden').css("display", "none");
		}
	});

	
	// q&A tab / default
	var clickArea = $("#helpTab li a,.card_list li a");
	var tabcontent = $(".tabcontent");
	
	clickArea.each(function(index){
		$(this).click(function(){
			clickArea.parent().removeClass("on");
			$(tabcontent).css("display","none");
			$(this).parent().addClass("on");
			$(tabcontent[index]).css("display","block");
		})
	});
	$(clickArea[0]).click();
	
	var chkArea = $(".tab_style01 li,.tab_style02 li");
	var tabcont = $(".tabcontent");
	
	if (chkArea.parent().hasClass("tab_none") == true){
		return;
	}
	else{
		chkArea.each(function(index){
			$(this).bind('vclick',function(){
				chkArea.removeClass("on");
				$(tabcont).css("display","none");
				$(this).addClass("on");
				$(tabcont[index]).css("display","block");
			})
		});
		$(chkArea[0]).click();	
	}
	
	
	var clickArea02 = $(".st_list02 li a");
	var tabcontent02 = $(".tabcontent02");
	
	clickArea02.each(function(index){
		$(this).click(function(){
			clickArea02.parent().removeClass("on");
			$(tabcontent02).css("display","none");
			$(this).parent().addClass("on");
			$(tabcontent02[index]).css("display","block");
		});
	});
	$(clickArea02[0]).click();
	
	for(var i = 0 ; i < $(".inputSel").length ; i++){
		$($(".inputSel")[i]).find("input[type=text]").attr("value",$($(".inputSel")[i]).find(".deselect option:first").text());
		$(".ui-btn-text").css("text-align","center");
		
	}
	
	$(".deselect").change(function(){
		$(".deselect option:selected").each(function(index){
			//$(this).parent().parent().parent().find("input[type=text]").attr("value",$(this).text());
			//console.log($(this).parents().parent().parent().parent().parent().parent());
			//$(".inputSel").eq(index).find("input[type=text]").attr("value",$(this).text());
			$(".ui-btn-text").css("text-align","center");
		})
	})
	
	//매장안내 tab / head_jqm import시
	/*var clickArea2 = $(".st_list li");
	var tabcontent2 = $(".tabcontent");
	//clickArea2.css("border","15px solid red");
	
	clickArea2.each(function(index){
		clickArea2.click(function(){
			clickArea2.removeClass("on");
			$(tabcontent2).css("display","none");
			$(this).addClass("on");
			$(tabcontent2[index]).css("display","block");
		});
	});
	$(clickArea2[0]).click();
	*/
	
	
	//q&a list
	$(".qna_list > li > a").click(function(){
		var displayChk = $(this).parent().find("ul").css("display");
		$(".qna_list > li > ul").css("display","none");
		if (displayChk == "none"){
			//console.log($(this).parent().find("ul"));
			$(this).parent().find("ul").css("display","block");
		}
		else{
			$(this).parent().find("ul").css("display","none");
		}
	})
});

function paRarent(){
	$(".searchBox").css("display","none");
}

/**
 * radio 객체의 상태를 변경
 * 
 * @param el
 *                radio 객체
 * @param inCheck
 *                true/false(선택/미선택)
 */
function changeRadioStatus(el, inCheck) {
    if (!$(el).attr("disabled")) {
        if (inCheck) {
            $(el).parent().find("span.radio").css("backgroundPosition", "0 -20px");
            $(el).parent().find("input[type=radio]").attr("checked", "checked");
        } else {
            $(el).parent().find("span.radio").css("backgroundPosition", "0 0");
            $(el).parent().find("input[type=radio]").removeAttr("checked");
        }
    }
}

/**
 * checkBox 객체의 상태를 변경
 * 
 * @param el
 *                checkbox객체
 * @param inCheck
 *                true/false(선택/미선택)
 */
function changeCheckBoxStatus(el, inCheck) {
    if (inCheck)
        $(el).attr('checked', 'checked');
    else
        $(el).removeAttr('checked');

    changeCheckBoxLabel($(el).parent());
}

/**
 * Label 객체안의 checkbox 상태값에 따라 checkbox와 span의 css를 변경
 * 
 * @param parentEl
 *                Label객체
 */
function changeCheckBoxLabel(parentEl) {
    if (!$(parentEl).find("input[type=checkbox]").attr("disabled")) {
        if ($(parentEl).find("input[type=checkbox]").attr("checked") == 'checked') {
            $(parentEl).find("span.checkbox").css("backgroundPosition", "0 -60px");
            $(parentEl).find("input[type=checkbox]").attr("checked", "checked");
        } else {
            $(parentEl).find("input[type=checkbox]").removeAttr("checked");
            $(parentEl).find("span.checkbox").css("backgroundPosition", "0 -40px");
        }
    }
}

// LayerSlideDown
function LayerSlideDown(obj){	
	$('.popSlidedownClose').click(function(){
		$('#'+obj).addClass('disnn');					
		// $(this).parent().css('display','none')
	})
	$('#'+obj).removeClass('disnn');
}

//LayerSlideDown MyAddressList
$(function(){
	$('#AddressUse').change(function() {							
	        if ($(this).val() == "contacts"){
			$('#myaddressList').removeClass('disnn')
		}							
	});
	$('.popSlidedownClose').click(function(){
		$('#myaddressList').addClass('disnn')
	});	
});

//LayerSlideDown DirectDream
$(document).ready(function(){	
    var rCp = $('#pickupSite');	    
    rCp.live('change',function(){	
	$('#pickupInfo').removeClass('disnn');
	$('.box105 th').removeClass('bbrl5');
	var siteCode = $(this).find('option:selected').val();
	
	if($(this).val() == '선택하세요'){
    	    $('#pickupInfo').addClass('disnn');
    	    $('.box105 th').addClass('bbrl5');
	}else {
	    pickupSite.siteCode = siteCode;
	    pickupSite.siteName = $(this).find('option:selected').attr('siteName');
	    findPickupSiteStatus(pickupSite);
	}
		
    });
	
});

var findPickupSiteStatus = function(pickup) {
    $.post(
        '/purchase/pickup/findPickupSiteStatus',
        {
            'barcode'           : pickup.barcode, 
            'productType'       : pickup.productType, 
            'quantity'          : pickup.quantity,
            'siteCode'          : pickup.siteCode
        },
        function(json) {
            showPickupSiteStatus(json); 
        },
        'json'
    );
};

var showPickupSiteStatus = function (pickupStatus) {
    $('#pickupTimeTerm').text('주문 완료 후 ' + pickupStatus.timeTerm + ' 시간 이후');
    $('#pickupDate').text(pickupStatus.expectDate);
    pickupSite.pickupTimeTerm = pickupStatus.timeTerm;
    return;
};

// Go Page Top
function pageTop(){
scroll(0,0);
}

// Header Search Box Input Operate
function inputClick01()
{
	$('.bi_area').stop().animate({"width":"0"},400,function(){$(this).hide();});
	$('.search_box').stop().animate({"paddingLeft":"10px"},600);
}
function inputClick02()
{
	$('.bi_area').stop().animate({"width":"107px"},200,function(){$(this).show();});
	$('.search_box').stop().animate({"paddingLeft":"117px"},500);
}
	
// jQuery Mobile Page Cache Remove
jQuery('div').live('pagehide', function(event, ui){
	var page = jQuery(event.target);
	
	if(page.attr('cache') == 'false'){
		page.remove();
	};
});

// Link Area Fixed - slidePopup
// 슬라이딩 되는 페이지들
// 링크 영역을 전체로 바꿈
$(document).ready(function(){
	
	var lbi = $('.slideOperateClass');
	var lbiLength = $('.slideOperateClass').length;
	for(var i = 0 ; i < lbiLength ; i++){
		$(lbi[i]).bind('vclick', function(e){
			if(e.target.tagName.toLowerCase() == 'a' || $(e.target).hasClass('popSlidedownClose' || $(e.target).closest('a').length)){
				return;
			}
			
			$.mobile.changePage($(this).find('.btn_view_more').attr('href'));
		});
	}
	
});

// Store Info View Tab
$(document).ready(function(){
		
	$('#storeTab li').live('vclick', function(){
		
		$('#storeTab li').removeClass('on');
		$(this).addClass('on');
		
		$('#storeTab01, #storeTab02, #storeTab03').addClass('disnn');
		
		var index = $(this).index();
		if(index == 0){
			$('#storeTab01').removeClass('disnn');
		} else if(index == 1){
			$('#storeTab02').removeClass('disnn');
		} else{
			$('#storeTab03').removeClass('disnn');
		}
								
	});		
	
});

/* cart check / scroll */
/*
 * function cart(){ $(document).bind('scroll', function() { cartAction(); });
 * 
 * $(document).bind('touchend', function() { cartAction(); });
 * 
 * $('#container').bind('touchstart', function() { $("#cart").css('display',
 * 'none'); }); $('#footer').bind('touchstart', function() {
 * $("#cart").css('display', 'none'); }); }
 * 
 * function cartAction(){
 * 
 * var $button = $("#clickme"); var $cart = $("#cart"); $cart.css('display',
 * 'none'); $cart.css('top', window.innerHeight + window.scrollY + "px");
 * 
 * $cart.css('display', 'block');
 * 
 * $button.toggle( function() { $('#cart .list').css("display","block");
 * $cart.stop().animate({top: window.innerHeight + window.scrollY -165 +
 * "px"},200); }, function() { $cart.stop().animate({top: window.innerHeight +
 * window.scrollY + "px"},200, function() { $('#cart
 * .list').css("display","none"); }); } ); }
 * 
 * function fnShow(val) { if (val == "cartIn") { clickView(); cart(); } }
 * function clickView(){ $("#cart").css({"top":window.innerHeight +
 * window.scrollY - 165 + "px","display":"block"}); $("#cart .btn_Area
 * .close").click(function(){ $("#cart").css({"top":window.innerHeight +
 * window.scrollY + "px","display":"block"}); })
 * 
 * var $button = $("#clickme"); var $cart = $("#cart");
 * 
 * $button.toggle( function() { $('#cart .list').css("display","block");
 * $cart.stop().animate({top: window.innerHeight + window.scrollY -165 +
 * "px"},200); }, function() { $cart.stop().animate({top: window.innerHeight +
 * window.scrollY + "px"},200, function() { $('#cart
 * .list').css("display","none"); }); } ); }
 */

// main visual tab
/*
function mainTab(){
var clickarea = $(".tab_Type li");
var tabcont = $(".tabcont");

clickarea.each(function(index){
	$(this).click(function(){
		clickarea.parent().removeClass("on");
		$(tabcont).css("display","none");
		$(this).parent().addClass("on");
		ContBlock = $(tabcont[index]).css("display","block");
	});
});
	$(clickarea[2]).click();
}
*/


// //////////////////////////////////////////////
// /// start: 장바구니 관련
// //////////////////////////////////////////////
var cartJsonData, checkDupl, showState;
var cartJsonDataGetUrl = '/myroom/cart/prevList';

$(function(){
	$("#cart").css("display", "none");
	
	/*
         * 로그인 되었을 경우 showCartCnt(${countCartList}); // ex) showCartCnt(3); 을 매
         * 페이지에 호출 함으로써 장바구니를 보여준다.
         * 
         * "장바구니 담기"를 클릭시 addCartItem('bookId', 'bookPageLink', 'bookImage');
         * ex) addCartItem('id', 'link', 'http://image.kyobobook.com/mimages/static/images/cartTestImg1.jpg'); 로
         * 장바구니에 제품을 담는다.
         * 
         * "장바구니"를 클릭시 getCartItem(); 를 호출하여 json데이터를 가져와서 장바구니를 뿌려준다. var
         * memberCartList = '/member/@memberId/cartList'; // 수정 필요
         */
	
	$('#cartArrow').live('vclick', function(){
		alert('장바구니 바로가기');
		// location.href = '장바구니 url';
	});
	
	$('#cartClose').live('vclick', function(){
	    controlCartDiv('close');
	});
	
	$('#btnCart').live('vclick', function(){
	    if('show' === showState) {
	    	controlCartDiv('open');
	        getCartItem();
	    } else  if ('open' === showState) {
	        controlCartDiv('close');
	    }
	});
	
	$(window).bind('touchmove', function(e) {
		$("#cart").css('display', 'none');
	    $("#cart").css('display');
	});
	
	$(document).bind('scroll', function() {
	    controlCartDiv(showState);
	});
});

// 갯수만 보이는 장바구니
function showCartCnt(cnt){
	setCartCnt(cnt);
	controlCartDiv('show');
}

function setCartCnt(cnt){
	$('#txtCartCnt').html(cnt);
}

function getCartCnt(){
	return parseInt($('#txtCartCnt').html());
}

// 장바구니 리스트 출력
function showCartItem(cartData){
    showCartCnt(cartData.countCartList);
    cartPrevList(cartData);
    controlCartDiv('open');
}

//장바구니에 담긴 최신상품 4개 목록
function cartPrevList(cartData) {
    var cartList = $('#cart .list ul');
    cartList.find('li').remove();
 
    if(cartData){
        for(var i = 0 ; i < cartData.prevCartList.body.length ; i++){
            if(i >= 4){
                break;
            }
            cartList.append('<li><a rel="external" href="'+cartData.prevCartList.body[i].link+'"><img src="'+cartData.prevCartList.body[i].image+'" alt="" style="width:55px; height:75p;"/></a></li>');
        }
    }    
}

// 미사용 - 장바구니 담기(해당 제품 링크, 카트에 들어갈 이미지)
function addCartItem(barcode, cartItemLink, cartItemImage){
	if(searchDuplCartItem(barcode)){
		alert('이미 담긴 상품입니다.');
		return;
	}
	
	if(!(showState == 'open')){
		controlCartDiv('open');
	}
	
	setCartItem(barcode, cartItemLink, cartItemImage);
}


// 장바구니에 이미 담긴 물건인지 체크
function searchDuplCartItem(barcode){
	if(cartJsonData){
		for(var i = 0 ; i < cartJsonData.listItems.length ; i++){
			if(cartJsonData.listItems[i].barcode == barcode){
				return true;
			}
		}
	}
	
	if(checkDupl == barcode){
		return true;
	}
	return false;
}

// 장바구니 제어
function controlCartDiv(sw){
    if(sw == 'show'){
		$("#cart").css({display:'block', top: window.innerHeight + window.scrollY});
		showState = 'show';
    }else if(sw == 'hide'){
		$("#cart").css({display:'none'});
		showState = 'close';
    }else if(sw == 'open'){       
        if(showState != sw){
            $("#cart").css({display:'block', top:window.innerHeight + window.scrollY});
            $("#cart").stop().animate({top: window.innerHeight + window.scrollY - 165}, 200);
            $("#cart .list").css({display:'block'});
		}else{
		    $("#cart").css({display:'block', top: window.innerHeight + window.scrollY - 165});
		    $("#cart .list").css({display:'block'});
		}
        showState = 'open';
    }else if(sw == 'close'){
        $("#cart").stop().animate({top: window.innerHeight + window.scrollY}, 200);
        showState = 'show';
    }
}

// 장바구니 데이터 가져오기
function getCartItem(){
    $.ajax({
	url: this.cartJsonDataGetUrl,
	dataType: 'json',
	beforeSend: function(jqXHR, settings){
		
	},
	complete: function(jqXHR, textStatus){
		
	},
	success: function(data, textStatus, jqXHR){	    
	    showCartItem(data);
	},
	error: function(jqXHR, textStatus, errorThrown){
		
	}
    });
}

// 장바구니 데이터 넣기
// 안씀
function setCartItem(setCartItem, cartItemLink, cartItemImage){
	// db로 전송
	
	// ajax로 db에 밀어 넣는다.
	// start: 성공시
	var cartList = $('#cart .list ul');
	
	if(cartList.find('li').length >= 4){
		cartList.find('li').eq(0).remove();
	}
	var cartCnt = getCartCnt();
	setCartCnt(++cartCnt);
	cartList.append('<li><a href="'+cartItemLink+'"><img src="'+cartItemImage+'" alt="" style="width:55px; height:75p;"/></a></li>');
	checkDupl = setCartItem;
	// end: 성공시
	/*
	$.ajax({
		url: '',
		beforeSend: function(jqXHR, settings){
			
		},
		success: function(data, textStatus, jqXHR){
			
		},
		complete: function(jqXHR, textStatus){
			// 성공시 코드
		},
		error: function(jqXHR, textStatus, errorThrown){
			
		}
	});
	*/
}
// //////////////////////////////////////////////
// /// end: 장바구니 관련
// //////////////////////////////////////////////

/* test 소스
$(document).bind('mobileinit',function(){
	($.mobile.activePage).remove();
	//alert($.mobile.useFastClick);
});

$('div').live('pageshow',function(event, ui){
	//alert('this page was just hidden: ' + ui.prevPage);
	//$('div').css('border','2px solid blue');
	//($.mobile.activePage).remove();
})

$('div').live('pagehide',function(event, ui){
	//$('div').css('border','2px solid red');
	console.log($.mobile.activePage);
	//($.mobile.activePage).remove();
	//alert('this page was just shown: ' + ui.nextPage);
})
*/

////////////////////////////////////////////////
///// start: 모바일 디바이스 및 위치정보 관련
////////////////////////////////////////////////
//device 정보 가져오기
function getDeviceInfo(){
    var rslt = {};
    var browserInfo = navigator.userAgent + '^' + navigator.platform;
    //alert(browserInfo);
    if(browserInfo.indexOf('Android') > -1) {
        var models = 'SHW-M180S,SHW-M180L,SCH-I800,SGH-T849'.split(',');
        var deviceArry = browserInfo.split(';');
        var locale = deviceArry[3];
        var tmpArry = deviceArry[4].split('/');
        var deviceModel = tmpArry[0];
        rslt.osVersion = tmpArry[1].substring(0, tmpArry[1].indexOf(')'));
        rslt.deviceModelName = deviceModel;
        for(var i=0;i<models.length;i++) {
            if(deviceModel.indexOf(models[i]) > -1) {
                rslt.osType = 'androidTablet';
                break;
            }
        }
        if(!rslt.osType) rslt.osType = 'androidPhone'; 
    } else if(browserInfo.indexOf('Mac OS') > -1) {
        var models = 'iPhone,iPod'.split(',');
        var deviceArry = browserInfo.split(';');
        var deviceType = deviceArry[0].substring(deviceArry[0].indexOf('(') + 1, deviceArry[0].length);
        rslt.osVersion = deviceArry[2].substring(deviceArry[2].indexOf('OS') + 3, deviceArry[2].indexOf('like') - 1);
        var locale = deviceArry[3].substring(1, deviceArry[3].indexOf(')'));
        rslt.deviceModelName = browserInfo;     
        for(var i=0;i<models.length;i++) {
            if(deviceType.indexOf(models[i]) > -1) {
                rslt.osType = 'applePhone';
                break;
            }
        }
        if(!rslt.osType) rslt.osType = 'appleTablet'; 
    } else {
        // 순서!
        if (/msie/.test(navigator.userAgent.toLowerCase())) rslt.deviceModelName = 'msie';    
        if (/mozilla/.test(navigator.userAgent.toLowerCase())) rslt.deviceModelName = 'mozilla';    
        if (/safari/.test(navigator.userAgent.toLowerCase())) rslt.deviceModelName = 'safari';    
        if (/opera/.test(navigator.userAgent.toLowerCase())) rslt.deviceModelName = 'opera';    
        if (/chrome/.test(navigator.userAgent.toLowerCase())) rslt.deviceModelName = 'crome';
        rslt.osVersion = $.browser.version; 
        rslt.osType = 'WEB';
    }
    return rslt;
}

//GeotLocation 정보 가져오기
function getGeoLocationInfo(callback){
    if(navigator.geolocation) {
        var rslt = {};
        navigator.geolocation.getCurrentPosition(
            function successCallback(position) {
                rslt.code = "1";
                rslt.message = "";
                rslt.latitude = position.coords.latitude;   // 위도
                rslt.longitude = position.coords.longitude; // 경도
                rslt.coordinates = position.coords.latitude + "," + position.coords.longitude; // '위도,경도'
                callback(rslt);
            },
            function errorCallback(error) {
                var message = "";
                switch (error.code) {
                    case error.PERMISSION_DENIED:
                        message = "위치정보를 사용할 수 있는 권한이 없습니다.";
                        break;
                    case error.POSITION_UNAVAILABLE:
                        message = "현재 위치정보를 확인할 수 없습니다.";
                        break;
                    case error.PERMISSION_DENIED_TIMEOUT:
                        message = "위치정보 검색 시간을 초과하였습니다.";            
                        break;
                    default:
                        message = "위치정보를 가져오지 못했습니다.";
                        //message = "Error Code : " + error.code.toString();
                        break;
                }
                rslt.code = "0";
                rslt.errorCode = error.code;
                rslt.message = message;
                callback(rslt);
            },  {timeout:10000}             // Timeout : 테스트 10초
        );
    } else {
        callback(null); // no working to do
    }
}
////////////////////////////////////////////////
///// end: 모바일 디바이스 및 위치정보 관련
////////////////////////////////////////////////

////////////////////////////////////////////////
///// start: 검색영역 관련
////////////////////////////////////////////////
//검색하기
 var goSearch = function (type, rootURL) {
type = type || 'TOP'; // TOP : 상단, BOTTOM : 하단 검색바
rootURL = rootURL || '';
var searchInput = (type == 'TOP') ? $('#search_input') : $('#search_input_bottom');
var searchValue = searchInput.val();
if (!checkBytes(searchValue)) {
searchInput.focus();
return;
}
location.href = rootURL + "/search/main/" + searchValue;
};


//검색어 바이트 체크
var checkBytes = function (v) {
v = v.replace(/^\s*|\s*$/g, '');
if(v == ""){
    alert("검색어를 입력하세요.");
    return ;
}

var thisStrLen = getMsgSize(v);
if(v != ""){
    if( 2 > thisStrLen){
        if( v >= "0" && v <= "9"){
            alert("숫자는 2글자 이상 입력하세요!");
        }else if( v >= "A" && v <= "z"){
            alert("영문은 2글자 이상 입력하세요!");
        }else{
            alert("특수문자는 2글자 이상 입력하세요!");
        }
        return;
    }
 }
    
 return true;
};

//검색어 입력 길이 가져오기 
function getMsgSize(thisStrvalue){  
var strLen = 0;

for(i = 0; i < thisStrvalue.length;i++){
    if(escape(thisStrvalue.charAt(i)).length >= 4){
        strLen +=2;
    }
    else{
        if(escape(thisStrvalue.charAt(i)) !="%0D")
            strLen++;
    }
}
    return strLen;
}
////////////////////////////////////////////////
///// end: 검색영역 관련
////////////////////////////////////////////////

/**
 * 로그인이 필요한 경우 로그인 페이지도 이동 시키고 로그인 후 원래 화면으로 돌아오도록 제어
 * $loginFlag 로그인 여부(<@security.authorize ifNotGranted="ROLE_USER"> 로 확인) 
 * $loginUrl 로그인 페이지
 * $turl 원래 url param key
 * @returns false 로그인 페이지도 이동
 */ 
function cfLoginYn() {
    if($loginYn) return true;
    //로그인이 안되어 있을때는 로그인창으로 이동함..
    var currentUrl = document.location.href;
    //로그인이후 상세화면으로 이동처리
    document.location.href = $loginUrl + '?' + $turl + '=' + currentUrl;
    return false;
}

