(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('SpendingTypeMySuffixDetailController', SpendingTypeMySuffixDetailController);

    SpendingTypeMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SpendingType', 'SpendingTransaction'];

    function SpendingTypeMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, SpendingType, SpendingTransaction) {
        var vm = this;

        vm.spendingType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:spendingTypeUpdate', function(event, result) {
            vm.spendingType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
