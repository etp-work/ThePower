describe("LifecycleTest Suite", function() {
	var listener;
	beforeEach(function() {
		listener = jasmine.createSpy('listener');
		Lifecycle.addStateListener(listener);
	});

	afterEach(function() {
		Lifecycle.removeStateListener(listener);
	});

	it("addStateListener test", function() {
		Lifecycle.setState(Lifecycle.LOADED);
		expect(listener).toHaveBeenCalledWith(Lifecycle.LOADED);
	});

	it("removeStateListener test", function() {
		Lifecycle.removeStateListener(listener);
		Lifecycle.setState(Lifecycle.LOADED);
		expect(listener.calls.length).toEqual(1);
	});
});
