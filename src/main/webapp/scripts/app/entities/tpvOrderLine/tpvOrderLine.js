'use strict';

angular.module('tpvApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tpvOrderLine', {
                parent: 'entity',
                url: '/tpvOrderLines',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvOrderLine.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvOrderLine/tpvOrderLines.html',
                        controller: 'TpvOrderLineController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvOrderLine');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tpvOrderLine.detail', {
                parent: 'entity',
                url: '/tpvOrderLine/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.tpvOrderLine.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tpvOrderLine/tpvOrderLine-detail.html',
                        controller: 'TpvOrderLineDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tpvOrderLine');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TpvOrderLine', function($stateParams, TpvOrderLine) {
                        return TpvOrderLine.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tpvOrderLine.new', {
                parent: 'tpvOrderLine',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrderLine/tpvOrderLine-dialog.html',
                        controller: 'TpvOrderLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    lineNumber: null,
                                    qty: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('tpvOrderLine');
                    })
                }]
            })
            .state('tpvOrderLine.edit', {
                parent: 'tpvOrderLine',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrderLine/tpvOrderLine-dialog.html',
                        controller: 'TpvOrderLineDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TpvOrderLine', function(TpvOrderLine) {
                                return TpvOrderLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tpvOrderLine.delete', {
                parent: 'tpvOrderLine',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tpvOrderLine/tpvOrderLine-delete-dialog.html',
                        controller: 'TpvOrderLineDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TpvOrderLine', function(TpvOrderLine) {
                                return TpvOrderLine.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tpvOrderLine', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
