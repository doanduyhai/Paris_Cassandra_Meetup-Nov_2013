'use strict';

demoModule.service('apiService', ['$http',function($http) {

    this.setCountDownTTL = function(ttl,success,error) {
        $http.put('countdown/'+ttl)
            .success(success).error(error);
    };

    this.getCountDown = function(success,error) {
        $http.get('countdown')
            .success(success).error(error);
    };

    this.setRateLimitingThreshold = function(threshold,success,error) {
        $http.put('ratelimit/threshold/'+threshold)
            .success(success).error(error);
    };

   this.rateLimitingInsertValue = function (value,ttl,success,error) {
       $http.put('ratelimit/'+value+'/'+ttl)
           .success(success).error(error);
    };

    this.timestampInsertValue = function (value,shift,success,error) {
       $http.put('timestamp/'+value+'/'+shift)
           .success(success).error(error);
    };

    this.writeBarrierDelete = function (shift,success,error) {
       $http.delete('writebarrier/'+shift)
           .success(success).error(error);
    };

    this.writeBarrierInsertValue = function (value,success,error) {
       $http.put('writebarrier/'+value)
           .success(success).error(error);
    };

	this.createSuccess = function($scope) {
		return function(data,status) {
			$scope.success = true;
			$scope.error = false;
			$scope.httpResponse = {
				code: status,
				data: data
			};
		};
	};
	
	this.createError = function($scope) {
		return function(data,status) {
			$scope.success = false;
			$scope.error = true;
			$scope.httpResponse = {
				code: status,
				data: data
			};
		};
	};
}]);