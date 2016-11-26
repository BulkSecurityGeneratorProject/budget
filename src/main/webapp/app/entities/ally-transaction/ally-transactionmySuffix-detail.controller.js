(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('AllyTransactionMySuffixDetailController', AllyTransactionMySuffixDetailController);

    AllyTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AllyTransaction'];

    function AllyTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, AllyTransaction) {
        var vm = this;

        vm.allyTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:allyTransactionUpdate', function(event, result) {
            vm.allyTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
