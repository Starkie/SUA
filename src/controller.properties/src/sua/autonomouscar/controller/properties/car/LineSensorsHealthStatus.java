package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.controller.utils.LineSensorPosition;
import sua.autonomouscar.devices.interfaces.ILineSensor;

/**
 * Represents the status of the {@link ILineSensor}.
 */
public class LineSensorsHealthStatus extends HealthStatusBase {
    public LineSensorsHealthStatus(BundleContext context, LineSensorPosition position) {
        super(context);
        
        this.addImplementedInterface(LineSensorsHealthStatus.class.getName());
        
        this.properties.put("position", position);
    }
}
