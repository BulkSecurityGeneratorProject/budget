(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('SinkingFundTransactionSearch', SinkingFundTransactionSearch);

    SinkingFundTransactionSearch.$inject = ['$resource'];

    function SinkingFundTransactionSearch($resource) {
        var resourceUrl =  'api/_search/sinking-fund-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
