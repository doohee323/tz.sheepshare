'use strict';

angular.module('sheepwebApp')
  .directive('commbutton', function($compile) {
		var getTemplate = function(aType, aData, aDataset) {
			var template = '<button id="' + aType + aData + '" class="btn btn-primary" type="button" ng-click="' + aType + aData;
			if(aDataset) {
				template += '(' + aDataset + ')"';
			} else {
				template += '()"';
			}
			if (aType == 'add') {
				template += ' ng-disabled="!new' + aData + '.name || !new' + aData + '.chief || !new' + aData
						+ '.address"';
			// } else {
			// 	template += ' ng-disabled="true"';
			}
			template += '>' + aType + '</button>';
			return template;
		};

		var linker = function(scope, element, attr) {
			element.html(getTemplate(attr["type"], attr["data"], attr["dataset"])).show();
//			element.bind('click', function() {
//				eval('scope.' + attr["type"] + attr["data"] + '()');
//				return;
//			});
			$compile(element.contents())(scope);
		};

		return {
			restrict : 'E',
			link : linker,
			replace : true
		};
	});
