(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTypeMySuffixDeleteController',SpendingTypeMySuffixDeleteController);

    SpendingTypeMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'SpendingType'];

    function SpendingTypeMySuffixDeleteController($uibModalInstance, entity, SpendingType) {
        var vm = this;

        vm.spendingType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SpendingType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
