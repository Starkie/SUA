package sua.autonomouscar.devices.interfaces;

public interface ICopilotSensor {

	public boolean isCopilotSeatOccupied();
	
	public void setCopilotSeatOccupied(boolean isDriverSeatOccupied);
}
