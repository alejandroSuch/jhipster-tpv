'use strict';

angular.module('tpvApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('vat', {
                parent: 'entity',
                url: '/vats',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.vat.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/vat/vats.html',
                        controller: 'VatController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('vat');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('vat.detail', {
                parent: 'entity',
                url: '/vat/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tpvApp.vat.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/vat/vat-detail.html',
                        controller: 'VatDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('vat');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Vat', function($stateParams, Vat) {
                        return Vat.get({id : $stateParams.id});
                    }]
                }
            })
            .state('vat.new', {
                parent: 'vat',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/vat/vat-dialog.html',
                        controller: 'VatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    description: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('vat', null, { reload: true });
                    }, function() {
                        $state.go('vat');
                    })
                }]
            })
            .state('vat.edit', {
                parent: 'vat',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/vat/vat-dialog.html',
                        controller: 'VatDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Vat', function(Vat) {
                                return Vat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('vat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('vat.delete', {
                parent: 'vat',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/vat/vat-delete-dialog.html',
                        controller: 'VatDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Vat', function(Vat) {
                                return Vat.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('vat', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
