'use strict';

angular.module('tpvApp').controller('DiscountDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Discount',
        function($scope, $stateParams, $uibModalInstance, entity, Discount) {

        $scope.discount = entity;
        $scope.load = function(id) {
            Discount.get({id : id}, function(result) {
                $scope.discount = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:discountUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.discount.id != null) {
                Discount.update($scope.discount, onSaveSuccess, onSaveError);
            } else {
                Discount.save($scope.discount, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForActiveFrom = {};

        $scope.datePickerForActiveFrom.status = {
            opened: false
        };

        $scope.datePickerForActiveFromOpen = function($event) {
            $scope.datePickerForActiveFrom.status.opened = true;
        };
        $scope.datePickerForActiveTo = {};

        $scope.datePickerForActiveTo.status = {
            opened: false
        };

        $scope.datePickerForActiveToOpen = function($event) {
            $scope.datePickerForActiveTo.status.opened = true;
        };
}]);
