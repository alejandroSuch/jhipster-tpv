'use strict';

angular.module('tpvApp')
    .factory('TpvOrder', function ($resource, DateUtils) {
        return $resource('api/tpvOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
