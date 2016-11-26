(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bill-typemySuffix', {
            parent: 'entity',
            url: '/bill-typemySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BillTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill-type/bill-typesmySuffix.html',
                    controller: 'BillTypeMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bill-typemySuffix-detail', {
            parent: 'entity',
            url: '/bill-typemySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BillType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill-type/bill-typemySuffix-detail.html',
                    controller: 'BillTypeMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BillType', function($stateParams, BillType) {
                    return BillType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bill-typemySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bill-typemySuffix-detail.edit', {
            parent: 'bill-typemySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-type/bill-typemySuffix-dialog.html',
                    controller: 'BillTypeMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillType', function(BillType) {
                            return BillType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill-typemySuffix.new', {
            parent: 'bill-typemySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-type/bill-typemySuffix-dialog.html',
                    controller: 'BillTypeMySuffixDialogController',
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
                    $state.go('bill-typemySuffix', null, { reload: 'bill-typemySuffix' });
                }, function() {
                    $state.go('bill-typemySuffix');
                });
            }]
        })
        .state('bill-typemySuffix.edit', {
            parent: 'bill-typemySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-type/bill-typemySuffix-dialog.html',
                    controller: 'BillTypeMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillType', function(BillType) {
                            return BillType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill-typemySuffix', null, { reload: 'bill-typemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill-typemySuffix.delete', {
            parent: 'bill-typemySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-type/bill-typemySuffix-delete-dialog.html',
                    controller: 'BillTypeMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BillType', function(BillType) {
                            return BillType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill-typemySuffix', null, { reload: 'bill-typemySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
