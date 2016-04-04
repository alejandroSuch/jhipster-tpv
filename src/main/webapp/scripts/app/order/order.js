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
                },

            })
            .state('order.new', {
                url: '/new',
                onEnter: function (TpvOrder, $state) {
                    TpvOrder.save({id: null}, onSaveSuccess, onSaveError);

                    function onSaveSuccess(result) {
                        debugger;
                        $state.go('order.manage', {id: result.id});
                    };

                    function onSaveError() {
                        debugger;
                        alert('No se ha podido inicializar el pedido')
                    };
                }
            })
            .state('order.manage', {
                url: '/manage/:id',
                resolve: {
                    entity: ['$stateParams', 'TpvOrder', function ($stateParams, TpvOrder) {
                        return TpvOrder.get({id: $stateParams.id});
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
