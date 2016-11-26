(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTransactionMySuffixDialogController', BillTransactionMySuffixDialogController);

    BillTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BillTransaction', 'BillType', 'AllyTransaction', 'AmexTransaction'];

    function BillTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BillTransaction, BillType, AllyTransaction, AmexTransaction) {
        var vm = this;

        vm.billTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.billtypes = BillType.query();
        vm.allytransactions = AllyTransaction.query();
        vm.amextransactions = AmexTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.billTransaction.id !== null) {
                BillTransaction.update(vm.billTransaction, onSaveSuccess, onSaveError);
            } else {
                BillTransaction.save(vm.billTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:billTransactionUpdate', result);
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
