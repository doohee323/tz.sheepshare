'use strict';

var config = {
//	url : 'http://localhost\\:3000',
//	url: 'http://sheeprails.herokuapp.com',
	url : 'http://localhost\\:8080/rest',
	// url : '/pattern/pt42/masterdetail',
	server: 'spring' // spring, rails,
};

angular.module('sheepwebApp', ['ngResource'])
	.constant('config', config)
	.config(function($routeProvider, $locationProvider) {
	$routeProvider
	.when('/', {
		redirectTo : '/centers'
	})
	.when('/centers', {
		controller : 'CentersCtrl',
		templateUrl : './views/centers.html'
	})
	.when('/center/:id', {
		controller : 'CentersCtrl',
		templateUrl : './views/centers.html'
	})	
	.when('/regions/:id', {
		controller : 'RegionsCtrl',
		templateUrl : '/views/regions.html'
	})
	.otherwise({
		redirectTo : '/centers'
	});
	
	$locationProvider.html5Mode(true).hashPrefix('!');

});
