(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('amex-transactionmySuffix', {
            parent: 'entity',
            url: '/amex-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AmexTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amex-transaction/amex-transactionsmySuffix.html',
                    controller: 'AmexTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('amex-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/amex-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AmexTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amex-transaction/amex-transactionmySuffix-detail.html',
                    controller: 'AmexTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AmexTransaction', function($stateParams, AmexTransaction) {
                    return AmexTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'amex-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('amex-transactionmySuffix-detail.edit', {
            parent: 'amex-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amex-transaction/amex-transactionmySuffix-dialog.html',
                    controller: 'AmexTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AmexTransaction', function(AmexTransaction) {
                            return AmexTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('amex-transactionmySuffix.new', {
            parent: 'amex-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amex-transaction/amex-transactionmySuffix-dialog.html',
                    controller: 'AmexTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                description: null,
                                person: null,
                                amount: null,
                                referenceId: null,
                                budgeted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('amex-transactionmySuffix', null, { reload: 'amex-transactionmySuffix' });
                }, function() {
                    $state.go('amex-transactionmySuffix');
                });
            }]
        })
        .state('amex-transactionmySuffix.edit', {
            parent: 'amex-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amex-transaction/amex-transactionmySuffix-dialog.html',
                    controller: 'AmexTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AmexTransaction', function(AmexTransaction) {
                            return AmexTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('amex-transactionmySuffix', null, { reload: 'amex-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('amex-transactionmySuffix.delete', {
            parent: 'amex-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/amex-transaction/amex-transactionmySuffix-delete-dialog.html',
                    controller: 'AmexTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AmexTransaction', function(AmexTransaction) {
                            return AmexTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('amex-transactionmySuffix', null, { reload: 'amex-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
