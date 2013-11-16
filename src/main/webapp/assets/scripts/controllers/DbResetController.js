demoModule.controller('DbResetCtrl', [ '$scope','$http',function($scope,$http) {
    $scope.success = false;
    $scope.error = false;

    $scope.resetDb = function() {
        $http.delete("db")
            .success(function(data){
                $scope.success = true;
                $scope.error = false;
                $scope.successMessage=data;
            })
            .error(function(data){
                $scope.success = false;
                $scope.error = true;
                $scope.errorMessage=data;
            });
    };

    $scope.closeDbPanel = function() {
        console.log("closeDbPanel called")
        $scope.success = false;
        $scope.error = false;
    }
}]);
