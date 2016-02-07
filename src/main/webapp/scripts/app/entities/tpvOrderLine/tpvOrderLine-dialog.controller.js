'use strict';

angular.module('tpvApp').controller('TpvOrderLineDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TpvOrderLine', 'TpvOrder', 'Product', 'Price', 'Vat',
        function($scope, $stateParams, $uibModalInstance, entity, TpvOrderLine, TpvOrder, Product, Price, Vat) {

        $scope.tpvOrderLine = entity;
        $scope.tpvorders = TpvOrder.query();
        $scope.products = Product.query();
        $scope.prices = Price.query();
        $scope.vats = Vat.query();
        $scope.load = function(id) {
            TpvOrderLine.get({id : id}, function(result) {
                $scope.tpvOrderLine = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:tpvOrderLineUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tpvOrderLine.id != null) {
                TpvOrderLine.update($scope.tpvOrderLine, onSaveSuccess, onSaveError);
            } else {
                TpvOrderLine.save($scope.tpvOrderLine, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
