package sua.autonomouscar.devices.interfaces;

import sua.autonomouscar.interfaces.EFaceStatus;

public interface IDriverSensor {
	
	public boolean isHasHandsOnWheel();
	public boolean isDriverSeatOccupied();
	public boolean isDriverReady();
	public EFaceStatus getDriverStatus();
	
	public void setHasHandsOnWheel(boolean hasHandsOnWheel);
	public void setDriverSeatOccupied(boolean isDriverSeatOccupied);
	public void setDriverReady(boolean isDriverReady);
	public void setDriverStatus(EFaceStatus status);
	
}
