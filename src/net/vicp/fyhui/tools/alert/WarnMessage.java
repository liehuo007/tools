package net.vicp.fyhui.tools.alert;

/**
 * 未使用
 * @author Administrator
 *
 */
public class WarnMessage {

	private String id;
	private int level;
	private String levelName;
	private String message;
	
	public WarnMessage() {
		
	}

	public WarnMessage(String id, int level, String levelName, String message) {
		super();
		this.id = id;
		this.level = level;
		this.levelName = levelName;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
