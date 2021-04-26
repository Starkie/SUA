package sua.autonomouscar.controller.listeners;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import sua.autonomouscar.controller.interfaces.IAdaptionRule;
import sua.autonomouscar.interaction.interfaces.INotificationService;

/**
 * A {@link ServiceListener} that listens to changes on the services of the type {@link INotificationService}.
 */
public class NotificationServiceServiceListener implements ServiceListener {
	
	private IAdaptionRule notificationServiceEnabled;
	private IAdaptionRule notificationServiceDisabled;

	@Override
	public void serviceChanged(ServiceEvent event) {
		switch (event.getType())
		{
			case ServiceEvent.REGISTERED:
			case ServiceEvent.MODIFIED:
				if (this.notificationServiceEnabled != null)
				{
					this.notificationServiceEnabled.evaluateAndExecute();					
				}
				
				break;
			case ServiceEvent.UNREGISTERING:
				if (this.notificationServiceDisabled != null)
				{
					this.notificationServiceDisabled.evaluateAndExecute();					
				}
				
			default:
				return;
		}
	}
	
	public void setNotificationServiceEnabled(IAdaptionRule notificationServiceEnabled) {
		this.notificationServiceEnabled = notificationServiceEnabled;
	}

	public void setNotificationServiceDisabled(IAdaptionRule notificationServiceDisabled) {
		this.notificationServiceDisabled = notificationServiceDisabled;
	}
}
