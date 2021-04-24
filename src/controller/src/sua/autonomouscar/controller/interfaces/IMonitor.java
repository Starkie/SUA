package sua.autonomouscar.controller.interfaces;

/**
 * Monitors the measurements obtained from one ore more {@link IProbe} of the system and 
 * updates the adaption properties if necessary.
 */
public interface IMonitor {
	/**
	 *  Monitors the measurements obtained from one ore more {@link IProbe} of the system and 
	 * updates the adaption properties if necessary.
	 */
	void monitorize();
}
