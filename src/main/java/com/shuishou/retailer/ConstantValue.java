package com.shuishou.retailer;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class ConstantValue {
	public static final DateFormat DFYMDHMS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final DateFormat DFHMS = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DFYMD = new SimpleDateFormat("yyyy/MM/dd");
	public static final DateFormat DFWEEK = new SimpleDateFormat("EEE");
	
	public static final String PERMISSION_QUERY_USER = "QUERY_USER";
	public static final String PERMISSION_CREATE_USER = "CREATE_USER";
	public static final String PERMISSION_UPDATE_GOODS = "UPDATE_GOODS";
	public static final String PERMISSION_QUERY_ORDER = "QUERY_ORDER";
	public static final String PERMISSION_UPDATE_ORDER = "UPDATE_ORDER";
	public static final String PERMISSION_CHANGE_CONFIG = "CHANGE_CONFIG";
	public static final String PERMISSION_UPDATE_DISCOUNTTEMPLATE = "UPDATE_DISCOUNTTEMPLATE";
	public static final String PERMISSION_QUERY_SHIFTWORK = "QUERY_SHIFTWORK";
	public static final String PERMISSION_UPDATE_PAYWAY = "UPDATE_PAYWAY";
	public static final String PERMISSION_STATISTICS = "STATISTICS";
	public static final String PERMISSION_QUERY_MEMBER = "QUERY_MEMBER";
	public static final String PERMISSION_UPDATE_MEMBER = "UPDATE_MEMBER";
	public static final String PERMISSION_UPDATE_MEMBERSCORE = "UPDATE_MEMBERSCORE";
	public static final String PERMISSION_UPDATE_MEMBERBALANCE = "UPDATE_MEMBERBALANCE";
	
	public final static String SPLITTAG_PERMISSION = ";";
	
	public final static int ADDGOODSTYPE_IMPORT = 1;
	public final static int ADDGOODSTYPE_REFUND = 2;
	
	public static final String CATEGORY_ERRORLOG = "errorlog";
	public static final String CATEGORY_PRINTTEMPLATE = "printtemplate";
	
	
	public static final byte INDENT_STATUS_OPEN = 1;
	public static final byte INDENT_STATUS_CLOSED = 2;
	public static final byte INDENT_STATUS_PAID = 3;
	public static final byte INDENT_STATUS_CANCELED = 4;
	public static final byte INDENT_STATUS_FORCEEND = 5;//强制清台
	
	public static final byte INDENT_OPERATIONTYPE_ADD = 1;
	public static final byte INDENT_OPERATIONTYPE_DELETE = 2;
	public static final byte INDENT_OPERATIONTYPE_CANCEL = 3;
	public static final byte INDENT_OPERATIONTYPE_PAY = 4;
	
	public static final byte INDENT_TYPE_ORDER = 1;//普通订单
	public static final byte INDENT_TYPE_REFUND = 2;//退货单
	public static final byte INDENT_TYPE_PREBUY = 3;//预购单
	
	//付款方式
	public static final String INDENT_PAYWAY_CASH = "cash";//现金
	public static final String INDENT_PAYWAY_BANKCARD = "bankcard";//刷卡
	public static final String INDENT_PAYWAY_MEMBER = "member";//会员
	
	public static final byte INDENTDETAIL_OPERATIONTYPE_DELETE = 2;
	public static final byte INDENTDETAIL_OPERATIONTYPE_CHANGEAMOUNT = 5;
	
	public static final byte MENUCHANGE_TYPE_SOLDOUT = 0;
	
	public static final byte PRINT_STYLE_TOGETHER = 0;
	public static final byte PRINT_STYLE_SEPARATELY = 1;
	public static final byte PRINTER_TYPE_COUNTER = 1;
	public static final byte PRINTER_TYPE_KITCHEN = 2;
	
	public static final String CONFIGS_BRANCHNAME= "BRANCHNAME";
	public static final String CONFIGS_MEMBERMGR_BYSCORE= "MEMBERMGR_BYSCORE";
	public static final String CONFIGS_MEMBERMGR_BYDEPOSIT = "MEMBERMGR_BYDEPOSIT";
	public static final String CONFIGS_MEMBERMGR_SCOREPERDOLLAR = "MEMBERMGR_SCOREPERDOLLAR";
	public static final String CONFIGS_OPENCASHDRAWERCODE = "OPENCASHDRAWERCODE";
	
	public static final int STATISTICS_DIMENSTION_PAYWAY = 1;
	public static final int STATISTICS_DIMENSTION_SELL = 2;
	public static final int STATISTICS_DIMENSTION_PERIODSELL = 3;
	
	public static final int STATISTICS_SELLGRANULARITY_BYDISH = 1;
	public static final int STATISTICS_SELLGRANULARITY_BYCATEGORY2 = 2;
	public static final int STATISTICS_SELLGRANULARITY_BYCATEGORY1 = 3;
	
	public static final int STATISTICS_PERIODSELL_PERDAY = 1;
	public static final int STATISTICS_PERIODSELL_PERHOUR = 2;
	
	public static final int MATERIAL_ALARMSTATUS_NOALARM = 1;
	public static final int MATERIAL_ALARMSTATUS_ALARMACCEPTED = 2;
	public static final int MATERIAL_ALARMSTATUS_ALARMDELAY = 3;
	
	public static final int INDENTTYPE_CONSUM = 1;
	public static final int INDENTTYPE_REFUND = 2;
}
