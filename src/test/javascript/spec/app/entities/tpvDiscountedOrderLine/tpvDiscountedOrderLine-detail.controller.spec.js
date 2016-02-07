'use strict';

describe('Controller Tests', function() {

    describe('TpvDiscountedOrderLine Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTpvDiscountedOrderLine, MockDiscount;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTpvDiscountedOrderLine = jasmine.createSpy('MockTpvDiscountedOrderLine');
            MockDiscount = jasmine.createSpy('MockDiscount');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TpvDiscountedOrderLine': MockTpvDiscountedOrderLine,
                'Discount': MockDiscount
            };
            createController = function() {
                $injector.get('$controller')("TpvDiscountedOrderLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tpvApp:tpvDiscountedOrderLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
