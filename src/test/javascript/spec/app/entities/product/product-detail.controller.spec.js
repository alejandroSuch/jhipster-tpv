'use strict';

describe('Controller Tests', function() {

    describe('Product Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduct, MockPrice, MockCategory, MockDiscount;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduct = jasmine.createSpy('MockProduct');
            MockPrice = jasmine.createSpy('MockPrice');
            MockCategory = jasmine.createSpy('MockCategory');
            MockDiscount = jasmine.createSpy('MockDiscount');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Product': MockProduct,
                'Price': MockPrice,
                'Category': MockCategory,
                'Discount': MockDiscount
            };
            createController = function() {
                $injector.get('$controller')("ProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tpvApp:productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
