package com.example.client;

public class URLManager {
	public static final String REGISTER_GCM_URL = "http://****/chat/setup/register_gcm.php";

	public static final String CHECK_SERVER_URL = "http://****/chat/check/check_useable_server.php";
	public static final String CHECK_EXIST_FAVORITE_URL = "http://****/chat/check/check_exist_favorite.php";
	public static final String CHECK_EXIST_FAVORITE_HOTEL_URL = "http://****/chat/check/check_exist_favorite_hotel.php";
	public static final String CHECK_EXIST_FAVORITE_GOURMET_URL = "http://****/chat/check/check_exist_favorite_gourmet.php";

	public static final String SEND_MESSAGE_URL = "http://****/chat/message/send_message.php";
	public static final String EXTRACT_MESSAGE_URL = "http://****/chat/message/extract_keyword.php";
	public static final String EXTRACT_MESSAGE_NEW_URL = "http://****/chat/message/extract_keyword_new.php";

	public static final String SIGN_UP_URL = "http://****/chat/edit/edit_member.php";
	public static final String EDIT_USER_URL = "http://****/chat/edit/edit_member.php";
	public static final String EDIT_GUIDE_URL = "http://****/chat/edit/edit_guide.php";
	public static final String EDIT_GUIDE_MEMO_URL = "http://****/chat/edit/edit_guide_memo.php";
	public static final String ADD_FAVORITE_URL = "http://****/chat/edit/add_favorite.php";
	public static final String ADD_FAVORITE_URL_HOTEL = "http://****/chat/edit/add_favorite_hotel.php";
	public static final String ADD_FAVORITE_URL_GOURMET = "http://****/chat/edit/add_favorite_gourmet.php";
	public static final String REGISTER_ROUTE_URL = "http://****/chat/edit/register_route.php";
	public static final String REGISTER_GUIDE_URL = "http://****/chat/edit/register_guide.php";
	public static final String DELETE_ALL_FAVORITE_URL = "http://****/chat/edit/delete_favorite.php";
	public static final String DELETE_MY_GUIDE_URL = "http://****/chat/edit/delete_my_guide.php";
	public static final String DELETE_FAVORITE_SPOT_URL = "http://****/chat/edit/delete_favorite_spot.php";
	public static final String DELETE_FAVORITE_GOURMET_URL = "http://****/chat/edit/delete_favorite_gourmet.php";
	public static final String DELETE_FAVORITE_HOTEL_URL = "http://****/chat/edit/delete_favorite_hotel.php";
	public static final String DELETE_FAVORITE_SELECT_URL = "http://****/chat/edit/delete_select_favorite.php";
	public static final String DELETE_LOG_URL = "http://****/chat/edit/delete_log.php";

	public static final String GET_SPOT_URL = "http://****/chat/get/get_spot.php";
	public static final String GET_SEARCH_SPOT_URL = "http://****/chat/get/get_search_spot2.php";
	public static final String GET_SEARCH_SPOT_ALL_URL = "http://****/chat/get/get_search_spot.php";
	public static final String GET_SEARCH_GUIDE_URL = "http://****/chat/get/get_search_guide2.php";
	
	private static String GET_URL = "http://****/chat/get/";
	public static final String GET_FAVORITE_URL = GET_URL + "get_favorite_spot2.php";
	public static final String GET_FAVORITE_SPOT_URL = GET_URL + "get_favorite_spot2.php";
	public static final String GET_FAVORITE_HOTEL_URL = GET_URL + "get_favorite_hotel.php";
	public static final String GET_FAVORITE_GOURMET_URL = GET_URL + "get_favorite_gourmet.php";
	public static final String GET_VERSION_URL = GET_URL + "get_application_version.php";
	public static final String GET_MEMBER_URL = GET_URL + "get_member.php";
	public static final String GET_GUIDE_ID_URL = GET_URL + "get_guide_id.php";
	public static final String GET_GUIDE_VALUE_URL = GET_URL + "get_guide_value.php";
	public static final String GET_GUIDE_DAY_URL = GET_URL + "get_guide_day_id.php";
	public static final String GET_ROUTE_URL = GET_URL + "get_route.php";
}
