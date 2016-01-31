'use strict';

angular.module('tpvApp')
	.controller('VatDeleteController', function($scope, $uibModalInstance, entity, Vat) {

        $scope.vat = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Vat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
