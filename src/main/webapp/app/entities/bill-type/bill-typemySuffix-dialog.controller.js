(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTypeMySuffixDialogController', BillTypeMySuffixDialogController);

    BillTypeMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BillType', 'BillTransaction'];

    function BillTypeMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BillType, BillTransaction) {
        var vm = this;

        vm.billType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.billtransactions = BillTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.billType.id !== null) {
                BillType.update(vm.billType, onSaveSuccess, onSaveError);
            } else {
                BillType.save(vm.billType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:billTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
