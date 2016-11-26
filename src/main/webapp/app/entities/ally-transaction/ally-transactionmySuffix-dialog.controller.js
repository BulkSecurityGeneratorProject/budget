(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AllyTransactionMySuffixDialogController', AllyTransactionMySuffixDialogController);

    AllyTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AllyTransaction'];

    function AllyTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AllyTransaction) {
        var vm = this;

        vm.allyTransaction = entity;
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
            if (vm.allyTransaction.id !== null) {
                AllyTransaction.update(vm.allyTransaction, onSaveSuccess, onSaveError);
            } else {
                AllyTransaction.save(vm.allyTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:allyTransactionUpdate', result);
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
