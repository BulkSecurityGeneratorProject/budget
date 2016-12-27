(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SinkingFundTransactionMySuffixDetailController', SinkingFundTransactionMySuffixDetailController);

    SinkingFundTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SinkingFundTransaction', 'AllyTransaction', 'AmexTransaction', 'WellsFargoTransaction'];

    function SinkingFundTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, SinkingFundTransaction, AllyTransaction, AmexTransaction, WellsFargoTransaction) {
        var vm = this;

        vm.sinkingFundTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:sinkingFundTransactionUpdate', function(event, result) {
            vm.sinkingFundTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
