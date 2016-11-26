(function() {
    'use strict';
    angular
        .module('budgetApp')
        .factory('SpendingType', SpendingType);

    SpendingType.$inject = ['$resource'];

    function SpendingType ($resource) {
        var resourceUrl =  'api/spending-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
