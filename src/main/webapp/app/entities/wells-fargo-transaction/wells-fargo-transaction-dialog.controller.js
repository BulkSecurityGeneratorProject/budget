(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('WellsFargoTransactionDialogController', WellsFargoTransactionDialogController);

    WellsFargoTransactionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WellsFargoTransaction'];

    function WellsFargoTransactionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WellsFargoTransaction) {
        var vm = this;

        vm.wellsFargoTransaction = entity;
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
            if (vm.wellsFargoTransaction.id !== null) {
                WellsFargoTransaction.update(vm.wellsFargoTransaction, onSaveSuccess, onSaveError);
            } else {
                WellsFargoTransaction.save(vm.wellsFargoTransaction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('budgetApp:wellsFargoTransactionUpdate', result);
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
