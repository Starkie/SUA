package sua.autonomouscar.controller.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import sua.autonomouscar.devices.interfaces.IDistanceSensor;
import sua.autonomouscar.devices.interfaces.ILineSensor;
import sua.autonomouscar.driving.interfaces.IDrivingService;
import sua.autonomouscar.infrastructure.OSGiUtils;
import sua.autonomouscar.infrastructure.driving.DrivingService;
import sua.autonomouscar.interfaces.IIdentifiable;

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
		IDrivingService currentDrivingService = OSGiUtils.getService(context, IDrivingService.class, String.format("(%s=%s)", DrivingService.ACTIVE, true));

		return currentDrivingService;
	}

	/**
	 * Searches for the best {@link IDistanceSensor} in the given {@link DistanceSensorPositon} that is currently active in the vehicle.
	 * @param context The autonomous vehicle OSGi context.
	 * @param position The position of the sensor to find.
	 * @return The best available distance sensor in the given position, if found. Otherwise, returns null.
	 */
	public static IDistanceSensor findDistanceSensor(BundleContext context, DistanceSensorPositon position) {
		// Always prioritize the normal distance sensor before the LIDAR.
		IDistanceSensor distanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, position.getNormalSensorId()));

		if (distanceSensor == null)
		{
			distanceSensor = OSGiUtils.getService(context, IDistanceSensor.class, String.format("(%s=%s)", IIdentifiable.ID, position.getLidarSensorId()));
		}

		return distanceSensor;
	}

	/**
	 * Searches for the {@link ILineSensor} in the given {@link LineSensorPositon} that is currently active in the vehicle.
	 * @param context The autonomous vehicle OSGi context.
	 * @param position The position of the sensor to find.
	 * @return The active line sensor in the given position, if found. Otherwise, returns null.
	 */
	public static ILineSensor findLineSensor(BundleContext context, LineSensorPosition position) {
		return OSGiUtils.getService(context, ILineSensor.class, String.format("(%s=%s)", IIdentifiable.ID, position.getSensorId()));
	}
}
