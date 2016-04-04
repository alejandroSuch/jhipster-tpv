'use strict';

angular.module('tpvApp')
    .factory('OrderService', function ($resource) {
        return function(args) {
            return $resource('/api/tpvOrders/:orderId/product/:productId', args, {
                'add': {method: 'POST', isArray: false},
                'remove': {method: 'DELETE', isArray: false}
            });
        };
    });
