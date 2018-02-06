package com.shuishou.retailer.indent.services;

import java.util.Date;

import org.json.JSONArray;

import com.shuishou.retailer.DataCheckException;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;


public interface IIndentService {
	ObjectResult saveIndent(int userId, JSONArray jsonOrder, String payway, double paidPrice, double adjustPrice, String memberCard) throws DataCheckException;
	ObjectListResult queryIndent(int start, int limit, String sstarttime, String sendtime, String payway, String member, String indentCode, Integer[] types, String orderby, String orderbydesc);
	ObjectListResult queryPrebuyIndent(int start, int limit, String sstarttime, String sendtime, String member);
	ObjectListResult queryGoodsSoldRecord(int goodsId, String sstarttime, String sendtime, String payway, String member);
	ObjectListResult queryIndentForShiftwork(int shiftworkId);
	ObjectResult changePreOrderToOrder(int userId, int indentId) throws DataCheckException;
	ObjectResult deletePreOrder(int userId, int indentId);
	ObjectResult refundIndent(int userId, JSONArray jsonOrder, String memberCard, double paidPrice, double adjustPrice, boolean returnToStorage, String payWay) throws DataCheckException;
	ObjectResult prebuyIndent(int userId, JSONArray jsonOrder, String payway, double paidPrice, double adjustPrice, String memberCard, boolean paid);
}
