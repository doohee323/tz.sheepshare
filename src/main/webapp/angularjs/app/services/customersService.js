app.service('customersService', function($http, $location) {

	var $scope = null;
	var customersService = {};
	var baseUrl = '/com/sheepshare/customer';

	customersService.init = function(scope) {
		$scope = scope;
	};
	
	customersService.retrieveCustomers = function() {
		transManager.exec(baseUrl + '/retrieveCustomerList.ajax', {
			params : {
				customerID : ''
			}
		}, function(data) {
			$scope.customers = data.Customers;
		}, $http).retrieve();
	};

	customersService.saveCustomer = function() {
		var params = {};
		var orders = new Array();
		var customer = $scope.customer;
		customer.orders = null;
		params.customer = customer;
		params.orders = orders;

		transManager.exec(baseUrl + '/saveCustomers.ajax', params,
				function(data) {
					alert(data.result[0].code + "/" + data.result[0].msg);
				}, $http).save();
	};

	customersService.getCustomers = function() {
		return $scope.customers;
	};

	customersService.insertCustomer = function(id, firstName, lastName, city) {
		var cud = 'C';
		if(id) {
			for ( var i = 0; i < $scope.customers.length; i++) {
				if ($scope.customers[i].id == (id + '')) {
					debugger;
					$scope.customers[i].firstName = firstName;
					$scope.customers[i].lastName = lastName;
					$scope.customers[i].city = city;
					cud = 'U';
					break;
				}
			}
		}
		if(cud == 'C') {
			var topID = $scope.customers.length + 1;
			$scope.customers.push({
				id : topID,
				firstName : firstName,
				lastName : lastName,
				city : city
			});
		}
	};
	
	customersService.deleteCustomer = function(id) {
		var customers = $scope.customers;
		for ( var i = customers.length - 1; i >= 0; i--) {
			if (customers[i].id == (id + '')) {
				customers.splice(i, 1);
				break;
			}
		}
	};

	customersService.getCustomer = function(id) {
		var customers = null;
		try {
			customers = $scope.customers;
		} catch (e) {
			$location.path("/customers");
			return;
		}
		
		for ( var i = 0; i < customers.length; i++) {
			if (customers[i].id == (id + '')) {
				return customers[i];
			}
		}
		return null;
	};

	return customersService;
});
