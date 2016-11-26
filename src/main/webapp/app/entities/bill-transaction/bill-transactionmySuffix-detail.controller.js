(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTransactionMySuffixDetailController', BillTransactionMySuffixDetailController);

    BillTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BillTransaction', 'BillType', 'AllyTransaction', 'AmexTransaction'];

    function BillTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, BillTransaction, BillType, AllyTransaction, AmexTransaction) {
        var vm = this;

        vm.billTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:billTransactionUpdate', function(event, result) {
            vm.billTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
