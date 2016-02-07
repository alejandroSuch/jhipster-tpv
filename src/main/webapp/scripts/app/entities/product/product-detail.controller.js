'use strict';

angular.module('tpvApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, Price, Category, Discount) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
