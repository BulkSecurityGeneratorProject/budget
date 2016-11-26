(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AllyTransactionMySuffixDeleteController',AllyTransactionMySuffixDeleteController);

    AllyTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'AllyTransaction'];

    function AllyTransactionMySuffixDeleteController($uibModalInstance, entity, AllyTransaction) {
        var vm = this;

        vm.allyTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AllyTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
