(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sinking-fund-transactionmySuffix', {
            parent: 'entity',
            url: '/sinking-fund-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SinkingFundTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionsmySuffix.html',
                    controller: 'SinkingFundTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('sinking-fund-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/sinking-fund-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SinkingFundTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionmySuffix-detail.html',
                    controller: 'SinkingFundTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SinkingFundTransaction', function($stateParams, SinkingFundTransaction) {
                    return SinkingFundTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sinking-fund-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sinking-fund-transactionmySuffix-detail.edit', {
            parent: 'sinking-fund-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionmySuffix-dialog.html',
                    controller: 'SinkingFundTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SinkingFundTransaction', function(SinkingFundTransaction) {
                            return SinkingFundTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sinking-fund-transactionmySuffix.new', {
            parent: 'sinking-fund-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionmySuffix-dialog.html',
                    controller: 'SinkingFundTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                type: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sinking-fund-transactionmySuffix', null, { reload: 'sinking-fund-transactionmySuffix' });
                }, function() {
                    $state.go('sinking-fund-transactionmySuffix');
                });
            }]
        })
        .state('sinking-fund-transactionmySuffix.edit', {
            parent: 'sinking-fund-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionmySuffix-dialog.html',
                    controller: 'SinkingFundTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SinkingFundTransaction', function(SinkingFundTransaction) {
                            return SinkingFundTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sinking-fund-transactionmySuffix', null, { reload: 'sinking-fund-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sinking-fund-transactionmySuffix.delete', {
            parent: 'sinking-fund-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sinking-fund-transaction/sinking-fund-transactionmySuffix-delete-dialog.html',
                    controller: 'SinkingFundTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SinkingFundTransaction', function(SinkingFundTransaction) {
                            return SinkingFundTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sinking-fund-transactionmySuffix', null, { reload: 'sinking-fund-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
