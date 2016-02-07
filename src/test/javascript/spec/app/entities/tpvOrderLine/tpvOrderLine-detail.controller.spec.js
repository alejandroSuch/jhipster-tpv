'use strict';

describe('Controller Tests', function() {

    describe('TpvOrderLine Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTpvOrderLine, MockTpvOrder, MockProduct, MockPrice, MockVat;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTpvOrderLine = jasmine.createSpy('MockTpvOrderLine');
            MockTpvOrder = jasmine.createSpy('MockTpvOrder');
            MockProduct = jasmine.createSpy('MockProduct');
            MockPrice = jasmine.createSpy('MockPrice');
            MockVat = jasmine.createSpy('MockVat');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TpvOrderLine': MockTpvOrderLine,
                'TpvOrder': MockTpvOrder,
                'Product': MockProduct,
                'Price': MockPrice,
                'Vat': MockVat
            };
            createController = function() {
                $injector.get('$controller')("TpvOrderLineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tpvApp:tpvOrderLineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
