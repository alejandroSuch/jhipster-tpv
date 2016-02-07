'use strict';

angular.module('tpvApp')
    .factory('TpvDiscountedOrderLine', function ($resource, DateUtils) {
        return $resource('api/tpvDiscountedOrderLines/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
