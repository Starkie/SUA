package sua.autonomouscar.controller.monitors.driver;

import sua.autonomouscar.interfaces.EFaceStatus;

public interface IDriverStatusMonitor extends ISeatStatusMonitor {
	
	void registerDriverStatusChange(EFaceStatus status);
	
	void registerHandsOnWheelChange(boolean status);	
}
