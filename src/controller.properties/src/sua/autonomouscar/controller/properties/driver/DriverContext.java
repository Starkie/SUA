package sua.autonomouscar.controller.properties.driver;

import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.KnowledgeBase;
import sua.autonomouscar.interfaces.EFaceStatus;

public class DriverContext extends KnowledgeBase{
	
	private static boolean hasHandsOnWheel;
	private static boolean isDriverSeatOccupied;
	private static boolean isDriverReady;
	private static EFaceStatus status;
	
	public DriverContext(BundleContext context) {
        super(context);
        
        this.addImplementedInterface(DriverContext.class.getName());
    }
	
	public static boolean isHasHandsOnWheel() {
		return hasHandsOnWheel;
	}

	public static void setHasHandsOnWheel(boolean hasHandsOnWheel) {
		DriverContext.hasHandsOnWheel = hasHandsOnWheel;
	}

	public static boolean isDriverSeatOccupied() {
		return isDriverSeatOccupied;
	}

	public static void setDriverSeatOccupied(boolean isDriverSeatOccupied) {
		DriverContext.isDriverSeatOccupied = isDriverSeatOccupied;
	}

	public static boolean isDriverReady() {
		return isDriverReady;
	}

	public static void setDriverReady(boolean isDriverReady) {
		DriverContext.isDriverReady = isDriverReady;
	}

	public static EFaceStatus getDriverStatus() {
		return status;
	}

	public static void setDriverStatus(EFaceStatus status) {
		DriverContext.status = status;
	}

}
