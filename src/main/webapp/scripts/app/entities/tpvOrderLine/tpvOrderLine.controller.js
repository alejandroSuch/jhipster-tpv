'use strict';

angular.module('tpvApp')
    .controller('TpvOrderLineController', function ($scope, $state, TpvOrderLine, TpvOrderLineSearch, ParseLinks) {

        $scope.tpvOrderLines = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            TpvOrderLine.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.tpvOrderLines = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TpvOrderLineSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.tpvOrderLines = result;
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
            $scope.tpvOrderLine = {
                lineNumber: null,
                qty: null,
                id: null
            };
        };
    });
