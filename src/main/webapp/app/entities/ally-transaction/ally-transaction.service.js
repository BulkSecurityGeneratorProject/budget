(function() {
    'use strict';
    angular
        .module('budgetApp')
        .factory('AllyTransaction', AllyTransaction);

    AllyTransaction.$inject = ['$resource', 'DateUtils'];

    function AllyTransaction ($resource, DateUtils) {
        var resourceUrl =  'api/ally-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
