(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTransactionMySuffixDetailController', BillTransactionMySuffixDetailController);

    BillTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BillTransaction', 'BillType', 'AllyTransaction', 'AmexTransaction', 'WellsFargoTransaction'];

    function BillTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, BillTransaction, BillType, AllyTransaction, AmexTransaction, WellsFargoTransaction) {
        var vm = this;

        vm.billTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:billTransactionUpdate', function(event, result) {
            vm.billTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
