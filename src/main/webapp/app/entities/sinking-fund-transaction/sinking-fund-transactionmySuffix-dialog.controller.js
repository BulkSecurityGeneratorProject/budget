(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SinkingFundTransactionMySuffixDialogController', SinkingFundTransactionMySuffixDialogController);

    SinkingFundTransactionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SinkingFundTransaction', 'AllyTransaction', 'AmexTransaction'];

    function SinkingFundTransactionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SinkingFundTransaction, AllyTransaction, AmexTransaction) {
        var vm = this;

        vm.sinkingFundTransaction = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
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
            if (vm.sinkingFundTransaction.id !== null) {
                SinkingFundTransaction.update(vm.sinkingFundTransaction, onSaveSuccess, onSaveError);
            } else {
                SinkingFundTransaction.save(vm.sinkingFundTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:sinkingFundTransactionUpdate', result);
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
