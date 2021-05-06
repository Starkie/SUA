package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.devices.interfaces.IHumanSensors;

/**
 * Represents the status of the {@link IHumanSensors}.
 */
public class HumanSensorsHealthStatus extends HealthStatusBase {
    public HumanSensorsHealthStatus(BundleContext context) {
        super(context);

        this.addImplementedInterface(HumanSensorsHealthStatus.class.getName());
    }
}
