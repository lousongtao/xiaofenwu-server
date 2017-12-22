package com.shuishou.retailer.printertool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.shuishou.retailer.ConstantValue;

/**
 * 
 * 
 * 
 * @author zhulinfeng
 * 
 * @时间 2016年9月23日下午1:00:10
 *
 * 
 * 
 */
@Component
public class PrintThread implements InitializingBean{
	private Logger logger = Logger.getLogger(PrintThread.class);
	

	public void afterPropertiesSet() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						PrintJob printJob = PrintQueue.take();

						logger.debug("begin print job... Time : " + ConstantValue.DFYMDHMS.format(new Date()) 
							+", PrinterName:" + printJob.getPrinterName() 
							+ ", Param : " + printJob.getParam()
							+ ", Template : " + printJob.getTemplateFile());
						new DriverPos().print(readTxt(printJob.getTemplateFile(), "utf-8"),
								JsonKit.toJson(printJob.getParam()), printJob.getPrinterName());
						logger.debug("end print job...");
					} catch (InterruptedException e) {
						logger.error("Print Error", e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("Print Error", e);
						e.printStackTrace();
					}
				}
			}
		}, "driverPosPrint_thread").start();
	}

	/**
	 * 
	 * 根据任务类型选择打印模板
	 * 
	 * @param missionType
	 *            任务类型
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 */
	private static String jsonTemplateByPrintType(int printType) throws IOException {
		String template = "";
		String path = PathKit.getRootClassPath() + "/driverpos/";
		String pix = ".json";
		switch (printType) {
		case 0:
			template = path + "simple" + pix;
			break;
		case 1:
			template = path + "preOrder" + pix;
			break;
		case 2:
			template = path + "pay" + pix;
			break;
		case 3:
			template = path + "warn" + pix;
			break;
		case 4:
			template = path + "test" + pix;
			break;

		default:
			template = path + "simple" + pix;
			break;
		}
		String json = readTxt(template, "utf-8");

		return json;
	}

	private static String readTxt(String filePathAndName, String encoding) throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data);
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
			if (st != null && st.length() > 1)
				st = st.substring(0, st.length());

			isr.close();
			br.close();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}
}