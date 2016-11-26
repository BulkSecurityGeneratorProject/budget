(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bill-transactionmySuffix', {
            parent: 'entity',
            url: '/bill-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BillTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill-transaction/bill-transactionsmySuffix.html',
                    controller: 'BillTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bill-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/bill-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BillTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill-transaction/bill-transactionmySuffix-detail.html',
                    controller: 'BillTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BillTransaction', function($stateParams, BillTransaction) {
                    return BillTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bill-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bill-transactionmySuffix-detail.edit', {
            parent: 'bill-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-transaction/bill-transactionmySuffix-dialog.html',
                    controller: 'BillTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillTransaction', function(BillTransaction) {
                            return BillTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill-transactionmySuffix.new', {
            parent: 'bill-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-transaction/bill-transactionmySuffix-dialog.html',
                    controller: 'BillTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                dayOut: null,
                                fromAccount: null,
                                automatic: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bill-transactionmySuffix', null, { reload: 'bill-transactionmySuffix' });
                }, function() {
                    $state.go('bill-transactionmySuffix');
                });
            }]
        })
        .state('bill-transactionmySuffix.edit', {
            parent: 'bill-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-transaction/bill-transactionmySuffix-dialog.html',
                    controller: 'BillTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BillTransaction', function(BillTransaction) {
                            return BillTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill-transactionmySuffix', null, { reload: 'bill-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill-transactionmySuffix.delete', {
            parent: 'bill-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill-transaction/bill-transactionmySuffix-delete-dialog.html',
                    controller: 'BillTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BillTransaction', function(BillTransaction) {
                            return BillTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill-transactionmySuffix', null, { reload: 'bill-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
