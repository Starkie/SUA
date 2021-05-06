package sua.autonomouscar.controller.monitors.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.interfaces.IHealthMonitor;
import sua.autonomouscar.controller.properties.car.FallbackPlanHealthStatus;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class FallBackPlanHealthMonitor implements IHealthMonitor
{
    private BundleContext context;

    public FallBackPlanHealthMonitor(BundleContext context) {
        this.context = context;
    }

    @Override
    public void registerHealthCheck(boolean isFallbackPlanAvailable) {
        FallbackPlanHealthStatus fallbackPlanHealthStatus = OSGiUtils.getService(context, FallbackPlanHealthStatus.class);

        // Only update it if the value has changed.
        if (fallbackPlanHealthStatus != null && fallbackPlanHealthStatus.isAvailable() != isFallbackPlanAvailable) {
            System.out.println("[ Fallback Plan Health Monitor ] -  Updating the HealthStatus to " + isFallbackPlanAvailable);

            fallbackPlanHealthStatus.setIsAvailable(isFallbackPlanAvailable);
        }
    }
}
