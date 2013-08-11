function share_facebook(url, title) {
	window.open("http://www.facebook.com/sharer.php?u="+encodeURIComponent(url),
			'share_facebook','');
}

function share_twitter(url, title) {
	window.open('http://twitter.com/share?count=horizontal&text='+encodeURIComponent(striptTags(title))+
			"&url="+encodeURIComponent(url),'share_twitter','toolbar=1,status=1,width=800,height=500');
}

function share_me2day(url, title) {
	var text = '"'+striptTags(title)+'":'+url;
	window.open("http://me2day.net/plugins/mobile_post/new?new_post[body]="+encodeURIComponent(text)+"&new_post[tags]=&post[icon]=3",
			'share_me2day','');
}

function share_yozm(url, title) {
	window.open("http://m.yozm.daum.net/user/message/post?link="+encodeURIComponent(url)+"&prefix="+encodeURIComponent(title),
			'share_yozm','');
}

function striptTags(str) {
	return str.replace(/<\/?[^>]+(>|$)/g, '');
}