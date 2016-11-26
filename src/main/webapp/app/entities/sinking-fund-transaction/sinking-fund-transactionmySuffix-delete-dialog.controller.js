(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SinkingFundTransactionMySuffixDeleteController',SinkingFundTransactionMySuffixDeleteController);

    SinkingFundTransactionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'SinkingFundTransaction'];

    function SinkingFundTransactionMySuffixDeleteController($uibModalInstance, entity, SinkingFundTransaction) {
        var vm = this;

        vm.sinkingFundTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SinkingFundTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
