package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;
import sua.autonomouscar.devices.interfaces.IEngine;

/**
 * Represents the status of the {@link IEngine}.
 */
public class EngineHealthStatus extends HealthStatusBase {
    public EngineHealthStatus(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(EngineHealthStatus.class.getName());
    }
}
