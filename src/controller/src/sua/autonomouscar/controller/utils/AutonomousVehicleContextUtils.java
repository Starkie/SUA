package sua.autonomouscar.controller.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.driving.interfaces.IDrivingService;

/**
 * Class with utility methods to interact with the autonomous vehicle OSGi context.
 */
public class AutonomousVehicleContextUtils {
	
	/**
	 * Searches for the {@link IDrivingService} that is currently active in the vehicle.
	 * @param context The autonomous vehicle OSGi context.
	 * @return The currently active driving service, if found. Otherwise returns null.
	 */
	public static IDrivingService findCurrentDrivingService(BundleContext context)
	{
		// Get all the registered IDrivingService references.
		Collection<ServiceReference<IDrivingService>> drivingServiceReferences = null;
		
		try {
			drivingServiceReferences = context.getServiceReferences(IDrivingService.class, null);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
			
			return null;
		}
		
		// Resolve all the service references.
		Collection<IDrivingService> existingDrivingServices = drivingServiceReferences
				.stream()
				.map(sr -> context.getService(sr))
				.collect(Collectors.toUnmodifiableList());
		
		// Find the active driving service.
		IDrivingService currentDrivingService = existingDrivingServices.stream()
				.filter(ds -> ds != null && ds.isDriving())
				.findFirst()
				.orElseGet(() -> null);
		
		return currentDrivingService;
	}
}
