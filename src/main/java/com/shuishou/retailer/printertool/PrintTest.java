package com.shuishou.retailer.printertool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;

public class PrintTest {
	public static void main(String[] args){
		try {
			DriverPos pos = new DriverPos();
			String jsontemp = "/printtemplate/simple.json";
			File file = new File("G:/webspace-web/retailer/configs/printtemplate/simple.json");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String temp = null;
			while((temp = reader.readLine()) != null){
				sb.append(temp);
			}
			reader.close();
//			System.out.println(sb.toString().replace("	", ""));
			pos.print(sb.toString(), jsonParam(), "GP-L80180 Series");

		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static String jsonParam(){
		Map<String, Object> template = new HashMap<String, Object>();
		Map<String, Object> keys = new HashMap<String, Object>();
		String posResult = "点菜成功！1001 无此菜 本次点菜3/3份 2成功，1失败合计XX元祝您用餐愉快a";
		keys.put("title", "网络订单");
		keys.put("brandName", "智慧餐厅");
		keys.put("shopName", "天山店");
		keys.put("tableNumb", "0002");
		keys.put("tableName", "外卖1");
		keys.put("orderId", "1609101220001");
		keys.put("dateTime", "2016-09-10 12:21:00");
		keys.put("allPrice", "66.88");
		keys.put("barCode","7255");
		keys.put("path", "1.png");
		keys.put("remark", "免葱，免辣");
		
		if(!posResult.contains("成功")){
			if(posResult.contains("重单")){
				keys.put("warnTitle", "提    示");
				keys.put("warnMsg", "该订单已处理");
			}else{
				keys.put("warnTitle", "异常提示");
				keys.put("warnMsg", "自动下单失败请人工处理");
			}
		}else{
			if(posResult.contains("无此") || posResult.contains("沽清")
					|| posResult.contains("不存在")){
				keys.put("warnTitle", "异常提示");
				keys.put("warnMsg", "部分菜品未下成功请联系服务员人工处理");
			}
		}
		
		keys.put("posTitle", "收银软件下单结果");
		keys.put("posMsg", posResult);
		
		List<Map<String, Object>> goods = new ArrayList<Map<String,Object>>();
		Map<String, Object> good = new HashMap<String, Object>();
		good.put("code", "goods code");
		good.put("name", "goods name");
		good.put("quantity", "1.0");
		good.put("price", "goods price");
		good.put("qrcode", "1.png");
		good.put("remark", "免葱、免辣");
		
		Map<String, Object> good2 = new HashMap<String, Object>();
		good2.put("code", "goods 2");
		good2.put("name", "goods 2");
		good2.put("quantity", "1");
		good2.put("price", "2.3");
		good2.put("qrcode", "1.png");
		good2.put("remark", "remark remak");
		
		goods.add(good);
		goods.add(good2);
		
		
		template.put("goods", goods);
		template.put("keys", keys);
		String temp = JsonKit.toJson(template);
		System.out.println(temp);
		return temp;
	}
}
