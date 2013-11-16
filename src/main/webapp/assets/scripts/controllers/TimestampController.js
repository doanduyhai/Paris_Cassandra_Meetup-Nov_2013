'use strict';

demoModule.controller('TimestampCtrl',['$scope','apiService',function($scope,$apiService) {
    $scope.success = false;
    $scope.error = false;
    $scope.timestamp = {
        shift: 10,
        value: "value1"
    };

    $scope.insertValue = function () {
        $apiService.timestampInsertValue($scope.timestamp.value,$scope.timestamp.shift,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };
} ]);
