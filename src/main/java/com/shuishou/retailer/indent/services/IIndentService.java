package com.shuishou.retailer.indent.services;

import java.util.Date;

import org.json.JSONArray;

import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;


public interface IIndentService {
	ObjectResult saveIndent(int userId, JSONArray jsonOrder, String payway, double paidPrice, String memberCard);
	ObjectListResult queryIndent(int start, int limit, String sstarttime, String sendtime, String payway, String member, String orderby, String orderbydesc);
	ObjectResult printIndent(int userId, int indentId);
	
	ObjectResult refundIndent(int userId, JSONArray jsonOrder, double refundPrice, String memberCard, boolean returnToStorage);
	ObjectResult prebuyIndent(int userId, JSONArray jsonOrder, String payway, double paidPrice, String memberCard, boolean paid);
}
