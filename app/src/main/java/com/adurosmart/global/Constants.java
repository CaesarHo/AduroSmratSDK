package com.adurosmart.global;

public class Constants {
	// handle message
	public static final int KEYBOARD_SHOW_FILTER_USER = 0x11;
	public static final String PACKAGE_NAME = "com.adurosmart.";
	public static final String FORGET_PASSWORD_URL = "http://www.cloudlinks.cn/pw/";
	public static final String CACHE_FOLDER_NAME = "adurosmart";

	public static class Action {
		/*
		 * globel
		 */
		public final static String ACTION_LISTEN_UDP_DATA = PACKAGE_NAME + "ACTION_LISTEN_UDP_DATA";
		public static final String CLOSE_INPUT_DIALOG = PACKAGE_NAME + "CLOSE_INPUT_DIALOG";
		public static final String NET_WORK_TYPE_CHANGE = PACKAGE_NAME + "NET_WORK_TYPE_CHANGE";

		/*
		 * mainactivity
		 */
		public final static String ACTION_SWITCH_USER = PACKAGE_NAME + "ACTION_SWITCH_USER";
		public final static String ACTION_EXIT = PACKAGE_NAME + "ACTION_EXIT";
		public static final String RECEIVE_MSG = PACKAGE_NAME + "RECEIVE_MSG";
		public final static String ACTION_UPDATE = PACKAGE_NAME + "ACTION_UPDATE";
		public static final String RECEIVE_SYS_MSG = PACKAGE_NAME + "RECEIVE_SYS_MSG";
		public static final String SESSION_ID_ERROR = PACKAGE_NAME + "SESSION_ID_ERROR";

		/*
		 * contact
		 */
		public final static String REFRESH_CONTANTS = PACKAGE_NAME + "refresh.contants";
		public final static String ACTION_REFRESH_NEARLY_TELL = PACKAGE_NAME + "ACTION_REFRESH_NEARLY_TELL";
		public final static String GET_FRIENDS_STATE = PACKAGE_NAME + "GET_FRIENDS_STATE";
		public final static String ACTION_COUNTRY_CHOOSE = PACKAGE_NAME + "ACTION_COUNTRY_CHOOSE";
		public final static String ADD_CONTACT_SUCCESS = PACKAGE_NAME + "ADD_CONTACT_SUCCESS";
		public final static String LOCAL_DEVICE_SEARCH_END = PACKAGE_NAME + "LOCAL_DEVICE_SEARCH_END";
		public final static String IS_OPEN_THREAD_CONTACTFRAG = PACKAGE_NAME + "IS_OPEN_THREAD_CONTACTFRAG";
		/*
		 * MainControl
		 */
		public static final String CHECK_DEVICE_PWD = PACKAGE_NAME + "CHECK_DEVICE_PWD";

		/*
		 * sysset
		 */
		public static final String ADD_ALARM_MASK_ID_SUCCESS = PACKAGE_NAME + "ADD_ALARM_MASK_ID_SUCCESS";
		public static final String REFRESH_ALARM_RECORD = PACKAGE_NAME + "REFRESH_ALARM_RECORD";
		public static final String CHANGE_ALARM_MESSAGE = PACKAGE_NAME + "CHANGE_ALARM_MESSAGE";

		/*
		 * shake
		 */
		public static final String UPDATE_DEVICE_FALG = PACKAGE_NAME + "UPDATE_DEVICE_FALG";

		/*
		 * MainLogin
		 */
		public static final String REPLACE_EMAIL_LOGIN = PACKAGE_NAME + "REPLACE_EMAIL_LOGIN";
		public static final String REPLACE_PHONE_LOGIN = PACKAGE_NAME + "REPLACE_PHONE_LOGIN";
		public static final String EMAIL_LOGIN = PACKAGE_NAME + "EMAIL_LOGIN";
		public static final String PHONE_LOGIN = PACKAGE_NAME + "PHONE_LOGIN";

		/*
		 * Control
		 */
		public static final String CONTROL_SETTING_PWD_ERROR = PACKAGE_NAME + "CONTROL_SETTING_PWD_ERROR";

