package sua.autonomouscar.controller.properties.car;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.HealthStatusBase;

/**
 * Represents the status of the {@link IHaptic}.
 */
public class HapticDeviceHealthStatus extends HealthStatusBase{

	public HapticDeviceHealthStatus(BundleContext context) {
        super(context);

        this.addImplementedInterface(HapticDeviceHealthStatus.class.getName());
    }
}
