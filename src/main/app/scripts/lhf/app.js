'use strict';

var config = {
	url : 'http://localhost\\:3000',
	server: 'spring', // spring, rails,
	centers: {}
};

var app = angular.module('sheepgridApp', ['ngResource', 'ui.router']);

app.constant('config', config)
	.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
		// default route
	    $urlRouterProvider.otherwise("/");
	    
		// default route
		$stateProvider
        .state('default', {
            templateUrl: '/views/layout/default.html',
            controller: 'DefaultCtrl',
            abstract: true
	      })
	      .state('default.main', {
	        templateUrl: '/index.html',
	        controller: 'MainCtrl',
	      });

	$locationProvider.html5Mode(true).hashPrefix('!');

});
