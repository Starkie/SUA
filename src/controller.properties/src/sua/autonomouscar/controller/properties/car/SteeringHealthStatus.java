package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;

/**
 * Represents the status of the {@link ISteeringe}.
 */
public class SteeringHealthStatus extends HealthStatusBase {
    public SteeringHealthStatus(BundleContext context) {
        super(context);

        this.addImplementedInterface(SteeringHealthStatus.class.getName());
    }
}
