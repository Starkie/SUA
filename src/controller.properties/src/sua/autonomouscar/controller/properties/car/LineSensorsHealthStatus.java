package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.IEngine;

/**
 * Represents the status of the {@link IEngine}.
 */
public class LineSensorsHealthStatus extends HealthStatusBase {
    public LineSensorsHealthStatus(BundleContext context, LineSensorPosition position) {
        super(context);
        
        this.addImplementedInterface(LineSensorsHealthStatus.class.getName());
        
        this.properties.put("position", position);
    }
}
