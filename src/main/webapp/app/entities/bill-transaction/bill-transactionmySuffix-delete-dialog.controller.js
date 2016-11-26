(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTransactionMySuffixDeleteController',BillTransactionMySuffixDeleteController);

    BillTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'BillTransaction'];

    function BillTransactionMySuffixDeleteController($uibModalInstance, entity, BillTransaction) {
        var vm = this;

        vm.billTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BillTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
