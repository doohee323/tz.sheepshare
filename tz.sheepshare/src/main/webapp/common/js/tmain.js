var tmBooks1, tmBooks2, tmBooks3;
var tmJson1 = '/main/today';
var tmJson2 = '/main/hotissue';
var tmJson3 = '/main/prealbum';
//var tmJson1 = '/_test/tmain_content1.json';
//var tmJson2 = '/_test/tmain_content2.json';
//var tmJson3 = '/_test/tmain_content3.json';

var loading_spinner = '<div id="tm_spinner"><img src="http://image.kyobobook.com/mimages/static/images/common/ico_spinner_gray.gif"/></div>';

function getTmainContent(index, type){
	var context = $(getType(type));
	var tmBooks = getTypeJSON(type);
	
	if(type == 'album'){
		return;
	}
	
	$('.tm_view_wrap', context).empty();
	$("#tm_view_tmpl").tmpl(tmBooks.books[index]).appendTo($('.tm_view_wrap', context));
}

function createTmainList(type){
	var context = $(getType(type));
	var tmBooks = getTypeJSON(type);
	
	if(type == 'album'){
		for(var i = 0 ; i < tmBooks.albums.length ; i++){
			$("#tm02_list_tmpl").tmpl(tmBooks.albums[i]).appendTo($('.tm02_list', context));
		}
		
		
		$('.tm02_list li', context).bind('vclick', function(e){
			var index = $(this).index();
			
			//alert(tmBooks.albums[index].bookUrl);
			location.href = tmBooks.albums[index].bookUrl;
		});
		return;
	}
	
	for(var i = 0 ; i < tmBooks.books.length ; i++){
		$("#tm_list_tmpl").tmpl(tmBooks.books[i]).appendTo($('.tm_list', context));
	}
	
	$('.tm_list li', context).bind('vclick', function(e){
		e.preventDefault();
		var height = $(this).height();
		var index = $(this).index();
		
		$('.tm_list_on', context).stop().animate({marginTop:height*index}, 300);
		
		getTmainContent(index, type);
	});
}

function getType(type){
	if(type == 'today'){
		return '#tab01';
	}else if(type == 'hotissue'){
		return '#tab02';
	}else if(type == 'album'){
		return '#tab03';
	}
}

function getTypeJSON(type){
	if(type == 'today'){
		return tmBooks1;
	}else if(type == 'hotissue'){
		return tmBooks2;
	}else if(type == 'album'){
		return tmBooks3;
	}
}

function getTmainJSON(type){
	var context = $(getType(type));
	var jsonUrl = '';
	if(type == 'today'){
		jsonUrl = tmJson1;
	}else if(type == 'hotissue'){
		jsonUrl = tmJson2;
	}else if(type == 'album'){
		jsonUrl = tmJson3;
	}
	
	$.ajax({
		url: jsonUrl,
		dataType: 'json',
		beforeSend: function(jqXHR, settings){
			$('.tm_view_wrap', context).html(loading_spinner);
		},
		success: function(data, textStatus, jqXHR){
			if(type == 'today'){
				tmBooks1 = data;
			}else if(type == 'hotissue'){
				tmBooks2 = data;
			}else if(type == 'album'){
				tmBooks3 = data;
			}
		},
		complete: function(jqXHR, textStatus){
			createTmainList(type);
			
			getTmainContent(0, type);
		},
		error: function(jqXHR, textStatus, errorThrown){
		}
	});
}

$(function(){
	if(!tmBooks1){
		getTmainJSON('today');
	}
	
	$('#tm_tab a').bind('vclick', function(){
		var index = $(this).index() + 1;
		$('#tm_tab a').removeClass('on');
		$(this).addClass('on');
		
		$('#tab01, #tab02, #tab03').hide();
		$('#tab0'+index).show();
		
		if(index == 1){
			if(!tmBooks1){
				getTmainJSON('today');
			}
		}else if(index == 2){
			if(!tmBooks2){
				getTmainJSON('hotissue');
			}
		}else if(index == 3){
			if(!tmBooks3){
				getTmainJSON('album');
			}
		}
	});
});

// end: today's book

function shareBook(type, bookUrl){
	if(type == 'twitter'){
		
	}else if(type == 'facebook'){
		
	}else if(type == 'me2day'){
		
	}else if(type == 'yozm'){
		
	}
}

$(function(){
	//BestSellerTab
	$('#BestSellerTab li:nth-child(5)').bind('vclick', function(){			
		$(this).addClass('on');	
		$('#BestSellerTab li:nth-child(4)').removeClass('on');	
		$('#BestSellerTab li:nth-child(3)').removeClass('on');	
		$('#BestSellerTab li:nth-child(2)').removeClass('on');
		$('#rankTab02').addClass('disnn');	
		$('#rankTab03').addClass('disnn');	
		$('#rankTab04').addClass('disnn');	
		$('#rankTab01').removeClass('disnn');	
	});
	$('#BestSellerTab li:nth-child(4)').bind('vclick', function(){			
		$(this).addClass('on');	
		$('#BestSellerTab li:nth-child(5)').removeClass('on');	
		$('#BestSellerTab li:nth-child(3)').removeClass('on');	
		$('#BestSellerTab li:nth-child(2)').removeClass('on');
		$('#rankTab01').addClass('disnn');	
		$('#rankTab03').addClass('disnn');	
		$('#rankTab04').addClass('disnn');	
		$('#rankTab02').removeClass('disnn');	
	});
	$('#BestSellerTab li:nth-child(3)').bind('vclick', function(){			
		$(this).addClass('on');	
		$('#BestSellerTab li:nth-child(5)').removeClass('on');	
		$('#BestSellerTab li:nth-child(4)').removeClass('on');	
		$('#BestSellerTab li:nth-child(2)').removeClass('on');
		$('#rankTab01').addClass('disnn');	
		$('#rankTab02').addClass('disnn');	
		$('#rankTab04').addClass('disnn');	
		$('#rankTab03').removeClass('disnn');	
	});
	$('#BestSellerTab li:nth-child(2)').bind('vclick', function(){			
		$(this).addClass('on');	
		$('#BestSellerTab li:nth-child(5)').removeClass('on');	
		$('#BestSellerTab li:nth-child(4)').removeClass('on');	
		$('#BestSellerTab li:nth-child(3)').removeClass('on');
		$('#rankTab01').addClass('disnn');	
		$('#rankTab02').addClass('disnn');	
		$('#rankTab03').addClass('disnn');	
		$('#rankTab04').removeClass('disnn');	
	});
	
	// tablet main event rolling		
	$('#touchWipeImg').cycle({
		timeout: 4000,
		fx: 'scrollHorz',
		next: '#rightControl',
		prev: '#leftControl' 
	});
 
	$("#touchWipeImg").touchwipe({
 		wipeLeft: function() {
 	 		$("#touchWipeImg").cycle("next");
 		},
 		wipeRight: function() {
 	 		$("#touchWipeImg").cycle("prev");
 		}
	});	
			
});	