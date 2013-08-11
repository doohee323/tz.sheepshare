_.templateSettings = {
	    interpolate: /\<\@\=(.+?)\@\>/gim,
	    evaluate: /\<\@(.+?)\@\>/gim,
	    escape: /\<\@\-(.+?)\@\>/gim
	};

var gradeImg = '<img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  />';
gradeImg += '<img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  />';
gradeImg += '<img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  />';
gradeImg += '<img src="http://image.kyobobook.com/mimages/static/images/common/ico_star_on.png" alt=""  />';
gradeImg += '<img src="http://image.kyobobook.com/mimages/static/images/common/ico_star.png" alt=""  />';

var Movies = {
	grade : '1위',
	title : '아프니까 청춘이다',
	booksImg : '<img src="http://image.kyobobook.com/mimages/static/images/books/img_book.jpg" alt="" />',
	writer : '김난도',
	publisher : '샘앤파커스',
	price : '12,600',
	gradeImg : gradeImg,
	reviewCnt : '8',
	facebookLikeCnt : '11'
};
var InitDiv;

// view
var MovieV = Backbone.View.extend({
	el : $("#MovieUL"),
	tagName : "li",
	className : "st",
	template : $("#item-template").html(),
	initialize : function() {
		InitDiv = this.$el.html();
		tmpl = _.template(this.template);
	},
	render : function() {
	},
	doMovie : function(e) {
		var tmpl = _.template(this.template);
		var params = {
			params : {
				typeCd : 'movie'
			}
		};
		$.ajax({
			type : "post",
			url : gPath + '/com/sheepshare/mainPage/retrieveMainPageList.ajax',
			data : JSON.stringify(params),
			datatype : "json",
			contentType : "application/json+core; charset=utf-8",
			success : function(data) {
//				debugger;
				if (data && data.Movies) {
					var script = '';
					for(var i=0;i<data.Movies.length;i++) {
						script += tmpl(data.Movies[i]);
					}
					$("#MovieUL").html(InitDiv + script);
				}
			}
		});
	},
	events : {
		"click #add" : "doMovie"
	}
});

// Router
var AppRouter = Backbone.Router.extend({
	routes : {
		"/posts/:id" : "getPost",
		"*actions" : "defaultRoute"
	},
	getPost : function(id) {
		alert("Get post number " + id);
	},
	loadView : function(route, action) {
		alert(route + "_" + action);

	},
	defaultRoute : function(actions) {
	}
});

var app_router = new AppRouter;
Backbone.history.start();

var movieV = new MovieV();
movieV.doMovie();
