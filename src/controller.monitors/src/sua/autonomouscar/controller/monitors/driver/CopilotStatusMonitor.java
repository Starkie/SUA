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
		CopilotContext copilotContext = OSGiUtils.getService(context, CopilotContext.class);
		
		if (copilotContext != null && copilotContext.isCopilotSeatOccupied() != status) {
            System.out.println("[Copilot Status Monitor] -  Updating the Copilot 'seat occupied' to " + status);

            copilotContext.setCopilotSeatOccupied(status);
        }
		
	}
}
