'use strict';

angular.module('tpvApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tpvOrder', {
                parent: 'entity',
                url: '/tpvOrders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvOrder/tpvOrders.html',
                        controller: 'TpvOrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tpvOrder.detail', {
                parent: 'entity',
                url: '/tpvOrder/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvOrder/tpvOrder-detail.html',
                        controller: 'TpvOrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvOrder');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TpvOrder', function($stateParams, TpvOrder) {
                        return TpvOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tpvOrder.new', {
                parent: 'tpvOrder',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrder/tpvOrder-dialog.html',
                        controller: 'TpvOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dateCreated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrder', null, { reload: true });
                    }, function() {
                        $state.go('tpvOrder');
                    })
                }]
            })
            .state('tpvOrder.edit', {
                parent: 'tpvOrder',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrder/tpvOrder-dialog.html',
                        controller: 'TpvOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TpvOrder', function(TpvOrder) {
                                return TpvOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tpvOrder.delete', {
                parent: 'tpvOrder',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrder/tpvOrder-delete-dialog.html',
                        controller: 'TpvOrderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TpvOrder', function(TpvOrder) {
                                return TpvOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
