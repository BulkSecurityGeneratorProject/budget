(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('AllyTransactionSearch', AllyTransactionSearch);

    AllyTransactionSearch.$inject = ['$resource'];

    function AllyTransactionSearch($resource) {
        var resourceUrl =  'api/_search/ally-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
