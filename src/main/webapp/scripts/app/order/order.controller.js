'use strict';

angular.module('tpvApp')
    .controller('OrderController', function ($scope, order, lines, ProductSearchByEan, OrderService) {
        $scope.order = order;
        $scope.time = (lines == null || lines.length === 0) ? null : (new Date()).getTime();

        function onSearchSuccess(product) {
            if (!product.id) {
                onSearchError();
                return;
            }

            var pathVariables = {
                orderId: order.id,
                productId: product.id
            };

            return OrderService(pathVariables)
                .add({})
                .$promise;
        };

        function onProductAddedToOrder(order) {
            $scope.order = order;
            $scope.time = (new Date()).getTime();
        }

        function onSearchError() {
            $scope.$emit('tpvApp.httpError', {
                data: {
                    message: 'Product not added to cart'
                }
            });
        };

        function onSearchFinish() {
            $scope.searching = false;
            $scope.ean = null;
            
            setTimeout(function(){
                $('#ean').focus();console.log('!!!');
            });
        };


        $scope.search = function (ean) {
            if (!!ean && ean.length == 13) {
                $scope.searching = true;

                ProductSearchByEan
                    .get({ean: ean})
                    .$promise
                    .then(onSearchSuccess)
                    .then(onProductAddedToOrder)
                    .catch(onSearchError)
                    .finally(onSearchFinish);
            }
        }


    });
