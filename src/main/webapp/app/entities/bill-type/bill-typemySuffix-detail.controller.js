(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('BillTypeMySuffixDetailController', BillTypeMySuffixDetailController);

    BillTypeMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BillType', 'BillTransaction'];

    function BillTypeMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, BillType, BillTransaction) {
        var vm = this;

        vm.billType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('budgetApp:billTypeUpdate', function(event, result) {
            vm.billType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
