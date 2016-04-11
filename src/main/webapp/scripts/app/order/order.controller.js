'use strict';

angular.module('tpvApp')
    .controller('OrderController', function ($scope, $state, order, lines, ProductSearchByEan, OrderService, $q) {
        var lastProduct = null;

        $scope.order = order;
        $scope.time = (lines == null || lines.length === 0) ? null : (new Date()).getTime();
        $scope.add = true;

        function addOrRemoveProduct(product) {
            lastProduct = product;

            var pathVariables = {
                orderId: order.id,
                productId: product.id
            };

            if ($scope.add) {
                return OrderService(pathVariables)
                    .add({})
                    .$promise;
            } else {
                return OrderService(pathVariables)
                    .remove({})
                    .$promise;
            }
        }

        function onSearchSuccess(product) {
            if (!product.id) {
                return $q.reject(null);
            }

            return addOrRemoveProduct(product);
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

        function onFinish() {
            $scope.searching = false;
            $scope.ean = null;

            setTimeout(function () {
                $('#ean').focus();
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
                    .finally(onFinish);
            }
        }

        $scope.repeatLastAction = function () {
            if(!lastProduct) {
                return;
            }

            addOrRemoveProduct(lastProduct)
                .then(onProductAddedToOrder)
                .catch(onSearchError)
                .finally(onFinish);
        };

        $scope.completeOrder = function () {
            OrderService({orderId: order.id})
                .nextState({})
                .$promise
                .then(function(result){
                    $state.go('tpvOrder');
                })
                .finally(onFinish);
        }

    });
