'use strict';

angular.module('tpvApp').controller('VatDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vat',
        function($scope, $stateParams, $uibModalInstance, entity, Vat) {

        $scope.vat = entity;
        $scope.load = function(id) {
            Vat.get({id : id}, function(result) {
                $scope.vat = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:vatUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.vat.id != null) {
                Vat.update($scope.vat, onSaveSuccess, onSaveError);
            } else {
                Vat.save($scope.vat, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
