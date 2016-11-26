(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AmexTransactionMySuffixDetailController', AmexTransactionMySuffixDetailController);

    AmexTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AmexTransaction'];

    function AmexTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, AmexTransaction) {
        var vm = this;

        vm.amexTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:amexTransactionUpdate', function(event, result) {
            vm.amexTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
