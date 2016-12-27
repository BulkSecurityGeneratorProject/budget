'use strict';

describe('Controller Tests', function() {

    describe('SpendingTransaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSpendingTransaction, MockSpendingType, MockAllyTransaction, MockAmexTransaction, MockWellsFargoTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSpendingTransaction = jasmine.createSpy('MockSpendingTransaction');
            MockSpendingType = jasmine.createSpy('MockSpendingType');
            MockAllyTransaction = jasmine.createSpy('MockAllyTransaction');
            MockAmexTransaction = jasmine.createSpy('MockAmexTransaction');
            MockWellsFargoTransaction = jasmine.createSpy('MockWellsFargoTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SpendingTransaction': MockSpendingTransaction,
                'SpendingType': MockSpendingType,
                'AllyTransaction': MockAllyTransaction,
                'AmexTransaction': MockAmexTransaction,
                'WellsFargoTransaction': MockWellsFargoTransaction
            };
            createController = function() {
                $injector.get('$controller')("SpendingTransactionMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:spendingTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
