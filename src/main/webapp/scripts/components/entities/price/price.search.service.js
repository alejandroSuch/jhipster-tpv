'use strict';

angular.module('tpvApp')
    .factory('PriceSearch', function ($resource) {
        return $resource('api/_search/prices/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
