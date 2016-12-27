(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('WellsFargoTransactionDetailController', WellsFargoTransactionDetailController);

    WellsFargoTransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WellsFargoTransaction'];

    function WellsFargoTransactionDetailController($scope, $rootScope, $stateParams, previousState, entity, WellsFargoTransaction) {
        var vm = this;

        vm.wellsFargoTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:wellsFargoTransactionUpdate', function(event, result) {
            vm.wellsFargoTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
