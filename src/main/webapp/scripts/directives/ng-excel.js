'use strict';

app.directive('ngExcel', function($compile, $timeout, config){
	
	var _service = null;
	var _grid = null;
	var _dataset = null;	// ex) uip_center
	var _datasets = null;	// ex) uip_centers
	var _input = null;		// params
	
	var getTemplate = function(grid) {
		return "<div ng-grid=\"" + grid + "\" ng-style=\"myprop()\"></div>";
	};
	
	var linker = function(scope, element, attr) {
		_grid = attr['id'];
		_dataset = attr['data'];

		scope.gridInit = function(service, columnDefs, input) {
			_service = service;
			_input = input;
			_datasets = _dataset + 's';
			
			scope[_grid] = {
			        data: _dataset,
			        multiSelect: true,  
			        enableCellSelection: true,
			        enableCellEditOnFocus: true,
			        enableRowSelection: true,
			        enablePinning: true,
			        enableSorting: true,
			        columnDefs: columnDefs,
			        selectedItems: []
			    };
			
			element.html(getTemplate(_grid)).show();
//			element.bind('click', function() {
//				eval('scope.' + attr["type"] + attr["data"] + '()');
//				return;
//			});
			$compile(element.contents())(scope);
			
			scope[_grid].attr = attr;
		};		
		
		scope.updateEntity = function(column, row, cellValue) {
	        var data = scope[_dataset][row.rowIndex];
	        var rowStatus = scope[_dataset][row.rowIndex].rowStatus;
	        if(rowStatus && rowStatus == 'INSERT') {
	        } else {
	            if(data[column.colDef.field] != cellValue) {
	                scope[_dataset][row.rowIndex].rowStatus = 'UPDATE';
	            }
	        }
	        row.entity[column.field] = cellValue;
	    };
		
		scope.$watch(_dataset, function(newData){
			scope.myprop = function() {
				var attr = scope[_grid].attr;
				if(attr) {
		            return {
		                width: attr["width"] + 'px',
		                height: attr["height"] + 'px'
		            };
				} else {
					return {};
				}
	        };
		}, true);

		scope.getDatas = function(input) {
	    	var params = {};
	    	if(input) params = input;
	    	_service.get(params, function() {
	    		if(this[_datasets] != null) {
		            for (var i = 0; i < this[_datasets].length; i++) {
		                this[_datasets][i].rowStatus = 'NORMAL';
		            };
	    		}
	            scope[_dataset] = this[_datasets];
	        });
	    };

	    scope.retrieveData = function (input) {
	    	if(_input) input = _input;
	    	scope.getDatas(input);
	    };

	    scope.insertData = function (referer, refererId) {
	        var data = scope[_grid].columnDefs;
	        var newData = getAddRow(data);
	        if(referer) {
	        	newData[refererId] = referer.id;
	        }
	        if(_input) newData = mergeData(_input, newData);
	        newData.rowStatus = 'INSERT';
	        var selectRow = 0;
	        if(scope[_dataset] == null) {
	        	var newRow = new Array();
	        	newRow.push(newData);
	        	scope[_dataset] = newRow;
	        } else {
		        scope[_dataset].unshift(newData);
		        selectRow = function() {
		            scope[_grid].selectRow(0, true);
		            //$($($(".ngCellText.col3.colt1")[1]).parent()).parent().focus();
		        };
	        }
	        $timeout(selectRow, 500);
	    };

	    scope.deleteData = function () {
	        for (var i = 0; i < scope[_grid].selectedItems.length; i++) {
	        	var id = scope[_grid].selectedItems[i].id;
		        for (var j = 0; j < scope[_dataset].length; j++) {
		            if(scope[_dataset][j].id == id) {
		                if(scope[_dataset][j].rowStatus == 'INSERT') {
		                    scope[_dataset].splice(j, 1);
		                } else {
		                    scope[_dataset][j].rowStatus = 'DELETE';
		                }
		            }
		        };
	        };
	    };

	    scope.initData = function () {
	    	if(!scope[_grid].selectedItems[0]) return;
	        var id = scope[_grid].selectedItems[0].id;
	        for (var i = 0; i < scope[_dataset].length; i++) {
	            if(scope[_dataset][i].id == id) {
	                for (var j = 0; j < Object.keys(scope[_dataset][i]).length; j++) {
	                    scope[_dataset][i][Object.keys(scope[_dataset][i])[j]] = null;
	                };
	                break;
	            }
	        };
	    };
	    
	    scope.saveData = function () {
	        var dataset = scope[_dataset];
            var input = angular.copy(dataset);
            for (var i = dataset.length - 1; i >= 0; i--) {
	            var rowStatus = dataset[i].rowStatus;
	            if(rowStatus == 'INSERT' || rowStatus == 'UPDATE' || rowStatus == 'DELETE') {
	            } else {
	            	input.splice (i, 1);
	            }
	        };
            
            var params = {};
            params[_dataset] = input;
            _service.save(params, function () {
    	        for (var i = this[_dataset].length - 1; i >= 0; i--) {
    	            var rowStatus = this[_dataset][i].rowStatus;
    	            if(rowStatus == 'INSERT') {
    	            	lookupDs(this[_dataset][i].id, function (row, data){
            				scope[_dataset][row].id = data.id;
            			});
    	            } else if(rowStatus == 'UPDATE') {
    	            } else if(rowStatus == 'DELETE') {
        				lookupDs(this[_dataset][i].id, function (row){
            				scope[_dataset].splice(row, 1);
            			});
    	            }
    	            scope[_dataset][i].rowStatus = 'NORMAL';
    	        };
            });
	    };

		var lookupDs = function ( id, callback ) {
	    	for (var i = 0; i < scope[_dataset].length; i++) {
	    		if(scope[_dataset][i].id == null) { // 신규 입력
	    			
	    		} else if (scope[_dataset][i].id == (id + '')) {
					callback(i, scope[_dataset][i]);
					break;
				};
			};
		};

		var getAddRow = function(source) {
	        var data = angular.copy(source);
	        var target = {};
	        for (var i = 0; i < data.length; i++) {
	            if(data[i].field 
	            	&& data[i].field != 'link'
	            	&& data[i].field != 'CHK') {
	                target[data[i].field] = null;
	            };
	        };
	        return target;
	    };		

	    var mergeData = function(source, target) {
	        for (var j = 0; j < Object.keys(source).length; j++) {
                target[Object.keys(source)[j]] = source[Object.keys(source)[j]];
            };
	        return target;
	    };
	};
	
	return {
		restrict : 'EA',
		link : linker,
		replace : true
	};
});
