(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('WellsFargoTransactionSearch', WellsFargoTransactionSearch);

    WellsFargoTransactionSearch.$inject = ['$resource'];

    function WellsFargoTransactionSearch($resource) {
        var resourceUrl =  'api/_search/wells-fargo-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
