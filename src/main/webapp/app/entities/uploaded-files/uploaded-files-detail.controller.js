(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('UploadedFilesDetailController', UploadedFilesDetailController);

    UploadedFilesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'UploadedFiles'];

    function UploadedFilesDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, UploadedFiles) {
        var vm = this;

        vm.uploadedFiles = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('budgetApp:uploadedFilesUpdate', function(event, result) {
            vm.uploadedFiles = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
