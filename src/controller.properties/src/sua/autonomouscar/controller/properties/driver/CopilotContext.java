package sua.autonomouscar.controller.properties.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;

public class CopilotContext extends KnowledgeBase{
	private static boolean seatOccupied;

	public CopilotContext(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(CopilotContext.class.getName());
    }
	
	public static boolean isSeatOccupied() {
		return seatOccupied;
	}

	public static void setSeatOccupied(boolean seatOccupied) {
		CopilotContext.seatOccupied = seatOccupied;
	}

}
