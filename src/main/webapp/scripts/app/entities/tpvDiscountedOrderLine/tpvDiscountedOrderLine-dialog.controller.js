'use strict';

angular.module('tpvApp').controller('TpvDiscountedOrderLineDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TpvDiscountedOrderLine', 'Discount',
        function($scope, $stateParams, $uibModalInstance, entity, TpvDiscountedOrderLine, Discount) {

        $scope.tpvDiscountedOrderLine = entity;
        $scope.discounts = Discount.query();
        $scope.load = function(id) {
            TpvDiscountedOrderLine.get({id : id}, function(result) {
                $scope.tpvDiscountedOrderLine = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:tpvDiscountedOrderLineUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tpvDiscountedOrderLine.id != null) {
                TpvDiscountedOrderLine.update($scope.tpvDiscountedOrderLine, onSaveSuccess, onSaveError);
            } else {
                TpvDiscountedOrderLine.save($scope.tpvDiscountedOrderLine, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
