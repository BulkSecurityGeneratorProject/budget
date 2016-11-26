(function() {
    'use strict';

    angular
        .module('budgetApp')
        .factory('SpendingTypeSearch', SpendingTypeSearch);

    SpendingTypeSearch.$inject = ['$resource'];

    function SpendingTypeSearch($resource) {
        var resourceUrl =  'api/_search/spending-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
