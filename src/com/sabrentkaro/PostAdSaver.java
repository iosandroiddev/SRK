package com.sabrentkaro;

import android.content.Context;

public class PostAdSaver {

	private static PostAdSaver instance;
	private String mAddress = "";
	private String mAddressUser = "";

	private String city = "";
	private String userCity = "";

	private String state = "";
	private String userState = "";

	private String pincode = "";
	private String userPincode = "";

	private String mobNumber = "";
	private String userMobNumber = "";

	private String panCard = "";
	private String aadharname = "";
	private String aadharNumber = "";
	
	
	private boolean isProductAddressChecked =false;

	private boolean isEditing = false;

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmAddressUser() {
		return mAddressUser;
	}

	public void setmAddressUser(String mAddressUser) {
		this.mAddressUser = mAddressUser;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getUserPincode() {
		return userPincode;
	}

	public void setUserPincode(String userPincode) {
		this.userPincode = userPincode;
	}

	public String getMobNumber() {
		return mobNumber;
	}

	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}

	public String getUserMobNumber() {
		return userMobNumber;
	}

	public void setUserMobNumber(String userMobNumber) {
		this.userMobNumber = userMobNumber;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getAadharname() {
		return aadharname;
	}

	public void setAadharname(String aadharname) {
		this.aadharname = aadharname;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	private PostAdSaver(Context mContext) {
	}

	public static synchronized PostAdSaver getInstance(Context mContext) {
		if (instance == null) {
			instance = new PostAdSaver(mContext);
		}
		return instance;
	}

	public void setAddress(String address) {
		this.mAddress = address;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setUserAddress(String address) {
		this.mAddressUser = address;
	}

	public String getUserAddress() {
		return mAddressUser;
	}

	public boolean isProductAddressChecked() {
		return isProductAddressChecked;
	}

	public void setProductAddressChecked(boolean isProductAddressChecked) {
		this.isProductAddressChecked = isProductAddressChecked;
	}

}
