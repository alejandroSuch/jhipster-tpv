'use strict';

angular.module('tpvApp')
	.controller('TpvOrderDeleteController', function($scope, $uibModalInstance, entity, TpvOrder) {

        $scope.tpvOrder = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TpvOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
