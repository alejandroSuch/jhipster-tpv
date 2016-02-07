'use strict';

angular.module('tpvApp').controller('PriceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Price',
        function($scope, $stateParams, $uibModalInstance, entity, Price) {

        $scope.price = entity;
        $scope.load = function(id) {
            Price.get({id : id}, function(result) {
                $scope.price = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:priceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.price.id != null) {
                Price.update($scope.price, onSaveSuccess, onSaveError);
            } else {
                Price.save($scope.price, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
