package API_VootKids;

public class request {
	
	
	private String parentKS;
	private String deviceId;
	private String deviceBrand;
	private profile profile;
	private String childProfileId;
	
	public String getChildProfileId() {
		return childProfileId;
	}
	public void setChildProfileId(String childProfileId) {
		this.childProfileId = childProfileId;
	}
	public String getParentKS() {
		return parentKS;
	}
	public void setParentKS(String parentKS) {
		this.parentKS = parentKS;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceBrand() {
		return deviceBrand;
	}
	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}
	public profile getProfile() {
		return profile;
	}
	public void setProfile(profile profile) {
		this.profile = profile;
	}
	
	

}
