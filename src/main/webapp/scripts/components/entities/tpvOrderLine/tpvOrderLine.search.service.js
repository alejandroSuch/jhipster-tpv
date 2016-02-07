'use strict';

angular.module('tpvApp')
    .factory('TpvOrderLineSearch', function ($resource) {
        return $resource('api/_search/tpvOrderLines/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
