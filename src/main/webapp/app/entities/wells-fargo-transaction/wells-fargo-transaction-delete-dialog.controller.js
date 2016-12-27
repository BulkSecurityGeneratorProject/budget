(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('WellsFargoTransactionDeleteController',WellsFargoTransactionDeleteController);

    WellsFargoTransactionDeleteController.$inject = ['$uibModalInstance', 'entity', 'WellsFargoTransaction'];

    function WellsFargoTransactionDeleteController($uibModalInstance, entity, WellsFargoTransaction) {
        var vm = this;

        vm.wellsFargoTransaction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WellsFargoTransaction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
