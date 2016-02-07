'use strict';

angular.module('tpvApp')
    .controller('TpvOrderDetailController', function ($scope, $rootScope, $stateParams, entity, TpvOrder) {
        $scope.tpvOrder = entity;
        $scope.load = function (id) {
            TpvOrder.get({id: id}, function(result) {
                $scope.tpvOrder = result;
            });
        };
        var unsubscribe = $rootScope.$on('tpvApp:tpvOrderUpdate', function(event, result) {
            $scope.tpvOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
