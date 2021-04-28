package sua.autonomouscar.controller.rules;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import sua.autonomouscar.controller.properties.CurrentDrivingServiceStatus;
import sua.autonomouscar.controller.properties.RoadContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

    private SwithToL2AdaptiveCruiseControlFromL1Rule swithToL2AdaptiveCruiseControlFromL1Rule;

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// Register the rule services.
        this.swithToL2AdaptiveCruiseControlFromL1Rule = new SwithToL2AdaptiveCruiseControlFromL1Rule(context);
        context.registerService(SwithToL2AdaptiveCruiseControlFromL1Rule.class, swithToL2AdaptiveCruiseControlFromL1Rule, null);

        // Register the rule listeners to the properties it depends on.
        String roadContextKnowledgePropertyListenerFilter= "(objectclass=" + RoadContext.class.getName() + ")";
        context.addServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule, roadContextKnowledgePropertyListenerFilter);
        
        String currentDrivingServiceKnowledgePropertyListenerFilter= "(objectclass=" + CurrentDrivingServiceStatus.class.getName() + ")";
        context.addServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule, currentDrivingServiceKnowledgePropertyListenerFilter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    context.removeServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule);
	    
	    this.swithToL2AdaptiveCruiseControlFromL1Rule = null;
	    
		Activator.context = null;
	}

}
