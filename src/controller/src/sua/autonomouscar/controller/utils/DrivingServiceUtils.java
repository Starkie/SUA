package sua.autonomouscar.controller.utils;

import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.driving.interfaces.IL0_DrivingService;
import sua.autonomouscar.driving.interfaces.IL1_DrivingService;
import sua.autonomouscar.driving.interfaces.IL2_DrivingService;
import sua.autonomouscar.driving.interfaces.IL3_DrivingService;

/**
 * Class with utility methods to get information from {@link IDrivingService}.
 */
public class DrivingServiceUtils {
	/**
	 * Checks if the given driving service is an {@link IL0_DrivingService}.
	 * @param currentDrivingService The driving service.
	 * @return True if it is an instance of an L0 driving service. False otherwise
	 */
	public static boolean isL0DrivingService(IDrivingService currentDrivingService) {
		return currentDrivingService instanceof IL0_DrivingService 
				&& !(currentDrivingService instanceof IL1_DrivingService)
				&& !(currentDrivingService instanceof IL2_DrivingService)
				&& !(currentDrivingService instanceof IL3_DrivingService);
	}
	
	/**
	 * Checks if the given driving service is an {@link IL1_DrivingService}.
	 * @param currentDrivingService The driving service.
	 * @return True if it is an instance of an L1 driving service. False otherwise
	 */
	public static boolean isL1DrivingService(IDrivingService currentDrivingService) {
		return currentDrivingService instanceof IL1_DrivingService
				&& !(currentDrivingService instanceof IL2_DrivingService)
				&& !(currentDrivingService instanceof IL3_DrivingService);
	}
	
	/**
	 * Checks if the given driving service is an {@link IL2_DrivingService}.
	 * @param currentDrivingService The driving service.
	 * @return True if it is an instance of an L2 driving service. False otherwise
	 */
	public static boolean isL2DrivingService(IDrivingService currentDrivingService) {
		return currentDrivingService instanceof IL2_DrivingService
				&& !(currentDrivingService instanceof IL3_DrivingService);
	}
	
	/**
	 * Checks if the given driving service is an {@link IL3_DrivingService}.
	 * @param currentDrivingService The driving service.
	 * @return True if it is an instance of an L3 driving service. False otherwise
	 */
	public static boolean isL3DrivingService(IDrivingService currentDrivingService) {
		return currentDrivingService instanceof IL3_DrivingService;
	}
}
