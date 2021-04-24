package sua.autonomouscar.controller.interfaces;

/**
 * The interface of a probe. Used to measure parameters of the controlled system or its context.
 *
 * @param <T> The type of the measurement obtained.
 */
public interface IProbe<T> {
	/**
	 * Returns a measurement of the probe.
	 * @return The obtained measurement, if any.
	 */
	T getMeasurement();
}
