'use strict';

angular.module('tpvApp')
    .factory('TpvOrderSearch', function ($resource) {
        return $resource('api/_search/tpvOrders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
