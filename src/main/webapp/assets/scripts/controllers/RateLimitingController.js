demoModule.controller('RateLimitingCtrl', [ '$scope', 'apiService',function($scope,$apiService) {
    $scope.success = false;
    $scope.error = false;
    $scope.ratelimiting = {
        threshold: 5,
        ttl: 10,
        value: "value1"
    };

    $scope.setThreshold = function () {
        $apiService.setRateLimitingThreshold($scope.ratelimiting.threshold,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };

    $scope.insertValue = function () {
        $apiService.rateLimitingInsertValue($scope.ratelimiting.value,$scope.ratelimiting.ttl,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };
} ]);