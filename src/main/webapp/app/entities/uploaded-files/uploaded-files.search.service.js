(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('UploadedFilesSearch', UploadedFilesSearch);

    UploadedFilesSearch.$inject = ['$resource'];

    function UploadedFilesSearch($resource) {
        var resourceUrl =  'api/_search/uploaded-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
