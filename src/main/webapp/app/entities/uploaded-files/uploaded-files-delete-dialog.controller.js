(function() {
    'use strict';

    angular
        .module('budgetApp')
        .controller('UploadedFilesDeleteController',UploadedFilesDeleteController);

    UploadedFilesDeleteController.$inject = ['$uibModalInstance', 'entity', 'UploadedFiles'];

    function UploadedFilesDeleteController($uibModalInstance, entity, UploadedFiles) {
        var vm = this;

        vm.uploadedFiles = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UploadedFiles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
