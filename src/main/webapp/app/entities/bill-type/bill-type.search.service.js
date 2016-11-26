(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('BillTypeSearch', BillTypeSearch);

    BillTypeSearch.$inject = ['$resource'];

    function BillTypeSearch($resource) {
        var resourceUrl =  'api/_search/bill-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
