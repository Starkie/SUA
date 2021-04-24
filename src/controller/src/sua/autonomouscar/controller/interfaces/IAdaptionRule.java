package sua.autonomouscar.controller.interfaces;

/**
 * Represents an adaption rule of the adaptive system. They are usually divided in 2 parts:
 * 	1. Condition: A condition based on one or more adaptation properties. If the condition evaluates to true, the rule can be executed.
 * 	2. Body: Reconfigures the system depending on the type of the rule.
 */
public interface IAdaptionRule {
	/**
	 * Evaluates the condition of the rule. Returns a value indicating whether the rule should be executed or not.
	 * @return True if the rule can be executed, otherwise, false.
	 */
	boolean evaluateCondition();
	
	/**
	 * Executes the body of the rule.
	 */
	void execute();
}