		public static final String REPLACE_REMOTE_CONTROL = PACKAGE_NAME + "REPLACE_REMOTE_CONTROL";
		public static final String REPLACE_MAIN_CONTROL = PACKAGE_NAME + "REPLACE_MAIN_CONTROL";
		public static final String REPLACE_SETTING_TIME = PACKAGE_NAME + "REPLACE_SETTING_TIME";
		public static final String REPLACE_ALARM_CONTROL = PACKAGE_NAME + "REPLACE_ALARM_CONTROL";
		public static final String REPLACE_VIDEO_CONTROL = PACKAGE_NAME + "REPLACE_VIDEO_CONTROL";
		public static final String REPLACE_RECORD_CONTROL = PACKAGE_NAME + "REPLACE_RECORD_CONTROL";
		public static final String REPLACE_SECURITY_CONTROL = PACKAGE_NAME + "REPLACE_SECURITY_CONTROL";
		public static final String REPLACE_NET_CONTROL = PACKAGE_NAME + "REPLACE_NET_CONTROL";
		public static final String REPLACE_DEFENCE_AREA_CONTROL = PACKAGE_NAME + "REPLACE_DEFENCE_AREA_CONTROL";
		public static final String REPLACE_SD_CARD_CONTROL = PACKAGE_NAME + "REPLACE_SD_CARD_CONTROL";
		public static final String REPLACE_LANGUAGE_CONTROL = PACKAGE_NAME + "REPLACE_LANGUAGE_CONTROL";
		public static final String CONTROL_BACK = PACKAGE_NAME + "CONTROL_BACK";
		// 智能钥匙连接
		public static final String CURRENT_WIFI_NAME = PACKAGE_NAME + "CURRENT_WIFI_NAME";
		public static final String HEARED = PACKAGE_NAME + "HEARED";
		public static final String DELETE_DEVICE_ALL = PACKAGE_NAME + "DELETE_DEVICE_ALL";
		public static final String INSERT_INFRARED_BACK = PACKAGE_NAME + "INSERT_INFRARED_BACK";
		public static final String ADD_SUCESS = PACKAGE_NAME + "ADD_SUCESS";
		public static final String SETTING_WIFI_SUCCESS = PACKAGE_NAME + "SETTING_WIFI_SUCCESS";
		public static final String ACTIVITY_FINISH = PACKAGE_NAME + "ACTIVITY_FINISH";
		public static final String REPEAT_LOADING_DATA = PACKAGE_NAME + "REPEAT_LOADING_DATA";
		// add
		public static final String DIAPPEAR_ADD = PACKAGE_NAME + "DIAPPEAR_ADD";
		public static final String RADAR_SET_WIFI_SUCCESS = PACKAGE_NAME + "RADAR_SET_WIFI_SUCCESS";
		public static final String RADAR_SET_WIFI_FAILED = PACKAGE_NAME + "RADAR_SET_WIFI_FAILED";
		public static final String MONITOR_NEWDEVICEALARMING = PACKAGE_NAME + "MONITOR_NEWDEVICEALARMING";
		public static final String SEARCH_AP_DEVICE = PACKAGE_NAME + "SEARCH_AP_DEVICE";
		public static final String SET_AP_DEVICE_WIFI_PWD = PACKAGE_NAME + "SET_AP_DEVICE_WIFI_PWD";
		public static final String SEARCH_AP_APMODE = PACKAGE_NAME + "SEARCH_AP_APMODE";
		public static final String SEARCH_AP_ADDAPDEVICE = PACKAGE_NAME + "SEARCH_AP_ADDAPDEVICE";
		public static final String SEARCH_AP = PACKAGE_NAME + "SEARCH_AP";
		public static final String SEARCH_AP_QUITEAPDEVICE = PACKAGE_NAME + "SEARCH_AP_QUITEAPDEVICE";
		public static final String EXITE_AP_MODE = PACKAGE_NAME + "EXITE_AP_MODE";
		
		public static final String ENTER_DEVICE_SETTING=PACKAGE_NAME+"ENTER_DEVICE_SETTING";
		public static final String CALL_DEVICE=PACKAGE_NAME+"CALL_DEVICE";
		
		public static final String GET_DEVICE_TYPE=PACKAGE_NAME+"GET_DEVICE_TYPE";
	}

	public static class DeviceState {
		public static final int ONLINE = 1;
		public static final int OFFLINE = 0;
	}

