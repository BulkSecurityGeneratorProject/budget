(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AmexTransactionMySuffixDialogController', AmexTransactionMySuffixDialogController);

    AmexTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AmexTransaction'];

    function AmexTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AmexTransaction) {
        var vm = this;

        vm.amexTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.amexTransaction.id !== null) {
                AmexTransaction.update(vm.amexTransaction, onSaveSuccess, onSaveError);
            } else {
                AmexTransaction.save(vm.amexTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:amexTransactionUpdate', result);
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
