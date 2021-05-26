package sua.autonomouscar.controller.properties.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;

public class CopilotContext extends KnowledgeBase{
	private static final String IS_COPILOT_SEAT_OCCUPIED = "isCopilotSeatOccupied";

	public CopilotContext(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(CopilotContext.class.getName());
    }
	
	public boolean isCopilotSeatOccupied() {
		Object propertyValue = this.properties.get(IS_COPILOT_SEAT_OCCUPIED);
		
		if (propertyValue == null) {
			return false;
		}
		return (boolean) propertyValue;
	}

	public void setCopilotSeatOccupied(boolean isCopilotSeatOccupied) {
		if (isCopilotSeatOccupied != this.isCopilotSeatOccupied()) {
            updateProperty(IS_COPILOT_SEAT_OCCUPIED, isCopilotSeatOccupied, true);
        }
	}

}
