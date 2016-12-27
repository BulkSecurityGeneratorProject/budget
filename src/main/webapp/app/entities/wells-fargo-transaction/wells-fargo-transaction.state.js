(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wells-fargo-transaction', {
            parent: 'entity',
            url: '/wells-fargo-transaction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellsFargoTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transactions.html',
                    controller: 'WellsFargoTransactionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('wells-fargo-transaction-detail', {
            parent: 'entity',
            url: '/wells-fargo-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WellsFargoTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transaction-detail.html',
                    controller: 'WellsFargoTransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WellsFargoTransaction', function($stateParams, WellsFargoTransaction) {
                    return WellsFargoTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wells-fargo-transaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wells-fargo-transaction-detail.edit', {
            parent: 'wells-fargo-transaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transaction-dialog.html',
                    controller: 'WellsFargoTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellsFargoTransaction', function(WellsFargoTransaction) {
                            return WellsFargoTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wells-fargo-transaction.new', {
            parent: 'wells-fargo-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transaction-dialog.html',
                    controller: 'WellsFargoTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                amount: null,
                                description: null,
                                budgeted: null,
                                accountType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wells-fargo-transaction', null, { reload: 'wells-fargo-transaction' });
                }, function() {
                    $state.go('wells-fargo-transaction');
                });
            }]
        })
        .state('wells-fargo-transaction.edit', {
            parent: 'wells-fargo-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transaction-dialog.html',
                    controller: 'WellsFargoTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WellsFargoTransaction', function(WellsFargoTransaction) {
                            return WellsFargoTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wells-fargo-transaction', null, { reload: 'wells-fargo-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wells-fargo-transaction.delete', {
            parent: 'wells-fargo-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wells-fargo-transaction/wells-fargo-transaction-delete-dialog.html',
                    controller: 'WellsFargoTransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WellsFargoTransaction', function(WellsFargoTransaction) {
                            return WellsFargoTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wells-fargo-transaction', null, { reload: 'wells-fargo-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
