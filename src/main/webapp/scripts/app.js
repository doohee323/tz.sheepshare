'use strict';

var config = {
//	url : 'http://localhost\\:3000',
//	url: 'http://sheeprails.herokuapp.com',
	url : 'http://localhost\\:7001/tz.sheepshare/rest',
	// url : '/pattern/pt42/masterdetail',
	server: 'spring' // spring, rails,
};

angular.module('sheepwebApp', ['ngResource'])
	.constant('config', config)
	.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/', {
		redirectTo : '/tz.sheepshare/centers'
	})
	.when('/tz.sheepshare/centers', {
		controller : 'CentersCtrl',
		templateUrl : './views/centers.html'
	})
	.when('/tz.sheepshare/center/:id', {
		controller : 'CentersCtrl',
		templateUrl : './views/centers.html'
	})	
	.when('/tz.sheepshare/regions/:id', {
		controller : 'RegionsCtrl',
		templateUrl : '/views/regions.html'
	})
	.otherwise({
		redirectTo : '/tz.sheepshare/centers'
	});
	
	$locationProvider.html5Mode(true).hashPrefix('!');

});
