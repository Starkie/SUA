package sua.autonomouscar.controller.monitors.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.driver.DriverContext;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.interfaces.EFaceStatus;

public class DriverStatusMonitor implements IDriverStatusMonitor{
	private BundleContext context;

    public DriverStatusMonitor(BundleContext context) {
        this.context = context;
    }
    
	@Override
	public void registerDriverStatusChange(EFaceStatus status) {
		DriverContext driverContext = OSGiUtils.getService(context, DriverContext.class);

        // Only update it if the value has changed.
        if (driverContext != null && driverContext.getDriverStatus() != status) {
            System.out.println("[Driver Status Monitor] -  Updating the Driver Status to " + status);

            driverContext.setDriverStatus(status);
        }
		
	}

	@Override
	public void registerHandsOnWheelChange(boolean status) {
		DriverContext driverContext = OSGiUtils.getService(context, DriverContext.class);
		if (driverContext != null && driverContext.isHasHandsOnWheel() != status) {
            System.out.println("[Driver Status Monitor] -  Updating the Driver 'hands on wheel' to " + status);

            driverContext.setHasHandsOnWheel(status);
        }
		
	}

	@Override
	public void registerDriverSeatChange(boolean status) {
		DriverContext driverContext = OSGiUtils.getService(context, DriverContext.class);
		if (driverContext != null && driverContext.isDriverSeatOccupied() != status) {
            System.out.println("[Driver Status Monitor] -  Updating the Driver 'seat occupied' to " + status);

            driverContext.setDriverSeatOccupied(status);
        }
		
	}
	
	@Override
	public void registerIsDriverReadyChange() {
		DriverContext driverContext = OSGiUtils.getService(context, DriverContext.class);
		if (driverContext != null &&
			driverContext.getDriverStatus() == EFaceStatus.LOOKING_FORWARD &&
			driverContext.isDriverSeatOccupied() &&
			driverContext.isHasHandsOnWheel()) {
            System.out.println("[Driver Status Monitor] -  Updating the Driver 'is ready' to true");

            driverContext.setDriverReady(true);
        } else {
        	System.out.println("[Driver Status Monitor] -  Updating the Driver 'is ready' to false");

            driverContext.setDriverReady(false);
        }
		
	}
	

}
