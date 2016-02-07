'use strict';

angular.module('tpvApp')
    .controller('TpvOrderLineDetailController', function ($scope, $rootScope, $stateParams, entity, TpvOrderLine, TpvOrder, Product, Price, Vat) {
        $scope.tpvOrderLine = entity;
        $scope.load = function (id) {
            TpvOrderLine.get({id: id}, function(result) {
                $scope.tpvOrderLine = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:tpvOrderLineUpdate', function(event, result) {
            $scope.tpvOrderLine = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