	public static class DefenceState {
		public static final int DEFENCE_STATE_OFF = 0;
		public static final int DEFENCE_STATE_ON = 1;
		public static final int DEFENCE_STATE_LOADING = 2;
		public static final int DEFENCE_STATE_WARNING_PWD = 3;
		public static final int DEFENCE_STATE_WARNING_NET = 4;
		public static final int DEFENCE_NO_PERMISSION = 5;
	}

	public static class DeviceFlag {
		public static final int UNSET_PASSWORD = 0;
		public static final int ALREADY_SET_PASSWORD = 1;
		public static final int AP_MODE = 2;
		public static final int UNKNOW = 3;
	}

	public static class ReplaceStyle {
		public static final int SLIDE = 0;
		public static final int FADE = 1;
	}

	public static class LoginType {
		public static final int PHONE = 0;
		public static final int EMAIL = 1;
	}

	public static class RegisterType {
		public static final int PHONE = 0;
		public static final int EMALL = 1;
	}

	public static class ModifyAccountType {
		public static final int MODIFY_EMAIL = 0;
	}

	public static class SettingConfig {

		public static final int SETTING_CLICK_TIME_DELAY = 0;

	}

	public static class MessageType {
		public static final int SEND_SUCCESS = 0;
		public static final int SENDING = 1;
		public static final int SEND_FAULT = 2;
		public static final int UNREAD = 3;
		public static final int READED = 4;
	}

	public static class ActivityInfo {
		public static final int ACTIVITY_LOGOACTIVITY = 0;
		public static final int ACTIVITY_MAINACTIVITY = 1;
		public static final int ACTIVITY_LOGINACTIVITY = 2;
		public static final int ACTIVITY_IMAGEBROWSER = 3;
		public static final int ACTIVITY_ABOUTACTIVITY = 4;
		public static final int ACTIVITY_ACCOUNTINFOACTIVITY = 5;
		public static final int ACTIVITY_ADDALARMMASKIDACTIVITY = 6;
		public static final int ACTIVITY_ADDCONTACTACTIVITY = 7;
		public static final int ACTIVITY_ADDCONTACTNEXTACTIVITY = 8;
		public static final int ACTIVITY_ALARMRECORDACTIVITY = 9;
		public static final int ACTIVITY_ALARMSETACTIVITY = 10;
		public static final int ACTIVITY_FORWARDACTIVITY = 11;
		public static final int ACTIVITY_MAINCONTROLACTIVITY = 12;
		public static final int ACTIVITY_MESSAGEACTIVITY = 13;
		public static final int ACTIVITY_MODIFYACCOUNTEMAILACTIVITY = 14;
		public static final int ACTIVITY_MODIFYACCOUNTPHONEACTIVITY = 15;
		public static final int ACTIVITY_MODIFYACCOUNTPHONEACTIVITY2 = 16;
		public static final int ACTIVITY_MODIFYALARMIDACTIVITY = 17;
		public static final int ACTIVITY_MODIFYBOUNDEMAILACTIVITY = 18;
		public static final int ACTIVITY_MODIFYCONTACTACTIVITY = 19;
		public static final int ACTIVITY_MODIFNPCPASSWORDACTIVITY = 20;
		public static final int ACTIVITY_REGISTERACTIVITY = 21;
		public static final int ACTIVITY_REGISTERACTIVITY2 = 22;
		public static final int ACTIVITY_SEARCHLISTACTIVITY = 23;
		public static final int ACTIVITY_SETTINGBELLRINGACTIVITY = 24;
		public static final int ACTIVITY_SETTINGSDBELLACTIVITY = 25;
		public static final int ACTIVITY_SETTINGSYSTEMACTIVITY = 26;
		public static final int ACTIVITY_SYSMSGACTIVITY = 27;
		public static final int ACTIVITY_SYSNOTIFYACTIVITY = 28;
		public static final int ACTIVITY_TELLDETAILACTIVITY = 29;
		public static final int ACTIVITY_UNBINDPHONEACTIVITY = 30;
		public static final int ACTIVITY_VERIFYPHONEACTIVITY = 31;
		public static final int ACTIVITY_PLAYBACKLISTACTIVITY = 32;
		public static final int ACTIVITY_PLAYBACKACTIVITY = 33;
		public static final int ACTIVITY_VIDEOACTIVITY = 34;
		public static final int ACTIVITY_MONITORACTIVITY = 35;
		public static final int ACTIVITY_CALLACTIVITY = 36;
		public static final int ACTIVITY_MODIFYLOGINPASSWORDACTIVITY = 37;
		public static final int ACTIVITY_CUTIMAGEACTIVITY = 38;
		public static final int ACTIVITY_FORWARDDOWNACTIVITY = 39;
		public static final int ACTIVITY_DEVICE_UPDATE = 40;
		public static final int ACTIVITY_SHAKE = 41;
		public static final int ACTIVITY_INFRARED_REMOTE = 42;
		public static final int ACTIVITY_INFRARED_SET_WIFI = 43;
		public static final int ACTIVITY_LOCAL_DEVICE_LIST = 44;
		public static final int ACTIVITY_ALTOGETHERREGISTERACTIVITY = 45;
		public static final int ACTIVITY_QRCODE = 46;
		public static final int ACTIVITY_MODIFY_NPC_VISITOR_PASSWORD_ACTIVITY = 47;
		public static final int ACTIVITY_THREEADDCONTACTACTIVITY = 48;
		public static final int SMARTKEYSETWIFIACTIVITY = 49;
		public static final int WIFILISTACTIVITY = 50;
		public static final int ACTIVITY_QRCODEACTIVITY = 51;
		public static final int ACTIVITY_CREATEQRCODEACTIVITY = 52;
		public static final int ACTIVITY_ALARMPUSHACCOUNTACTIVITY = 53;
		public static final int ACTIVITY_IMAGESEE = 54;
		public static final int ACTIVITY_RADARADDFIRSTACTIVITY = 55;
		public static final int ACTIVITY_RARDARADDACTIVITY = 56;
		public static final int ACTIVITY_WAITDEVICENETWORKACTIVITY = 57;
		public static final int ACTIVITY_ADDWAITACTIVITY = 58;
		public static final int ACTIVITY_ADDAPDEVICEACTIVITY = 59;
		public static final int ACTIVITY_APDEVICEMONITORACTIVITY = 60;
		public static final int ACTIVITY_WAITTING = 61;
		public static final int ACTIVITY_MODIFYAPWIFIPWD = 62;
	}

