'use strict';

angular.module('tpvApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('order', {
                url: '/order',
                abstract: true,
                parent: 'entity',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Order'
                }
            })
            .state('order.new', {
                url: '/new',
                onEnter: function (TpvOrder, $state) {
                    TpvOrder.new({}, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        $state.go('order.manage', {id: result.id});
                    }

                    function onSaveError() {
                        alert('No se ha podido inicializar el pedido')
                    }
                }
            })
            .state('order.manage', {
                url: '/manage/:id',
                resolve: {
                    order: ['$stateParams', 'TpvOrder', function ($stateParams, TpvOrder) {
                        return TpvOrder.get({id: $stateParams.id}).$promise;
                    }],
                    lines: ['$stateParams', 'TpvOrder', function ($stateParams, TpvOrder) {
                        return TpvOrder.lines({id: $stateParams.id}).$promise;
                    }]
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/order/order.html',
                        controller: 'OrderController'
                    }
                }
            });
    });
