'use strict';

angular.module('tpvApp')
    .controller('VatDetailController', function ($scope, $rootScope, $stateParams, entity, Vat) {
        $scope.vat = entity;
        $scope.load = function (id) {
            Vat.get({id: id}, function(result) {
                $scope.vat = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:vatUpdate', function(event, result) {
            $scope.vat = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
