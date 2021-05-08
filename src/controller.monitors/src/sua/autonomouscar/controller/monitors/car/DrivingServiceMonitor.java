package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.utils.DrivingAutonomyLevel;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DrivingServiceMonitor {
    private BundleContext context;

    public DrivingServiceMonitor(BundleContext context) {
        this.context = context;
    }

    public void registerAutonomyLevelChange(DrivingAutonomyLevel autonomyLevel, Class drivingServiceClass) {
        CurrentDrivingServiceStatus currentDrivingService = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);

        // Only update it if the value has changed.
        if (currentDrivingService != null
                && (currentDrivingService.getAutonomyLevel() != autonomyLevel
                    || drivingServiceClass != currentDrivingService.getDrivingServiceClass())) {

            System.out.println("[ Driving Service Monitor ] -  Updating the Autonomy Level to " + autonomyLevel);
            System.out.println("[ Driving Service Monitor ] -  Updating the Driving Service Class to " + drivingServiceClass.getSimpleName());

            currentDrivingService.setAutonomyLevel(autonomyLevel, drivingServiceClass);
        }
    }
}
