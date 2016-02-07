'use strict';

angular.module('tpvApp')
    .controller('CategoryController', function ($scope, $state, Category, CategorySearch) {

        $scope.categories = [];
        $scope.loadAll = function() {
            Category.query(function(result) {
               $scope.categories = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categories = result;
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
            $scope.category = {
                name: null,
                description: null,
                id: null
            };
        };
    });
