'use strict';

angular.module('tpvApp')
	.controller('TpvDiscountedOrderLineDeleteController', function($scope, $uibModalInstance, entity, TpvDiscountedOrderLine) {

        $scope.tpvDiscountedOrderLine = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TpvDiscountedOrderLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
