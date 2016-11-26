(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('SpendingTransactionSearch', SpendingTransactionSearch);

    SpendingTransactionSearch.$inject = ['$resource'];

    function SpendingTransactionSearch($resource) {
        var resourceUrl =  'api/_search/spending-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
