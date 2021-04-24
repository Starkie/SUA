package sua.autonomouscar.controller;

import java.util.List;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.controller.interfaces.IMonitor;

/**
 * The controller class. Will act as a MAPE-K loop. Only the monitoring and analysis phases are implemented.
 * Every iteration monitorizes the measurements from the probes and executes those rules whose conditions evaluate to true.
 */
public class Controller {
	// The collection of monitors that collect the measurements from the probes.
	private List<IMonitor> monitors;

	// The collection of adaption rules that will be executed.
	private List<IAdaptionRule> rules;

	/**
	 * Creates a new instance of the {@link Controller} class.
	 * @param monitors The collection of monitors that collect the measurements from the probes.
	 * @param rules The collection of adaption rules that will be executed.
	 */
	public Controller(List<IMonitor> monitors, List<IAdaptionRule> rules)
	{
		this.monitors = monitors;
		this.rules = rules;
	}

	/**
	 * Executes an iteration of the control loop.
	 */
	public void execute()
	{
		// Execute the monitorize function for every monitor.
		monitors.stream()
			.forEach(m -> m.monitorize());

		// Execute those rules whose condition evaluate to true.
		rules.stream()
			.filter(r -> r.evaluateCondition())
			.forEach(r -> r.execute());
	}
}
