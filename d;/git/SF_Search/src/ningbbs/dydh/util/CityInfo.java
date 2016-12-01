package ningbbs.dydh.util;



public class CityInfo {
	private String parentID;
	private String id;
	private String name;

	public CityInfo(String parentID, String id, String name) {
		super();
		this.parentID = parentID;
		this.id = id;
		this.name = name;
	}
	public String getParentID() {
		return parentID;
	}
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CityInfo [parentID=" + parentID + ", id=" + id + ", name="
				+ name + "]";
	}
}
