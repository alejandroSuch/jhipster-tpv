'use strict';

angular.module('tpvApp')
    .controller('TpvDiscountedOrderLineController', function ($scope, $state, TpvDiscountedOrderLine, TpvDiscountedOrderLineSearch, ParseLinks) {

        $scope.tpvDiscountedOrderLines = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            TpvDiscountedOrderLine.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.tpvDiscountedOrderLines.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.tpvDiscountedOrderLines = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TpvDiscountedOrderLineSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tpvDiscountedOrderLines = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tpvDiscountedOrderLine = {
                id: null
            };
        };
    });
