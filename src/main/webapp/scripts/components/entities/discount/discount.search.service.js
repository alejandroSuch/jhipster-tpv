'use strict';

angular.module('tpvApp')
    .factory('DiscountSearch', function ($resource) {
        return $resource('api/_search/discounts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
