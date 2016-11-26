(function() {
    'use strict';

    angular
        .module('budgetApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('upload-transactions', {
            parent: 'app',
            url: '/upload',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/uploadTransactions/upload-transactions.html',
                    controller: 'UploadTransactionController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
