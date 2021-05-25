package sua.autonomouscar.controller.monitors.driver;

import sua.autonomouscar.interfaces.EFaceStatus;

public interface IDriverStatusMonitor {
	
	void registerDriverStatusChange(EFaceStatus status);
	
	void registerHandsOnWheelChange(boolean status);
	
	void registerDriverSeatChange(boolean status);
	
}
