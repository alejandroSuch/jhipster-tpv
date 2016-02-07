'use strict';

angular.module('tpvApp')
	.controller('TpvOrderLineDeleteController', function($scope, $uibModalInstance, entity, TpvOrderLine) {

        $scope.tpvOrderLine = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TpvOrderLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
