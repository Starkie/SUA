package controller.properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.road.RoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private RoadContext roadContext;
	private ServiceRegistration<?>roadContextRegistration;
	
	private CurrentDrivingServiceStatus currentDrivingLevel;
    private ServiceRegistration<?> currentDrivingLevelRegistration;
    
	public void start(BundleContext bundleContext) throws Exception {
        this.roadContext = new RoadContext(context);
        this.roadContextRegistration = this.roadContext.registerKnowledge();
        
        this.currentDrivingLevel = new CurrentDrivingServiceStatus(context);
        this.currentDrivingLevelRegistration = this.currentDrivingLevel.registerKnowledge();
	    
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    this.roadContextRegistration.unregister();
        this.currentDrivingLevelRegistration.unregister();
        
        this.roadContext = null;
        this.currentDrivingLevel = null;
	    
		Activator.context = null;
	}
}
