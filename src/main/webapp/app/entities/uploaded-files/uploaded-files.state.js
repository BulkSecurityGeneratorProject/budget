(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uploaded-files', {
            parent: 'entity',
            url: '/uploaded-files?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UploadedFiles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/uploaded-files/uploaded-files.html',
                    controller: 'UploadedFilesController',
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
                }],
            }
        })
        .state('uploaded-files-detail', {
            parent: 'entity',
            url: '/uploaded-files/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UploadedFiles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/uploaded-files/uploaded-files-detail.html',
                    controller: 'UploadedFilesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UploadedFiles', function($stateParams, UploadedFiles) {
                    return UploadedFiles.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'uploaded-files',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('uploaded-files-detail.edit', {
            parent: 'uploaded-files-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uploaded-files/uploaded-files-dialog.html',
                    controller: 'UploadedFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UploadedFiles', function(UploadedFiles) {
                            return UploadedFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('uploaded-files.new', {
            parent: 'uploaded-files',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uploaded-files/uploaded-files-dialog.html',
                    controller: 'UploadedFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                uploadDate: null,
                                bank: null,
                                file: null,
                                fileContentType: null,
                                accountType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uploaded-files', null, { reload: 'uploaded-files' });
                }, function() {
                    $state.go('uploaded-files');
                });
            }]
        })
        .state('uploaded-files.edit', {
            parent: 'uploaded-files',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uploaded-files/uploaded-files-dialog.html',
                    controller: 'UploadedFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UploadedFiles', function(UploadedFiles) {
                            return UploadedFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('uploaded-files', null, { reload: 'uploaded-files' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('uploaded-files.delete', {
            parent: 'uploaded-files',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/uploaded-files/uploaded-files-delete-dialog.html',
                    controller: 'UploadedFilesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UploadedFiles', function(UploadedFiles) {
                            return UploadedFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('uploaded-files', null, { reload: 'uploaded-files' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
