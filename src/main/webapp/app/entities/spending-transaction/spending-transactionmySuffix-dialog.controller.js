(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTransactionMySuffixDialogController', SpendingTransactionMySuffixDialogController);

    SpendingTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpendingTransaction', 'SpendingType', 'AllyTransaction', 'AmexTransaction', 'WellsFargoTransaction'];

    function SpendingTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpendingTransaction, SpendingType, AllyTransaction, AmexTransaction, WellsFargoTransaction) {
        var vm = this;

        vm.spendingTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.spendingtypes = SpendingType.query();
        vm.allytransactions = AllyTransaction.query();
        vm.amextransactions = AmexTransaction.query();
        vm.wellsfargotransactions = WellsFargoTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spendingTransaction.id !== null) {
                SpendingTransaction.update(vm.spendingTransaction, onSaveSuccess, onSaveError);
            } else {
                SpendingTransaction.save(vm.spendingTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:spendingTransactionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
