package pt.ist.cm.game.dto;

import java.util.List;
import java.util.Map;

import pt.ist.cm.game.NetworkEventListener;
import pt.ist.cm.game.ball.Ball;

public class SystemStateDTO extends MDPongDTO {

	private String id;

	private transient String ipAddress;

	private Map<String, String> currentStatus;

	private final List<Ball> balls;

	public SystemStateDTO(String id, String ipAddress,
			Map<String, String> currentStatus, List<Ball> balls) {
		super();
		this.id = id;
		this.ipAddress = ipAddress;
		this.currentStatus = currentStatus;
		this.balls = balls;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Map<String, String> getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Map<String, String> currentStatus) {
		this.currentStatus = currentStatus;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	@Override
	public void visit(NetworkEventListener listener) {
		listener.systemState(this);
	}

}
