'use strict';

angular.module('tpvApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


