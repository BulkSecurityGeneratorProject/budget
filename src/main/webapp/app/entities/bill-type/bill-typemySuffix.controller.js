(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTypeMySuffixController', BillTypeMySuffixController);

    BillTypeMySuffixController.$inject = ['$scope', '$state', 'BillType', 'BillTypeSearch'];

    function BillTypeMySuffixController ($scope, $state, BillType, BillTypeSearch) {
        var vm = this;
        
        vm.billTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BillType.query(function(result) {
                vm.billTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BillTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.billTypes = result;
            });
        }    }
})();
