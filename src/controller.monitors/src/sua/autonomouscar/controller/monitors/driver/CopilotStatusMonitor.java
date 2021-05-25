package sua.autonomouscar.controller.monitors.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.driver.CopilotContext;
import sua.autonomouscar.infrastructure.OSGiUtils;

public class CopilotStatusMonitor implements ISeatStatusMonitor{

	private BundleContext context;

    public CopilotStatusMonitor(BundleContext context) {
        this.context = context;
    }
	
	@Override
	public void registerSeatChange(boolean status) {
		CopilotContext driverContext = OSGiUtils.getService(context, CopilotContext.class);
		
		if (driverContext != null && driverContext.isSeatOccupied() != status) {
            System.out.println("[Copilot Status Monitor] -  Updating the Copilot 'seat occupied' to " + status);

            driverContext.setSeatOccupied(status);
        }
		
	}
}
