(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AmexTransactionMySuffixDeleteController',AmexTransactionMySuffixDeleteController);

    AmexTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'AmexTransaction'];

    function AmexTransactionMySuffixDeleteController($uibModalInstance, entity, AmexTransaction) {
        var vm = this;

        vm.amexTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AmexTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
