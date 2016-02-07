'use strict';

angular.module('tpvApp')
    .factory('TpvDiscountedOrderLineSearch', function ($resource) {
        return $resource('api/_search/tpvDiscountedOrderLines/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
