'use strict';

angular.module('tpvApp')
    .factory('Discount', function ($resource, DateUtils) {
        return $resource('api/discounts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.activeFrom = DateUtils.convertLocaleDateFromServer(data.activeFrom);
                    data.activeTo = DateUtils.convertLocaleDateFromServer(data.activeTo);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.activeFrom = DateUtils.convertLocaleDateToServer(data.activeFrom);
                    data.activeTo = DateUtils.convertLocaleDateToServer(data.activeTo);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.activeFrom = DateUtils.convertLocaleDateToServer(data.activeFrom);
                    data.activeTo = DateUtils.convertLocaleDateToServer(data.activeTo);
                    return angular.toJson(data);
                }
            }
        });
    });
