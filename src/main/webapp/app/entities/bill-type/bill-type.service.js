(function() {
    'use strict';
    angular
        .module('budgetApp')
        .factory('BillType', BillType);

    BillType.$inject = ['$resource'];

    function BillType ($resource) {
        var resourceUrl =  'api/bill-types/:id';

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
