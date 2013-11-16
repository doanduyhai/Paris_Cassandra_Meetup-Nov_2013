'use strict';

demoModule.controller('WriteBarrierCtrl',  [ '$scope', 'apiService',function($scope,$apiService) {
    $scope.success = false;
    $scope.error = false;
    $scope.writebarrier = {
        shift: 10,
        value: "value1"
    };

    $scope.delete = function () {
        $apiService.writeBarrierDelete($scope.writebarrier.shift,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };

    $scope.insertValue = function () {
        $apiService.writeBarrierInsertValue($scope.writebarrier.value,
            $apiService.createSuccess($scope),
            $apiService.createError($scope));
    };
} ]);