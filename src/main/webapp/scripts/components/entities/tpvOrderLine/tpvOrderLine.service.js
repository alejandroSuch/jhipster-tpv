'use strict';

angular.module('tpvApp')
    .factory('TpvOrderLine', function ($resource, DateUtils) {
        return $resource('api/tpvOrderLines/:id', {}, {
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
