var app = angular.module('centersApp', []);

app.config(function($routeProvider) {
	$routeProvider.when('/centers', {
		controller : 'CentersController',
		templateUrl : './partials/centers.html'
	}).when('/center/:code', {
		controller : 'CenterController',
		templateUrl : './partials/center.html'
	}).when('/centerregions/:code', {
		controller : 'CenterRegionsController',
		templateUrl : './partials/centerRegions.html'
	}).when('/regions', {
		controller : 'RegionsController',
		templateUrl : './partials/regions.html'
	}).otherwise({
		redirectTo : '/centers'
	});
});
