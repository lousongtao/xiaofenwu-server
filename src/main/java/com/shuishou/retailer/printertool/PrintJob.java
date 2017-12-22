package com.shuishou.retailer.printertool;

import java.util.Map;

/**

 * 打印任务

 * @author zhulinfeng

 * @时间 2016年9月23日下午12:59:57

 *

 */
public class PrintJob {

	/**

	 * 任务类型：0=print together, 1=print separately

	 */
	private String templateFile;
	
	/**

	 * 需要打印的参数--可扩展

	 */
	private Map<String, Object> param;
	
	/**

	 * 打印机逻辑名称

	 */
	private String printerName;
	
	public PrintJob(String templateFile, Map<String, Object> param, String printerName) {
		this.templateFile = templateFile;
		this.param = param;
		this.printerName = printerName;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

}