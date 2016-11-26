(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('spending-transactionmySuffix', {
            parent: 'entity',
            url: '/spending-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SpendingTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spending-transaction/spending-transactionsmySuffix.html',
                    controller: 'SpendingTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('spending-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/spending-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SpendingTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/spending-transaction/spending-transactionmySuffix-detail.html',
                    controller: 'SpendingTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SpendingTransaction', function($stateParams, SpendingTransaction) {
                    return SpendingTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'spending-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('spending-transactionmySuffix-detail.edit', {
            parent: 'spending-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-transaction/spending-transactionmySuffix-dialog.html',
                    controller: 'SpendingTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpendingTransaction', function(SpendingTransaction) {
                            return SpendingTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spending-transactionmySuffix.new', {
            parent: 'spending-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-transaction/spending-transactionmySuffix-dialog.html',
                    controller: 'SpendingTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('spending-transactionmySuffix', null, { reload: 'spending-transactionmySuffix' });
                }, function() {
                    $state.go('spending-transactionmySuffix');
                });
            }]
        })
        .state('spending-transactionmySuffix.edit', {
            parent: 'spending-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-transaction/spending-transactionmySuffix-dialog.html',
                    controller: 'SpendingTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpendingTransaction', function(SpendingTransaction) {
                            return SpendingTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spending-transactionmySuffix', null, { reload: 'spending-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('spending-transactionmySuffix.delete', {
            parent: 'spending-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/spending-transaction/spending-transactionmySuffix-delete-dialog.html',
                    controller: 'SpendingTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SpendingTransaction', function(SpendingTransaction) {
                            return SpendingTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('spending-transactionmySuffix', null, { reload: 'spending-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
