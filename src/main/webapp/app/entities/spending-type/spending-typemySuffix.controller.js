(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTypeMySuffixController', SpendingTypeMySuffixController);

    SpendingTypeMySuffixController.$inject = ['$scope', '$state', 'SpendingType', 'SpendingTypeSearch'];

    function SpendingTypeMySuffixController ($scope, $state, SpendingType, SpendingTypeSearch) {
        var vm = this;
        
        vm.spendingTypes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SpendingType.query(function(result) {
                vm.spendingTypes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SpendingTypeSearch.query({query: vm.searchQuery}, function(result) {
                vm.spendingTypes = result;
            });
        }    }
})();
