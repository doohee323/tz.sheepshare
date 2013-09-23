var app = angular.module('serverApp', []);

app.config(function($routeProvider) {
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
});
