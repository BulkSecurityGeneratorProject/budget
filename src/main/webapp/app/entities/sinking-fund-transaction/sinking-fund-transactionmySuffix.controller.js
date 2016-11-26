(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SinkingFundTransactionMySuffixController', SinkingFundTransactionMySuffixController);

    SinkingFundTransactionMySuffixController.$inject = ['$scope', '$state', 'SinkingFundTransaction', 'SinkingFundTransactionSearch'];

    function SinkingFundTransactionMySuffixController ($scope, $state, SinkingFundTransaction, SinkingFundTransactionSearch) {
        var vm = this;
        
        vm.sinkingFundTransactions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SinkingFundTransaction.query(function(result) {
                vm.sinkingFundTransactions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SinkingFundTransactionSearch.query({query: vm.searchQuery}, function(result) {
                vm.sinkingFundTransactions = result;
            });
        }    }
})();
