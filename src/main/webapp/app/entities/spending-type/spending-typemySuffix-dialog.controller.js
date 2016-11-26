(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTypeMySuffixDialogController', SpendingTypeMySuffixDialogController);

    SpendingTypeMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpendingType', 'SpendingTransaction'];

    function SpendingTypeMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpendingType, SpendingTransaction) {
        var vm = this;

        vm.spendingType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.spendingtransactions = SpendingTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spendingType.id !== null) {
                SpendingType.update(vm.spendingType, onSaveSuccess, onSaveError);
            } else {
                SpendingType.save(vm.spendingType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:spendingTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