	public static class ActivityStatus {
		public static final int STATUS_START = 0;
		public static final int STATUS_STOP = 1;
	}

	public static class Image {
		public static final String USER_HEADER_PATH = "/sdcard/"
				+ CACHE_FOLDER_NAME + "/";
		public static final String USER_HEADER_TEMP_FILE_NAME = "temp";
		public static final float USER_HEADER_ROUND_SCALE = 1f / 32f;
		public static final int USER_HEADER_MIN_SIZE = 32;
		public static final String USER_HEADER_FILE_NAME = "header";
		public static final String USER_GRAY_HEADER_FILE_NAME = "header_gray";
	}

	public static class Update {
		public static final String SAVE_PATH = Constants.CACHE_FOLDER_NAME
				+ "/apk";
		public static final String FILE_NAME = Constants.CACHE_FOLDER_NAME
				+ ".apk";
		public static final String INSTALL_APK = "application/vnd.android.package-archive";
		public static final int CHECK_UPDATE_HANDLE_FALSE = 0x11;
		public static final int CHECK_UPDATE_HANDLE_TRUE = 0x12;
	}

	public static final int USER_HEADER_WIDTH_HEIGHT = 500;

	public static class Languege {
		public static final int LANG_EN = 0; // 0 默认 英文
		public static final int LANG_CHS = 1; // 1 中文简体
		public static final int LANG_JP = 2; // 2 日语
		public static final int LANG_PT = 3; // 3 葡萄牙
		public static final int LANG_SP = 4; // 4西班牙
		public static final int LANG_CF = 5; // 5 中文繁体
		public static final int LANG_FR = 6; // 6 法语
		public static final int LANG_RU = 7; // 7 俄语
	}
	public static class FOCUS{
		public static final byte FOCUS_BIG=0x1;
		public static final byte FOCUS_SMALL=0x11;
	}
	public static class CHANGE_FOCUS_ZOOM{
		public static final int CHANGE_NO=0;
        public static final int CHANGE_ZOOM=1;
		public static final int CHANGE_FOCUS=2;
		public static final int CHANGE_FOCUS_ZOOM=3;
	}
}
