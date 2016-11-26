(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('spending-typemySuffix', {
            parent: 'entity',
            url: '/spending-typemySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SpendingTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spending-type/spending-typesmySuffix.html',
                    controller: 'SpendingTypeMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('spending-typemySuffix-detail', {
            parent: 'entity',
            url: '/spending-typemySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SpendingType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spending-type/spending-typemySuffix-detail.html',
                    controller: 'SpendingTypeMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SpendingType', function($stateParams, SpendingType) {
                    return SpendingType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'spending-typemySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('spending-typemySuffix-detail.edit', {
            parent: 'spending-typemySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-type/spending-typemySuffix-dialog.html',
                    controller: 'SpendingTypeMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpendingType', function(SpendingType) {
                            return SpendingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spending-typemySuffix.new', {
            parent: 'spending-typemySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-type/spending-typemySuffix-dialog.html',
                    controller: 'SpendingTypeMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                mainType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('spending-typemySuffix', null, { reload: 'spending-typemySuffix' });
                }, function() {
                    $state.go('spending-typemySuffix');
                });
            }]
        })
        .state('spending-typemySuffix.edit', {
            parent: 'spending-typemySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-type/spending-typemySuffix-dialog.html',
                    controller: 'SpendingTypeMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpendingType', function(SpendingType) {
                            return SpendingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spending-typemySuffix', null, { reload: 'spending-typemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spending-typemySuffix.delete', {
            parent: 'spending-typemySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-type/spending-typemySuffix-delete-dialog.html',
                    controller: 'SpendingTypeMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SpendingType', function(SpendingType) {
                            return SpendingType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spending-typemySuffix', null, { reload: 'spending-typemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
