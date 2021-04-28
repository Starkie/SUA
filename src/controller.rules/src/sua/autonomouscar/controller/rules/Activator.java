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

        // Register the rule listeners to the properties it depends on.        
        String filter = createFilter(RoadContext.class, CurrentDrivingServiceStatus.class);
        context.addServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule, filter);
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    context.removeServiceListener(swithToL2AdaptiveCruiseControlFromL1Rule);
	    
	    this.swithToL2AdaptiveCruiseControlFromL1Rule = null;
	    
		Activator.context = null;
	}
	
	// TODO: Move to an utilities class.
	// Obtained from: https://gist.github.com/toelen/1370316
	private static String createFilter(Class<?>... interfaces) {
        StringBuilder builder = new StringBuilder();

        builder.append("( |");
        for (Class<?> clazz : interfaces) {
            builder.append("(objectclass=" + clazz.getName()
                    + ") ");
        }

        builder.append(" ) ");
        return builder.toString();
	}

}
