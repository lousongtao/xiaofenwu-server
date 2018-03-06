package com.shuishou.retailer;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class ConstantValue {
	public static final DateFormat DFYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DFHMS = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DFYMD = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat DFWEEK = new SimpleDateFormat("EEE");
	public static final DateFormat DFYMDHMS_2 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static final String DATE_PATTERN_YMD = "yyyy-MM-dd";
	public static final String DATE_PATTERN_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String FORMAT_DOUBLE = "%.2f";
	
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
	public static final String PERMISSION_UPDATE_PACKAGEBIND = "UPDATE_PACKAGEBIND";
	
	public final static String SPLITTAG_PERMISSION = ";";
	
	public final static int ADDGOODSTYPE_IMPORT = 1;
	public final static int ADDGOODSTYPE_REFUND = 2;
	
	public static final String CATEGORY_ERRORLOG = "errorlog";
	public static final String CATEGORY_PRINTTEMPLATE = "printtemplate";
	
	public static final int INDENT_TYPE_ORDER = 1;//普通订单
	public static final int INDENT_TYPE_REFUND = 2;//退货单
	public static final int INDENT_TYPE_PREBUY_PAID = 3;//预购单已付款
	public static final int INDENT_TYPE_PREBUY_UNPAID = 4;//预购单未付款
	public static final int INDENT_TYPE_PREBUY_FINISHED = 5;//预购单完结,已经转为订单
	public static final int INDENT_TYPE_ORDER_FROMPREBUY = 6;//预购单完结,已经转为订单
	
	//付款方式
	public static final String INDENT_PAYWAY_CASH = "cash";//现金
	public static final String INDENT_PAYWAY_BANKCARD = "bankcard";//刷卡
	public static final String INDENT_PAYWAY_MEMBER = "member";//会员
	
	public static final String CONFIGS_BRANCHNAME= "BRANCHNAME";
	public static final String CONFIGS_MEMBERMGR_BYSCORE= "MEMBERMGR_BYSCORE";
	public static final String CONFIGS_MEMBERMGR_BYDEPOSIT = "MEMBERMGR_BYDEPOSIT";
	public static final String CONFIGS_MEMBERMGR_SCOREPERDOLLAR = "MEMBERMGR_SCOREPERDOLLAR";
	public static final String CONFIGS_OPENCASHDRAWERCODE = "OPENCASHDRAWERCODE";
	
	public static final int STATISTICS_DIMENSTION_PAYWAY = 1;
	public static final int STATISTICS_DIMENSTION_SELL = 2;
	public static final int STATISTICS_DIMENSTION_PERIODSELL = 3;
	
	public static final int STATISTICS_SELLGRANULARITY_BYGOODS = 1;
	public static final int STATISTICS_SELLGRANULARITY_BYCATEGORY2 = 2;
	public static final int STATISTICS_SELLGRANULARITY_BYCATEGORY1 = 3;
	
	public static final int STATISTICS_PERIODSELL_PERDAY = 1;
	public static final int STATISTICS_PERIODSELL_PERHOUR = 2;
	
	public static final int MATERIAL_ALARMSTATUS_NOALARM = 1;
	public static final int MATERIAL_ALARMSTATUS_ALARMACCEPTED = 2;
	public static final int MATERIAL_ALARMSTATUS_ALARMDELAY = 3;
	
	public static final int MEMBERSCORE_CONSUM = 1;//积分类型-消费
	public static final int MEMBERSCORE_REFUND = 2;//积分类型-退货
	public static final int MEMBERSCORE_ADJUST = 3;//积分类型-调整
	public static final int MEMBERDEPOSIT_CONSUM = 1;//消费余额类型-消费
	public static final int MEMBERDEPOSIT_REFUND = 2;//消费余额类型-退款
	public static final int MEMBERDEPOSIT_RECHARGE = 3;//消费余额类型-充值
	public static final int MEMBERDEPOSIT_ADJUST = 4;//消费余额类型-调整
}
