'use strict';

describe('Controller Tests', function() {

    describe('BillTransaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBillTransaction, MockBillType, MockAllyTransaction, MockAmexTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBillTransaction = jasmine.createSpy('MockBillTransaction');
            MockBillType = jasmine.createSpy('MockBillType');
            MockAllyTransaction = jasmine.createSpy('MockAllyTransaction');
            MockAmexTransaction = jasmine.createSpy('MockAmexTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BillTransaction': MockBillTransaction,
                'BillType': MockBillType,
                'AllyTransaction': MockAllyTransaction,
                'AmexTransaction': MockAmexTransaction
            };
            createController = function() {
                $injector.get('$controller')("BillTransactionMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'budgetApp:billTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
