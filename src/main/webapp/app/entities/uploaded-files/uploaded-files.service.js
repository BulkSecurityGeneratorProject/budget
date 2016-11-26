(function() {
    'use strict';
    angular
        .module('budgetApp')
        .factory('UploadedFiles', UploadedFiles);

    UploadedFiles.$inject = ['$resource', 'DateUtils'];

    function UploadedFiles ($resource, DateUtils) {
        var resourceUrl =  'api/uploaded-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.uploadDate = DateUtils.convertLocalDateFromServer(data.uploadDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.uploadDate = DateUtils.convertLocalDateToServer(copy.uploadDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.uploadDate = DateUtils.convertLocalDateToServer(copy.uploadDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
