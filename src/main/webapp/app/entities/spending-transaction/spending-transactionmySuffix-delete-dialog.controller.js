(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTransactionMySuffixDeleteController',SpendingTransactionMySuffixDeleteController);

    SpendingTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'SpendingTransaction'];

    function SpendingTransactionMySuffixDeleteController($uibModalInstance, entity, SpendingTransaction) {
        var vm = this;

        vm.spendingTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SpendingTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
