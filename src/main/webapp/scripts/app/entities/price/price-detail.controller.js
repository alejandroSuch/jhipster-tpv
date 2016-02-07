'use strict';

angular.module('tpvApp')
    .controller('PriceDetailController', function ($scope, $rootScope, $stateParams, entity, Price) {
        $scope.price = entity;
        $scope.load = function (id) {
            Price.get({id: id}, function(result) {
                $scope.price = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:priceUpdate', function(event, result) {
            $scope.price = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
