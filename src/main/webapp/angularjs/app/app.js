var app = angular.module('customersApp', []);

app.config(function($routeProvider) {
	$routeProvider.when('/customers', {
		controller : 'CustomersController',
		templateUrl : './partials/customers.html'
	}).when('/customer/:customerID', {
		controller : 'CustomerController',
		templateUrl : './partials/customer.html'
	}).when('/customerorders/:customerID', {
		controller : 'CustomerOrdersController',
		templateUrl : './partials/customerOrders.html'
	}).when('/orders', {
		controller : 'OrdersController',
		templateUrl : './partials/orders.html'
	}).otherwise({
		redirectTo : '/customers'
	});
});
