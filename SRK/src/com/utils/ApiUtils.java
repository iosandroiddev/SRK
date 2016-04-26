package com.utils;

import com.sabrentkaro.InternalApp;

public class ApiUtils {

	public static final String ONLINE_URL = "http://allrental.co.in/businessservices/api/";
	public static final String ONLINE_LIVE_URL = "https://www.sabrentkaro.com/businessservices/api/";

	public static final String BASE_URL = InternalApp.isProductionApi ? ONLINE_LIVE_URL
			: ONLINE_URL;

	public static final String GETALLPRODUCTS = BASE_URL
			+ "Product/GetAllProducts";

	public static final String GETCATEGORYMAPPINGS = BASE_URL
			+ "Product/GetCategoryMappings";

	public static final String GETCITY = BASE_URL + "Common/GetKeyPairs/city";

	public static final String FETCHSEARCHRESULTS = BASE_URL
			+ "Search/FetchSearchResults";

	public static final String FETCHSEARCHRESULTSFROMFILTER = BASE_URL
			+ "Search/FetchSearchResultsFromFilter";

	public static final String LOADADDETAILS = BASE_URL
			+ "Search/LoadAdDetails";

	public static final String POSTUSERINFORMATION = BASE_URL
			+ "User/PostUserInformation";

	public static final String POSTREGISTERUSER = BASE_URL
			+ "User/PostRegisterUser";

	public static final String POSTREGISTERUSERMOBILE = BASE_URL
			+ "User/PostRegisterUserMobile";

	public static final String CALCULATERENTALPRICING = BASE_URL
			+ "Rental/CalculateRentalPricing";

	public static final String GETADTEMPLATEFORCATEGORY = BASE_URL
			+ "Product/GetAdTemplateForCategory";

	public static final String UPLOADADCONTENT = BASE_URL
			+ "Product/UploadAdContent";
	
	public static final String SAVEUSERTPINFORMATION = BASE_URL
			+ "Product/SaveUserTpInformation";

	public static final String INITIATEAD = BASE_URL + "Product/InitiateAd";

	public static final String POSTANAD = BASE_URL + "Product/PostAnAd";

	public static final String GETEMAILFROMMOBILE = BASE_URL
			+ "User/GetEmailFromMobile/";

	public static final String GETADAVAILABILITYCALENDAR = BASE_URL
			+ "Rental/GetAdAvailabilityCalendar";

	public static final String POSTDEVICEENTRY = BASE_URL
			+ "Common/PostDeviceEntry";

	public static final String GETPROVIDERSERVICES = BASE_URL
			+ "Product/GetProviderServices";

	public static final String FINALIZERENTAL = BASE_URL
			+ "Rental/FinalizeRental";

	public static final String INITIATERENTAL = BASE_URL
			+ "Rental/InitiateRental";

	public static final String GETADDRESSLIST = BASE_URL
			+ "User/GetAddressList";
	
	public static final String SAVERENTALPAYMENT = BASE_URL
			+ "Rental/SaveRentalPayment";
}
