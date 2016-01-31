'use strict';

angular.module('tpvApp')
    .controller('VatController', function ($scope, $state, Vat, VatSearch) {

        $scope.vats = [];
        $scope.loadAll = function() {
            Vat.query(function(result) {
               $scope.vats = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            VatSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.vats = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.vat = {
                code: null,
                description: null,
                value: null,
                id: null
            };
        };
    });
