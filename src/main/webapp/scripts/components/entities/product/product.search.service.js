'use strict';

angular.module('tpvApp')
    .factory('ProductSearch', function ($resource) {
        return $resource('api/_search/products/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    })
    .factory('ProductSearchByEan', function($resource){
        return $resource('api/products/ean/:ean', {}, {
            'get': { method: 'GET', isArray: false}
        });
    });
