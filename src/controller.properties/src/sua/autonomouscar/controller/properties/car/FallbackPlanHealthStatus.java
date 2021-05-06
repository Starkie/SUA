package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.driving.interfaces.IFallbackPlan;

/**
 * Represents the status of the {@link IFallbackPlan}.
 */
public class FallbackPlanHealthStatus extends HealthStatusBase {
    public FallbackPlanHealthStatus(BundleContext context) {
        super(context);

        this.addImplementedInterface(FallbackPlanHealthStatus.class.getName());
    }
}
