'use strict';

angular.module('tpvApp')
    .controller('DiscountController', function ($scope, $state, Discount, DiscountSearch) {

        $scope.discounts = [];
        $scope.loadAll = function() {
            Discount.query(function(result) {
               $scope.discounts = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            DiscountSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.discounts = result;
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
            $scope.discount = {
                code: null,
                description: null,
                value: null,
                units: null,
                activeFrom: null,
                activeTo: null,
                id: null
            };
        };
    });
