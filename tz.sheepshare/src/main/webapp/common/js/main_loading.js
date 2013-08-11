var bookSet1, bookSet2, bookSet3;
var jsonUrl1 = '/main/today';
var jsonUrl2 = '/main/hotissue';
var jsonUrl3 = '/main/prealbum';
// var jsonUrl1 = '/_test/tmain_content1.json';
// var jsonUrl2 = '/_test/tmain_content2.json';
// var jsonUrl3 = '/_test/tmain_content3.json';
var Cate1Imgs = new Array;
var Cate2Imgs = new Array;
var Cate3Imgs = new Array;

$(function(){
	showTab1();
	
	$('.tab_Type li').bind('vclick', function(){
		var index = $(this).index();
		
		if(index == 0){
			$(this).addClass('on');
			$(this).parent().find('li').eq(1).removeClass('on');
			$('#mainSection1').show();
			$('#mainSection2').hide();
			showTab1();
		}else{
			$(this).addClass('on');
			$(this).parent().find('li').eq(0).removeClass('on');
			$('#mainSection1').hide();
			$('#mainSection2').show();
			showTab2();
		}
	});
});

function showTab1(){
	if(!bookSet1){
		loadJson1();
	}
}

function showTab2(){
	if(!bookSet2){
		loadJson2();
	}
}

function removeLoading(section){
	$('.loadImageArea', $('#mainSection' + section)).remove();
}

function loadJson1(){
	$.ajax({
		url: jsonUrl1, dataType: 'json',
		beforeSend: function(jqXHR, settings){
		},
		success: function(data, textStatus, jqXHR){
			bookSet1 = data;
		},
		complete: function(jqXHR, textStatus){
			loadBookSet1();
		},
		error: function(jqXHR, textStatus, errorThrown){
			//console.log('throw error');
		}
	});
}

function loadJson2(){
	$.ajax({
		url: jsonUrl2, dataType: 'json',
		beforeSend: function(jqXHR, settings){
			$('.loadImageArea', $('#mainSection2')).css({'display':'block'});
		},
		success: function(data, textStatus, jqXHR){
			bookSet2 = data;
		},
		complete: function(jqXHR, textStatus){
			loadBookSet2();
		},
		error: function(jqXHR, textStatus, errorThrown){
			//console.log('throw error');
		}
	});
}

function loadJson3(){
	$.ajax({
		url: jsonUrl3, dataType: 'json',
		beforeSend: function(jqXHR, settings){
			$('.loadImageArea', $('#mainSection3')).css({'display':'block'});
		},
		success: function(data, textStatus, jqXHR){
			bookSet3 = data;
		},
		complete: function(jqXHR, textStatus){
			loadBookSet3();
		},
		error: function(jqXHR, textStatus, errorThrown){
			//console.log('throw error');
		}
	});
}

function loadDefaultElement(){
	var il = new ImageLoading();
	il.init($('#mainSection1'), '.tempLoadArea', ['http://image.kyobobook.com/mimages/static/images/main/over_border.png']);
	il.loading(function(){
		loadBookSet1();
	});
}

function loadBookSet1(){
	for(var i = 0 ; i < bookSet1.books.length ; i++){
		Cate1Imgs[i] = bookSet1.books[i].imageSrc;
	}
	
	var il = new ImageLoading();
	il.init($('#mainSection1'), '.tempLoadArea', Cate1Imgs);
	il.loading(function(){
		loadScript();
	});
}

function loadBookSet2(){
	for(var i = 0 ; i < bookSet2.books.length ; i++){
		Cate2Imgs[i] = bookSet2.books[i].imageSrc;
	}
	
	var il = new ImageLoading();
	il.init($('#mainSection2'), '.tempLoadArea', Cate2Imgs);
	il.loading(function(){
		var kymp2 = new KYOBOMOBILE_MAIN_PHONE();
		kymp2.init(bookSet2, $('#mainSection2'));
		
		removeLoading(2);
	});
}

function loadBookSet3(){
	for(var i = 0 ; i < bookSet3.albums.length ; i++){
		Cate3Imgs[i] = bookSet3.albums[i].imageSrc;
	}
	
	var il = new ImageLoading();
	il.init($('#mainSection3'), '.tempLoadArea', Cate3Imgs);
	il.loading(function(){
		var kymp3 = new KYOBOMOBILE_MAIN_PHONE();
		kymp3.init(bookSet3, $('#mainSection3'));
		
		removeLoading(3);
	});
}

function loadScript(){
	$.ajax({
		url: gPath+'/common/js/main.js',
		dataType: 'script',
		complete: function(jqXHR, textStatus){
			var kymp = new KYOBOMOBILE_MAIN_PHONE();
			kymp.init(bookSet1, $('#mainSection1'));
			
			removeLoading(1);
		},
		error: function(jqXHR, textStatus, errorThrown){}
	});
}

var ImageLoading = function(){};
ImageLoading.prototype = {
	context: null,
	target: null,
	images: [],
	source: null,
	callBackFunction: null,
	init: function(context, target, source){
		this.context = context;
		this.target = target;
		this.source = source;
	},
	loading: function(callBackFunction){
		var index = 0;
		var appendDiv = $(this.target, this.context);
		var images = this.images;
		var source = this.source;
		var imagesLength = source.length;
		for(var i = 0 ; i < imagesLength ; i++){
			images[i] = new Image();
			
			appendDiv.append(function(idx, html) {
				var img = $(images[i]).load(function() {
					index++;
					if(imagesLength == index){
						if (typeof callBackFunction == 'function') {
							callBackFunction.call();
						}
						appendDiv.remove();
					}
				});
				$(img).hide();
				img.attr({src:source[i]});
				
				return img;
			});
		}
	}
};

// 네비게이션 반응 음반 메인비주얼
function gnb_open_complete(){
	$('#bookArea').hide();
	$('#albumArea').show();
	$('#today_sale').hide();
	$('#new_bray').show();
	
	if(!bookSet3){
		loadJson3();
	}
}

function gnb_close_complete(){
	$('#bookArea').show();
	$('#albumArea').hide();
	$('#today_sale').show();
	$('#new_bray').hide();
}

$(function(){

	// touchwipe event rolling		
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


