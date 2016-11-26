'use strict';

describe('Controller Tests', function() {

    describe('SinkingFundTransaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSinkingFundTransaction, MockAllyTransaction, MockAmexTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSinkingFundTransaction = jasmine.createSpy('MockSinkingFundTransaction');
            MockAllyTransaction = jasmine.createSpy('MockAllyTransaction');
            MockAmexTransaction = jasmine.createSpy('MockAmexTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SinkingFundTransaction': MockSinkingFundTransaction,
                'AllyTransaction': MockAllyTransaction,
                'AmexTransaction': MockAmexTransaction
            };
            createController = function() {
                $injector.get('$controller')("SinkingFundTransactionMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:sinkingFundTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
