package sua.autonomouscar.controller.properties.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;
import sua.autonomouscar.interfaces.EFaceStatus;

public class DriverContext extends KnowledgeBase{
	
	private static final String HAS_HANDS_ON_WHEEL = "hasHandsOnWheel";
    private static final String IS_DRIVER_SEAT_OCCUPIED = "isDriverSeatOccupied";
    private static final String DRIVER_STATUS = "driverStatus";
    private static final String IS_DRIVER_READY = "isDriverReady";
	
	public DriverContext(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(DriverContext.class.getName());
    }
	
	public boolean getHasHandsOnWheel() {
		return (boolean) this.properties.get(HAS_HANDS_ON_WHEEL);
	}

	public void setHasHandsOnWheel(boolean hasHandsOnWheel) {
		if (hasHandsOnWheel != this.getHasHandsOnWheel()) {
            updateProperty(HAS_HANDS_ON_WHEEL, hasHandsOnWheel, false);
            this.updateIsDriverReady();
        }
	}

	public boolean isDriverSeatOccupied() {
		Object propertyValue = this.properties.get(IS_DRIVER_SEAT_OCCUPIED);
		
		if (propertyValue == null)
		{
			return false;
		}
		
		return (boolean) propertyValue;
	}

	public void setDriverSeatOccupied(boolean isDriverSeatOccupied) {
		if (isDriverSeatOccupied != this.isDriverSeatOccupied()) {
            updateProperty(IS_DRIVER_SEAT_OCCUPIED, isDriverSeatOccupied, false);
            this.updateIsDriverReady();
        }
	}

	public void updateIsDriverReady() {
		boolean isDriverReady = this.getDriverStatus() == EFaceStatus.LOOKING_FORWARD &&
								isDriverSeatOccupied() &&
								getHasHandsOnWheel();
			
		updateProperty(IS_DRIVER_READY, isDriverReady, true);
	}

	public boolean isDriverReady() {
		return (boolean) this.properties.get(IS_DRIVER_READY);
	}

	public EFaceStatus getDriverStatus() {
		return (EFaceStatus) this.properties.get(DRIVER_STATUS);
	}

	public void setDriverStatus(EFaceStatus driverStatus) {
		if (driverStatus != this.getDriverStatus()) {
            updateProperty(DRIVER_STATUS, driverStatus, false);
            this.updateIsDriverReady();
        }
	}

}
