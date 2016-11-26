(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('BillTransactionSearch', BillTransactionSearch);

    BillTransactionSearch.$inject = ['$resource'];

    function BillTransactionSearch($resource) {
        var resourceUrl =  'api/_search/bill-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
