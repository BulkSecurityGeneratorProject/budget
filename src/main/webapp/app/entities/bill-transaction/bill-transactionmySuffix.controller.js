(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTransactionMySuffixController', BillTransactionMySuffixController);

    BillTransactionMySuffixController.$inject = ['$scope', '$state', 'BillTransaction', 'BillTransactionSearch'];

    function BillTransactionMySuffixController ($scope, $state, BillTransaction, BillTransactionSearch) {
        var vm = this;
        
        vm.billTransactions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BillTransaction.query(function(result) {
                vm.billTransactions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BillTransactionSearch.query({query: vm.searchQuery}, function(result) {
                vm.billTransactions = result;
            });
        }    }
})();
