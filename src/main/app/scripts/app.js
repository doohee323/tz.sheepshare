var app = angular.module('serverApp', []);

var config = {
	// url : 'http://localhost:3000/'
	url : 'http://192.168.219.112:3000/',
	// url : '/pattern/pt42/masterdetail',
	server : 'rails' // spring, rails
};

app.constant('config', config).
	config(function($routeProvider) {
	$routeProvider.when('/centers', {
		controller : 'CentersController',
		templateUrl : './views/centers.html'
	}).when('/center/:code', {
		controller : 'CenterController',
		templateUrl : './views/center.html'
	}).when('/centerregions/:code', {
		controller : 'CenterRegionsController',
		templateUrl : './views/centerRegions.html'
	}).when('/regions', {
		controller : 'RegionsController',
		templateUrl : './views/regions.html'
	}).otherwise({
		redirectTo : '/centers'
	});
}).config([ '$httpProvider', function($httpProvider) {
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];
} ]);
