'use strict';

angular.module('tpvApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tpvDiscountedOrderLine', {
                parent: 'entity',
                url: '/tpvDiscountedOrderLines',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvDiscountedOrderLine.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvDiscountedOrderLine/tpvDiscountedOrderLines.html',
                        controller: 'TpvDiscountedOrderLineController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvDiscountedOrderLine');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tpvDiscountedOrderLine.detail', {
                parent: 'entity',
                url: '/tpvDiscountedOrderLine/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvDiscountedOrderLine.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvDiscountedOrderLine/tpvDiscountedOrderLine-detail.html',
                        controller: 'TpvDiscountedOrderLineDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvDiscountedOrderLine');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TpvDiscountedOrderLine', function($stateParams, TpvDiscountedOrderLine) {
                        return TpvDiscountedOrderLine.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tpvDiscountedOrderLine.new', {
                parent: 'tpvDiscountedOrderLine',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvDiscountedOrderLine/tpvDiscountedOrderLine-dialog.html',
                        controller: 'TpvDiscountedOrderLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tpvDiscountedOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('tpvDiscountedOrderLine');
                    })
                }]
            })
            .state('tpvDiscountedOrderLine.edit', {
                parent: 'tpvDiscountedOrderLine',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvDiscountedOrderLine/tpvDiscountedOrderLine-dialog.html',
                        controller: 'TpvDiscountedOrderLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TpvDiscountedOrderLine', function(TpvDiscountedOrderLine) {
                                return TpvDiscountedOrderLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvDiscountedOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tpvDiscountedOrderLine.delete', {
                parent: 'tpvDiscountedOrderLine',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvDiscountedOrderLine/tpvDiscountedOrderLine-delete-dialog.html',
                        controller: 'TpvDiscountedOrderLineDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TpvDiscountedOrderLine', function(TpvDiscountedOrderLine) {
                                return TpvDiscountedOrderLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvDiscountedOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
