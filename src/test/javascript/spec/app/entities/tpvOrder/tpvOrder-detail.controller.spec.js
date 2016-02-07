'use strict';

describe('Controller Tests', function() {

    describe('TpvOrder Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTpvOrder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTpvOrder = jasmine.createSpy('MockTpvOrder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TpvOrder': MockTpvOrder
            };
            createController = function() {
                $injector.get('$controller')("TpvOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tpvApp:tpvOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
