'use strict';

demoModule.controller('CountDownCtrl', ['$scope','apiService', function($scope, $apiService) {
    $scope.success = false;
    $scope.error = false;
    $scope.countdown = {
        ttl: 100
    };

    $scope.setTtl = function () {
        $apiService.setCountDownTTL($scope.countdown.ttl,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };

    $scope.getCountDown = function () {
        $apiService.getCountDown($apiService.createSuccess($scope),
            $apiService.createError($scope));
    };
} ]);
