(function () {
    'use strict';
    angular
        .module('budgetApp')
        .controller('UploadTransactionController', UploadTransactionController)
        .filter('quantityTons', function() {
        	return function(number) {
	    		if(isNaN(number) || number < 1) {
    		      return number;
    		    } else {
    		    	return number + " tons";
    		    }
        	}
        });
    
    UploadTransactionController.$inject = ['$timeout', '$scope', '$stateParams', '$q', 'UploadTransaction'];

    function UploadTransactionController ($timeout, $scope, $stateParams, $q, UploadTransaction) {
        var vm = this;

        vm.uploadTransaction = UploadTransaction;
        vm.save = save;

        $timeout(function (){
	        angular.element('.form-group:eq(1)>input').focus();
	    });

        function save () {
        	var upload = {
        			bank: vm.bank,
        			file: vm.file,
        	}
           UploadTransaction.upload(upload);
        }

       

    }
})();
