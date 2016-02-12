package com.models;

import java.io.Serializable;

public class SearchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String adId;
	private String adTitle;
	private String adDescription;
	private String coverImagePath;
	private String category;
	private String productCategory;
	private String location;
	private String brand;
	private String pricePerDay;
	private String pricePerWeek;
	private String pricePerMonth;
	private String yearOfPurchase;
	private String productCondition;

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public String getAdDescription() {
		return adDescription;
	}

	public void setAdDescription(String adDescription) {
		this.adDescription = adDescription;
	}

	public String getCoverImagePath() {
		return coverImagePath;
	}

	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(String pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getPricePerWeek() {
		return pricePerWeek;
	}

	public void setPricePerWeek(String pricePerWeek) {
		this.pricePerWeek = pricePerWeek;
	}

	public String getPricePerMonth() {
		return pricePerMonth;
	}

	public void setPricePerMonth(String pricePerMonth) {
		this.pricePerMonth = pricePerMonth;
	}

	public String getYearOfPurchase() {
		return yearOfPurchase;
	}

	public void setYearOfPurchase(String yearOfPurchase) {
		this.yearOfPurchase = yearOfPurchase;
	}

	public String getProductCondition() {
		return productCondition;
	}

	public void setProductCondition(String productCondition) {
		this.productCondition = productCondition;
	}

}
