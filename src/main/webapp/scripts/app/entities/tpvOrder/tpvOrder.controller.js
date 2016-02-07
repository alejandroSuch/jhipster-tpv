'use strict';

angular.module('tpvApp')
    .controller('TpvOrderController', function ($scope, $state, TpvOrder, TpvOrderSearch, ParseLinks) {

        $scope.tpvOrders = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            TpvOrder.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.tpvOrders = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TpvOrderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tpvOrders = result;
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
            $scope.tpvOrder = {
                dateCreated: null,
                id: null
            };
        };
    });
