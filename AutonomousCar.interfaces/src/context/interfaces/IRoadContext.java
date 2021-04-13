package context.interfaces;

public interface IRoadContext {

	public ERoadType getType();
	public void setType(ERoadType type);
	
	public ERoadStatus getStatus();
	public void setStatus(ERoadStatus status);
}
