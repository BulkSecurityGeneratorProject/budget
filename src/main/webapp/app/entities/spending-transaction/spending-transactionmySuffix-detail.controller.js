(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTransactionMySuffixDetailController', SpendingTransactionMySuffixDetailController);

    SpendingTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SpendingTransaction', 'SpendingType', 'AllyTransaction', 'AmexTransaction', 'WellsFargoTransaction'];

    function SpendingTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, SpendingTransaction, SpendingType, AllyTransaction, AmexTransaction, WellsFargoTransaction) {
        var vm = this;

        vm.spendingTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:spendingTransactionUpdate', function(event, result) {
            vm.spendingTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
