'use strict';

angular.module('tpvApp')
    .controller('OrderController', function ($scope, $state, entity, TpvOrder, ProductSearchByEan, OrderService) {
        $scope.order = entity;

        function onSearchSuccess(product){
            if(!product.id) {
                onSearchError();
                return;
            }

            var pathVariables = {
                orderId: entity.id,
                productId: product.id
            };

            return OrderService(pathVariables)
                .add({})
                .$promise;
        };

        function onProductAddedToOrder(order) {
            $scope.order = order;
            debugger;
        }

        function onSearchError(){
            $scope.$emit('tpvApp.httpError', {
                data: {
                    message: 'Product not added to cart'
                }
            });
        };

        function onSearchFinish(){
            $scope.searching = false;
            $scope.ean = null;
        };



        $scope.search = function(ean) {
            if(!!ean && ean.length == 13) {
                $scope.searching = true;

                ProductSearchByEan
                    .get({ean:ean})
                    .$promise
                    .then(onSearchSuccess)
                    .then(onProductAddedToOrder)
                    .catch(onSearchError)
                    .finally(onSearchFinish);
            }
        }


    });
