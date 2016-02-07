'use strict';

angular.module('tpvApp')
    .controller('TpvDiscountedOrderLineDetailController', function ($scope, $rootScope, $stateParams, entity, TpvDiscountedOrderLine, Discount) {
        $scope.tpvDiscountedOrderLine = entity;
        $scope.load = function (id) {
            TpvDiscountedOrderLine.get({id: id}, function(result) {
                $scope.tpvDiscountedOrderLine = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:tpvDiscountedOrderLineUpdate', function(event, result) {
            $scope.tpvDiscountedOrderLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
