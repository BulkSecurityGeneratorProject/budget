(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('AmexTransactionSearch', AmexTransactionSearch);

    AmexTransactionSearch.$inject = ['$resource'];

    function AmexTransactionSearch($resource) {
        var resourceUrl =  'api/_search/amex-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
