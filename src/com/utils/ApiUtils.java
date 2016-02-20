package com.utils;

public class ApiUtils {

	public static final String ONLINE_URL = "http://allrental.co.in/businessservices/api/";
	public static final String BASE_URL = ONLINE_URL;

	public static final String GETALLPRODUCTS = BASE_URL
			+ "Product/GetAllProducts";

	public static final String GETCATEGORYMAPPINGS = BASE_URL
			+ "Product/GetCategoryMappings";

	public static final String GETCITY = BASE_URL + "Common/GetKeyPairs/city";

	public static final String FETCHSEARCHRESULTS = BASE_URL
			+ "Search/FetchSearchResults";

	public static final String LOADADDETAILS = BASE_URL
			+ "Search/LoadAdDetails";

	public static final String POSTUSERINFORMATION = BASE_URL
			+ "User/PostUserInformation";

	public static final String POSTREGISTERUSER = BASE_URL
			+ "User/PostRegisterUser";

	public static final String CALCULATERENTALPRICING = BASE_URL
			+ "Rental/CalculateRentalPricing";

	public static final String GETADTEMPLATEFORCATEGORY = BASE_URL
			+ "Product/GetAdTemplateForCategory";

	public static final String UPLOADADCONTENT = BASE_URL
			+ "Product/UploadAdContent";

	public static final String INITIATEAD = BASE_URL + "Product/InitiateAd";

	public static final String POSTANAD = BASE_URL + "Product/PostAnAd";

}