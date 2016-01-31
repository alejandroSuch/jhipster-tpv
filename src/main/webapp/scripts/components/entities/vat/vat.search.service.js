'use strict';

angular.module('tpvApp')
    .factory('VatSearch', function ($resource) {
        return $resource('api/_search/vats/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
