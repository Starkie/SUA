package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.controller.utils.DistanceSensorPositon;
import sua.autonomouscar.devices.interfaces.IDistanceSensor;

/**
 * Represents the status of the {@link IDistanceSensor}.
 */
public class DistanceSensorHealthStatus extends HealthStatusBase {
    public DistanceSensorHealthStatus(BundleContext context, DistanceSensorPositon position) {
        super(context);

        this.addImplementedInterface(DistanceSensorHealthStatus.class.getName());

        this.properties.put("position", position);
    }
}
