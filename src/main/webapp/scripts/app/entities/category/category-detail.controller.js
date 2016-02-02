'use strict';

angular.module('tpvApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Vat) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
