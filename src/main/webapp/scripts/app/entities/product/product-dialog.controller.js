'use strict';

angular.module('tpvApp').controller('ProductDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Product', 'Price', 'Category', 'Discount',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Product, Price, Category, Discount) {

        $scope.product = entity;
        $scope.prices = Price.query({filter: 'product-is-null'});
        $q.all([$scope.product.$promise, $scope.prices.$promise]).then(function() {
            if (!$scope.product.price || !$scope.product.price.id) {
                return $q.reject();
            }
            return Price.get({id : $scope.product.price.id}).$promise;
        }).then(function(price) {
            $scope.prices.push(price);
        });
        $scope.categories = Category.query();
        $scope.discounts = Discount.query();
        $scope.load = function(id) {
            Product.get({id : id}, function(result) {
                $scope.product = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tpvApp:productUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.product.id != null) {
                Product.update($scope.product, onSaveSuccess, onSaveError);
            } else {
                Product.save($scope.product, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
