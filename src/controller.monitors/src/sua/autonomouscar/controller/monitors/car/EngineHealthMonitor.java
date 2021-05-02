package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.car.EngineHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class EngineHealthMonitor{

    private BundleContext context;

    public EngineHealthMonitor(BundleContext context) {
        this.context = context;
    }

    public void registerHealthCheck(boolean isEngineAvailable) {
        EngineHealthStatus engineHealthStatus = OSGiUtils.getService(context, EngineHealthStatus.class);
        
        // Only update it if the value has changed.
        if (engineHealthStatus != null && engineHealthStatus.isAvailable() != isEngineAvailable) {
            System.out.println("[ Engine Health Monitor ] -  Updating the HealthStatus to " + isEngineAvailable);

            engineHealthStatus.setIsAvailable(isEngineAvailable);
        }
    }
}
