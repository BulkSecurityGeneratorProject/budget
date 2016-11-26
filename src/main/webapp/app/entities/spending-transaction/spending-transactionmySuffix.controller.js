(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTransactionMySuffixController', SpendingTransactionMySuffixController);

    SpendingTransactionMySuffixController.$inject = ['$scope', '$state', 'SpendingTransaction', 'SpendingTransactionSearch'];

    function SpendingTransactionMySuffixController ($scope, $state, SpendingTransaction, SpendingTransactionSearch) {
        var vm = this;
        
        vm.spendingTransactions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SpendingTransaction.query(function(result) {
                vm.spendingTransactions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SpendingTransactionSearch.query({query: vm.searchQuery}, function(result) {
                vm.spendingTransactions = result;
            });
        }    }
})();
