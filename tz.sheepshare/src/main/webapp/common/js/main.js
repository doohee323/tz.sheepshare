

	var m = Math;
	var hasTouch = 'ontouchstart' in window;
	
	var RESIZE_EV = 'onorientationchange' in window?'orientationchange':'resize';
	var START_EV = hasTouch?'touchstart':'mousedown';
	var MOVE_EV = hasTouch?'touchmove':'mousemove';
	var END_EV = hasTouch?'touchend':'mouseup';
	var CANCEL_EV = hasTouch?'touchcancel':'mouseup';
	
	var KYOBOMOBILE_MAIN_PHONE = function(){
	};
	
	KYOBOMOBILE_MAIN_PHONE.prototype = {
		times: 300,
		currentId: 2,
		bookSet: null,
		bookSetLength: 0,
		reTouchTime: 320,
		reTouchTimerId: 0,
		context:null,
		isBook:true,
		init: function(json, context){
			if(json.albums){
				this.isBook = false;
			}

			this.context = context;
			this.currentId = 2;
			bookSet = null;
			
			if(json == null){
				alert('json이 없습니다.');
				return;
			}
			this.bookSet = json;
			if(this.isBook){
				this.bookSetLength = this.bookSet.books.length;
			}else{
				this.bookSetLength = this.bookSet.albums.length;
			}
			
			var that = this;
			
			var tempBone = '';
			tempBone += '<div class="aniStage">';
			if(this.isBook){
				tempBone += '	<div class="rollingBack">';				
			}else{
				tempBone += '	<div class="rollingBack" style="left:-9px;">';
			}
			tempBone += '		<ul class="rollingBackSet">';
			tempBone += '		</ul>';
			tempBone += '	</div>';
			
			
			if(this.isBook){
				tempBone += '	<div class="rollingFront">';
				tempBone += '		<ul class="rollingFrontSet">';
			}else{
				tempBone += '	<div class="rollingFront rollingFrontAlbum">';
				tempBone += '		<ul class="rollingFrontSet rollingFrontSetAlbum">';
			}
			
			tempBone += '		</ul>';
			tempBone += '	</div>';
			tempBone += '	<div class="rollingFrontOveray">';
			if(this.isBook){
				tempBone += '		<img src="http://image.kyobobook.com/mimages/static/images/main/over_border.png"/>';
			}else{
				tempBone += '		<img src="http://image.kyobobook.com/mimages/static/images/main/over_border_album.png"/>';
			}
			tempBone += '	</div>';
			tempBone += '</div>';
			
			$('.book_rolling', this.context).html(tempBone);
			
			var tempUlContent = '';
			
			if(this.isBook){
				tempUlContent += '<li><img src="'+this.bookSet.books[0].imageSrc+'" style="width: 94px; height: 127px; margin-top: 34px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.books[1].imageSrc+'" style="width: 106px; height: 143px; margin-top: 26px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.books[2].imageSrc+'" style="width: 106px; height: 143px; margin-left: -28px; margin-top: 26px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.books[3].imageSrc+'" style="width: 106px; height: 143px; margin-left: -30px; margin-top: 26px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.books[4].imageSrc+'" style="width: 94px; height: 127px; margin-top: 34px;" /></li>';
			}else{
				tempUlContent += '<li><img src="'+this.bookSet.albums[0].imageSrc+'" style="width: 104px; height: 104px; margin-top: 28px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.albums[1].imageSrc+'" style="width: 134px; height: 134px; margin-top: 13px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.albums[2].imageSrc+'" style="width: 134px; height: 134px; margin-left: -72px; margin-top: 13px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.albums[3].imageSrc+'" style="width: 134px; height: 134px; margin-left: -72px; margin-top: 13px;" /></li>';
				tempUlContent += '<li><img src="'+this.bookSet.albums[4].imageSrc+'" style="width: 104px; height: 104px; margin-top: 28px;" /></li>';
			}

			$('.rollingBackSet', this.context).append(tempUlContent);
			
			var tempDesc = '';
			
			if(this.isBook){
				var bookDesc = this.bookSet.books;
				
				for(var i = 0 ; i < this.bookSetLength ; i++){
					tempDesc ='';
					tempDesc += '<ul class="book">';
					tempDesc += '<li class="tcolor_b txt_s14"><a href="'+bookDesc[i].bookUrl+'">'+bookDesc[i].title+'</a></li>';
					tempDesc += '<li class="write"><span class="name">'+bookDesc[i].writer+'</span><span class="publisher">'+bookDesc[i].publisher+'</span></li>';
					tempDesc += '<li class="price"><span class="p_price text_st">'+bookDesc[i].price+'</span><span class="won">원</span>';
					tempDesc += '(<span class="p_sale">'+bookDesc[i].sale+'%</span>';
					tempDesc += '+ <span class="p_point">'+bookDesc[i].point+'</span>원)</li>';
					tempDesc += '</ul>';
					
					$('.books_detail', this.context).append(tempDesc);
				}
			}else{
				var bookDesc = this.bookSet.albums;
				
				for(var i = 0 ; i < this.bookSetLength ; i++){
					tempDesc ='';
					tempDesc += '<ul class="book">';
					tempDesc += '<li class="tcolor_b txt_s14"><a href="'+bookDesc[i].link+'">'+bookDesc[i].title+'</a></li>';
					tempDesc += '<li class="write"><span class="name">'+bookDesc[i].author+'</span><span class="publisher">'+bookDesc[i].publisher+'</span></li>';
					tempDesc += '<li class="price"><span class="p_price text_st">'+bookDesc[i].price+'</span><span class="won">원</span>';
					tempDesc += '(<span class="p_sale">'+bookDesc[i].sale+'%</span>';
					tempDesc += '+ <span class="p_point">'+bookDesc[i].point+'</span>원)</li>';
					tempDesc += '</ul>';
					
					$('.books_detail', this.context).append(tempDesc);
				}
			}
			
			
			$('.books_detail ul', this.context).hide();
			
			var tempUlContent2 = '';
			if(this.isBook){
				tempUlContent2 += '<li><img src="'+this.bookSet.books[2].imageSrc+'"/></li>';
			}else{
				tempUlContent2 += '<li><img src="'+this.bookSet.albums[2].imageSrc+'"/></li>';
			}
			
			$('.rollingFrontSet', this.context).append(tempUlContent2);

			$('.prev', this.context).bind('vclick', this, function(e){
				e.data.btnPrev();				
			});

			$('.next', this.context).bind('vclick', this, function(e){
				e.data.btnNext();
			});
			
			$('.aniStage', this.context).bind(START_EV, this, this.touchStart);

			this.setDesc(2);
			
			$('.rollingFrontOveray', this.context).bind('vclick', this, function(e){
				var that = e.data;
				var index = that.calId(that.currentId);
				//alert(getLink(index));
				//location.href = '/books/books_detail.html';
			});
		},
		
		setDesc: function(index){
			$('.books_detail ul', this.context).hide();
			$('.books_detail ul', this.context).eq(index).show();
		},
		
		getLink: function(index){
			return this.bookSet.books[index].bookUrl;
		},
		
		btnPrev: function(){
			var context = this.context;
			
			if(this.reTouchTimerId){
				return;
			}else{
				this.reTouchTimerId = setTimeout(function(obj){
					obj.reTouchTimerId = 0;
				}, this.reTouchTime, this);
			}
			
			this.currentId--;
			var index = this.calId(this.currentId);
			
			if(this.isBook){
				$('.rollingBackSet', context).prepend('<li><img src="'+this.bookSet.books[(index+(this.bookSetLength-2))%this.bookSetLength].imageSrc+'" style="width: 94px; height: 127px; margin-top: 34px;" /></li>');
				$('.rollingBackSet', context).css({left:'-94px'});

				this.moveFrontSetPrev((index)%this.bookSetLength, this.times);

				$('.rollingBackSet', context).stop().animate({left:'0px'}, this.times);

				$('.rollingBackSet li', context).eq(1).find('img').stop().animate({width:'106px', height:'143px', marginTop:'26px'}, this.times);
				$('.rollingBackSet li', context).eq(2).find('img').stop().animate({marginLeft:'-28px'}, this.times);
				$('.rollingBackSet li', context).eq(3).find('img').stop().animate({marginLeft:'-30px'}, this.times, function(){
					$('.rollingBackSet li', context).eq(5).remove();
				});
				$('.rollingBackSet li', context).eq(4).find('img').stop().animate({width:'94px', height:'127px', marginTop:'34px', marginLeft:'0px'}, 100);
			}else{
				$('.rollingBackSet', context).prepend('<li><img src="'+this.bookSet.albums[(index+(this.bookSetLength-2))%this.bookSetLength].imageSrc+'" style="width: 104px; height: 104px; margin-top: 28px;" /></li>');
				$('.rollingBackSet', context).css({left:'-103px'});

				this.moveFrontSetPrev((index)%this.bookSetLength, this.times);

				$('.rollingBackSet', context).stop().animate({left:'0px'}, this.times);

				$('.rollingBackSet li', context).eq(1).find('img').stop().animate({width:'134px', height:'134px', marginTop:'13px'}, this.times);
				$('.rollingBackSet li', context).eq(2).find('img').stop().animate({marginLeft:'-72px'}, this.times);
				$('.rollingBackSet li', context).eq(3).find('img').stop().animate({marginLeft:'-72px'}, this.times, function(){
					$('.rollingBackSet li', context).eq(5).remove();
				});
				$('.rollingBackSet li', context).eq(4).find('img').stop().animate({width:'104px', height:'104px', marginTop:'28px', marginLeft:'0px'}, 100);
			}
		},

		btnNext: function(){
			var context = this.context;
			if(this.reTouchTimerId){
				return;
			}else{
				this.reTouchTimerId = setTimeout(function(obj){
					obj.reTouchTimerId = 0;
				}, this.reTouchTime, this);
			}
			var test = 'asdfafdafasdfasdf';
			this.currentId++;
			var index = this.calId(this.currentId);
			
			if(this.isBook){
				$('.rollingBackSet', context).append('<li><img src="'+this.bookSet.books[(index+2)%this.bookSetLength].imageSrc+'" style="width: 94px; height: 127px; margin-top: 34px;" /></li>');

				this.moveFrontSetNext((index)%this.bookSetLength, this.times);

				$('.rollingBackSet', context).stop().animate({left:'-94px'}, this.times);

				$('.rollingBackSet li', context).eq(1).find('img').stop().animate({width:'94px', height:'127px', marginTop:'34px', marginLeft:'0px'}, this.times);
				$('.rollingBackSet li', context).eq(2).find('img').stop().animate({marginLeft:'0px'}, this.times);
				$('.rollingBackSet li', context).eq(3).find('img').stop().animate({marginLeft:'-28px'}, this.times, function(){
					$('.rollingBackSet li', context).eq(0).remove();
					$('.rollingBackSet', context).css({left:'0px'});
				});
				$('.rollingBackSet li', context).eq(4).find('img').stop().animate({width:'106px', height:'143px', marginTop:'26px', marginLeft:'-30px'}, 100);
			}else{
				$('.rollingBackSet', context).append('<li><img src="'+this.bookSet.albums[(index+2)%this.bookSetLength].imageSrc+'" style="width: 104px; height: 104px; margin-top: 28px;" /></li>');

				this.moveFrontSetNext((index)%this.bookSetLength, this.times);

				$('.rollingBackSet', context).stop().animate({left:'-103px'}, this.times);

				$('.rollingBackSet li', context).eq(1).find('img').stop().animate({width:'104px', height:'104px', marginTop:'28px', marginLeft:'0px'}, this.times);
				$('.rollingBackSet li', context).eq(2).find('img').stop().animate({marginLeft:'0px'}, this.times);
				$('.rollingBackSet li', context).eq(3).find('img').stop().animate({marginLeft:'-72px'}, this.times, function(){
					$('.rollingBackSet li', context).eq(0).remove();
					$('.rollingBackSet', context).css({left:'0px'});
				});
				$('.rollingBackSet li', context).eq(4).find('img').stop().animate({width:'134px', height:'134px', marginTop:'13px', marginLeft:'-72px'}, 100);
			}
		},

		calId: function(index){
			index = index%this.bookSetLength;
			if(index < 0){
				index = this.bookSetLength + index;
			}
			return index;
		},

		moveFrontSetPrev: function(index, times){
			var context = this.context;
			if(this.isBook){
				$('.rollingFrontSet', context).prepend('<li><img src="'+this.bookSet.books[index].imageSrc+'"/></li>');
				$('.rollingFrontSet', context).css({'left':'-137px'}).animate({left:0}, times, function(){
					$('.rollingFrontSet li', context).eq(1).remove();
				});
			}else{
				$('.rollingFrontSet', context).prepend('<li><img src="'+this.bookSet.albums[index].imageSrc+'"/></li>');
				$('.rollingFrontSet', context).css({'left':'-148px'}).animate({left:0}, times, function(){
					$('.rollingFrontSet li', context).eq(1).remove();
				});
			}
			
			this.setDesc(index);
		},

		moveFrontSetNext: function(index, times){
			var context = this.context;
			if(this.isBook){
				$('.rollingFrontSet', context).append('<li><img src="'+this.bookSet.books[index].imageSrc+'"/></li>');
				$('.rollingFrontSet', context).animate({left:'-137px'}, times, function(){
					$('.rollingFrontSet li', context).eq(0).remove();
					$('.rollingFrontSet', context).css({'left':'0px'});
				});
			}else{
				$('.rollingFrontSet', context).append('<li><img src="'+this.bookSet.albums[index].imageSrc+'"/></li>');
				$('.rollingFrontSet', context).animate({left:'-148px'}, times, function(){
					$('.rollingFrontSet li', context).eq(0).remove();
					$('.rollingFrontSet', context).css({'left':'0px'});
				});
			}
			
			this.setDesc(index);
		},
		
		touchStart: function(e){
			var that = e.data;
			var point = hasTouch ? event.touches[0] : event;
			that.moved = false;
			that.startX = 0;
			that.startY = 0;

			that.startX = point.pageX;
			that.startY = point.pageY;
			
			$('.aniStage', that.context).bind(MOVE_EV, that, that.touchMove);
			$('.aniStage', that.context).bind(END_EV, that, that.touchEnd);
		},

		touchMove: function(e){
			var that = e.data;
			
			var eventThat = this;
			var point = hasTouch ? event.touches[0] : event;
			
			that.pointX = point.pageX;
			that.pointY = point.pageY;

			var x = that.pointX - that.startX;
			var y = that.pointY - that.startY;
			
			if(Math.abs(x) >= Math.abs(y)/1.5){
				e.preventDefault();
			}else{
				$('.aniStage', that.context).unbind(MOVE_EV, that.touchMove);
				$('.aniStage', that.context).unbind(END_EV, that.touchEnd);
				return;
			}
			
			if(x > 50){
				that.btnPrev();
				$('.aniStage', that.context).unbind(MOVE_EV, that.touchMove);
				$('.aniStage', that.context).unbind(END_EV, that.touchEnd);
			} else if(x < -50){
				that.btnNext();
				$('.aniStage', that.context).unbind(MOVE_EV, that.touchMove);
				$('.aniStage', that.context).unbind(END_EV, that.touchEnd);
			}
			
			that.moved = true;
		},

		touchEnd: function(e) {
			var that = e.data;
			if (hasTouch && event.touches.length != 0)
				return;
			
			var point = hasTouch ? event.changedTouches[0] : event;

			that.endX = point.pageX;
			that.endY = point.pageY;

			$('.aniStage', that.context).unbind(MOVE_EV, that, that.touchMove);
			$('.aniStage', that.context).unbind(END_EV, that, that.touchEnd);
		}
	};

	