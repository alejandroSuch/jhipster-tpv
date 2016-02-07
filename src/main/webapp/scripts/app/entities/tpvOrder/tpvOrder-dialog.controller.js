'use strict';

angular.module('tpvApp').controller('TpvOrderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TpvOrder',
        function($scope, $stateParams, $uibModalInstance, entity, TpvOrder) {

        $scope.tpvOrder = entity;
        $scope.load = function(id) {
            TpvOrder.get({id : id}, function(result) {
                $scope.tpvOrder = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:tpvOrderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.tpvOrder.id != null) {
                TpvOrder.update($scope.tpvOrder, onSaveSuccess, onSaveError);
            } else {
                TpvOrder.save($scope.tpvOrder, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDateCreated = {};

        $scope.datePickerForDateCreated.status = {
            opened: false
        };

        $scope.datePickerForDateCreatedOpen = function($event) {
            $scope.datePickerForDateCreated.status.opened = true;
        };
}]);
