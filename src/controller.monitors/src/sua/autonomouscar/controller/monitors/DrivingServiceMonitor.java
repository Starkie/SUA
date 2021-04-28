package sua.autonomouscar.controller.monitors;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.DrivingAutonomyLevel;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class DrivingServiceMonitor {
    private BundleContext context;

    public DrivingServiceMonitor(BundleContext context) {
        this.context = context;
    }

    public void registerAutonomyLevelChange(DrivingAutonomyLevel autonomyLevel) {
        CurrentDrivingServiceStatus currentDrivingService = OSGiUtils.getService(context, CurrentDrivingServiceStatus.class);

        // Only update it if the value has changed.
        if (currentDrivingService != null && currentDrivingService.getAutonomyLevel() != autonomyLevel) {
            System.out.println("[ Driving Service Monitor ] -  Updating the Autonomy Level to " + autonomyLevel);

            currentDrivingService.setAutonomyLevel(autonomyLevel);
        }
    }
}
