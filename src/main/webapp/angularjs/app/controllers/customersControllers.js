app.controller('CustomersController', function($scope, $http, customersService) {

	init();

	function init() {
		customersService.init($scope);
		customersService.retrieveCustomers();
		
		$scope.$watch('customers', function(newValue, oldValue) {
			if (newValue === newValue) {
				// alert(1);
			} else {
				// alert(-1);
			}
		}, true);
	}

	$scope.insertCustomer = function() {
		insertCustomer($scope, customersService, 'C');
	};

	$scope.deleteCustomer = function(id) {
		customersService.deleteCustomer(id);
	};
});

app.controller('CustomerController', function($scope, $routeParams, $location, customersService) {
	$scope.newCustomer = {};

	init();

	function init() {
		var customerID = ($routeParams.customerID) ? parseInt($routeParams.customerID) : 0;
		if (customerID > 0) {
			$scope.newCustomer = customersService.getCustomer(customerID);
		}
	}

	$scope.insertCustomer = function() {
		insertCustomer($scope, customersService, 'U	');
	};

	$scope.listCustomer = function() {
		$location.path("/customers");
	};

	$scope.saveCustomer = function() {
		customersService.saveCustomer();
	};
});

app.controller('CustomerOrdersController', function($scope, $routeParams, customersService) {
	$scope.customer = {};
	$scope.ordersTotal = 0.00;

	init();

	function init() {
		var customerID = ($routeParams.customerID) ? parseInt($routeParams.customerID) : 0;
		if (customerID > 0) {
			$scope.customer = customersService.getCustomer(customerID);
		}
	}

});

app.controller('OrdersController', function($scope, customersService) {
	$scope.customers = [];

	init();

	function init() {
		$scope.customers = customersService.getCustomers();
	}
});

app.controller('NavbarController', function($scope, $location) {
	$scope.getClass = function(path) {
		if ($location.path().substr(0, path.length) == path) {
			return true;
		} else {
			return false;
		}
	};
});

app.controller('OrderChildController', function($scope) {
	$scope.orderby = 'product';
	$scope.reverse = false;
	$scope.ordersTotal = 0.00;

	init();

	function init() {
		if ($scope.customer && $scope.customer.orders) {
			var total = 0.00;
			for ( var i = 0; i < $scope.customer.orders.length; i++) {
				var order = $scope.customer.orders[i];
				total += order.orderTotal;
			}
			$scope.ordersTotal = total;
		}
	}

	$scope.setOrder = function(orderby) {
		if (orderby === $scope.orderby) {
			$scope.reverse = !$scope.reverse;
		}
		$scope.orderby = orderby;
	};

});

var insertCustomer = function($scope, customersService, cu) {
	var id = $scope.newCustomer.id;
	var firstName = $scope.newCustomer.firstName;
	var lastName = $scope.newCustomer.lastName;
	var city = $scope.newCustomer.city;
	customersService.insertCustomer(id, firstName, lastName, city);

	if (cu == '') {
		$scope.newCustomer.id = '';
		$scope.newCustomer.firstName = '';
		$scope.newCustomer.lastName = '';
		$scope.newCustomer.city = '';
	}
};