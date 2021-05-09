package controller.initialization;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.utils.AutonomousVehicleContextUtils;
import sua.autonomouscar.devices.interfaces.IEngine;
import sua.autonomouscar.devices.interfaces.ISteering;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_ManualDriving;
import sua.autonomouscar.driving.l0.manual.L0_ManualDriving;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.devices.Engine;
import sua.autonomouscar.infrastructure.devices.Steering;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
	    // Set the initial state of the autonomous car:

	    // Initialize the engine component.
	    IEngine registeredEngine = OSGiUtils.getService(bundleContext, IEngine.class);

	    if (registeredEngine == null)
	    {
	        Engine engine = new Engine(bundleContext, "Engine");
	        engine.registerThing();

	        registeredEngine = engine;
	    }

	    // Initialize the steering component.
	    ISteering registeredSteering = OSGiUtils.getService(bundleContext, ISteering.class);

        if (registeredSteering == null)
        {
            Steering steering = new Steering(bundleContext, "Steering");
            steering.registerThing();

            registeredSteering = steering;
        }

        // Initialize the execution of L0_ManualDriving module.
        IDrivingService currentDrivingService = AutonomousVehicleContextUtils.findCurrentDrivingService(bundleContext);

        if (currentDrivingService != null && !(currentDrivingService instanceof IL0_ManualDriving))
        {
            currentDrivingService.stopDriving();
        }

        IL0_ManualDriving manualDriving = initializeManualDriving(bundleContext);

        manualDriving.startDriving();
	}

    private IL0_ManualDriving initializeManualDriving(BundleContext bundleContext) {
        IL0_ManualDriving manualDriving = OSGiUtils.getService(bundleContext, IL0_ManualDriving.class);

        if (manualDriving == null)
        {
            // If the module does not exist, instantiate it.
            L0_ManualDriving l0ManualDriving = new L0_ManualDriving(bundleContext, "L0_ManualDriving");
            l0ManualDriving.registerThing();

            manualDriving = l0ManualDriving;
        }

        return manualDriving;
    }

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
