'use strict';

describe('Controller Tests', function() {

    describe('Category Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategory, MockVat;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategory = jasmine.createSpy('MockCategory');
            MockVat = jasmine.createSpy('MockVat');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Category': MockCategory,
                'Vat': MockVat
            };
            createController = function() {
                $injector.get('$controller')("CategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tpvApp:categoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
