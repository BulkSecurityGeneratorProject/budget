(function() {
    'use strict';
    angular
        .module('budgetApp')
        .factory('UploadTransaction', UploadTransaction);

    UploadTransaction.$inject = ['$resource', 'DateUtils'];

    function UploadTransaction($resource, DateUtils) {
        var resourceUrl =  '/api/upload-transactions';

        return $resource(resourceUrl, {}, {
        	'upload': { method:'POST' },
            
        });
    }
})();