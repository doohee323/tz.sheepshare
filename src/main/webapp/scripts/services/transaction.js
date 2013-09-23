var transManager = {};
transManager.exec = function(url, params, callback, $http) {

	transManager.retrieve = function() {

		$http.post(url, JSON.stringify(params)).success(function(data, status, headers, config) {
			callback(data);
		}).error(function(data, status) {
			data = status;
			callback(data);
		});
	};

	transManager.save = function() {
		$http.post(url, JSON.stringify(params)).success(function(data, status, headers, config) {
			callback(data);
		}).error(function(data, status) {
			data = status;
			callback(data);
		});
	};
	return transManager;
};