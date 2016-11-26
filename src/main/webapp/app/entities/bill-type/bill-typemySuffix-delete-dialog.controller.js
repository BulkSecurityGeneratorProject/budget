(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTypeMySuffixDeleteController',BillTypeMySuffixDeleteController);

    BillTypeMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'BillType'];

    function BillTypeMySuffixDeleteController($uibModalInstance, entity, BillType) {
        var vm = this;

        vm.billType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BillType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
