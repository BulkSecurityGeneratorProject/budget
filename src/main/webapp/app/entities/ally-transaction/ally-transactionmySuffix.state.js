(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ally-transactionmySuffix', {
            parent: 'entity',
            url: '/ally-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AllyTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ally-transaction/ally-transactionsmySuffix.html',
                    controller: 'AllyTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('ally-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/ally-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AllyTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ally-transaction/ally-transactionmySuffix-detail.html',
                    controller: 'AllyTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AllyTransaction', function($stateParams, AllyTransaction) {
                    return AllyTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ally-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ally-transactionmySuffix-detail.edit', {
            parent: 'ally-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ally-transaction/ally-transactionmySuffix-dialog.html',
                    controller: 'AllyTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AllyTransaction', function(AllyTransaction) {
                            return AllyTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ally-transactionmySuffix.new', {
            parent: 'ally-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ally-transaction/ally-transactionmySuffix-dialog.html',
                    controller: 'AllyTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                transactionType: null,
                                description: null,
                                budgeted: null,
                                accountType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ally-transactionmySuffix', null, { reload: 'ally-transactionmySuffix' });
                }, function() {
                    $state.go('ally-transactionmySuffix');
                });
            }]
        })
        .state('ally-transactionmySuffix.edit', {
            parent: 'ally-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ally-transaction/ally-transactionmySuffix-dialog.html',
                    controller: 'AllyTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AllyTransaction', function(AllyTransaction) {
                            return AllyTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ally-transactionmySuffix', null, { reload: 'ally-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ally-transactionmySuffix.delete', {
            parent: 'ally-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ally-transaction/ally-transactionmySuffix-delete-dialog.html',
                    controller: 'AllyTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AllyTransaction', function(AllyTransaction) {
                            return AllyTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ally-transactionmySuffix', null, { reload: 'ally-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
