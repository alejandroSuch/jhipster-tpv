'use strict';

angular.module('tpvApp')
    .factory('Vat', function ($resource, DateUtils) {
        return $resource('api/vats/:id', {}, {
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
