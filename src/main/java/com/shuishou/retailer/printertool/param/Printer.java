package com.shuishou.retailer.printertool.param;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.PrinterName;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

/**

 * 驱动打印

 * @author zhulinfeng

 * @时间 2016年9月22日上午11:11:22

 *

 */
public class Printer implements Printable {
	private Logger logger = Logger.getLogger(Printer.class);
	private static ArrayList<Font> fontList = new ArrayList<>();
	private PrintPager printPager;
	
	/**
	 * 把要打印的内容转化到行集里, 凑够一页就定义成一个集合, 这个是页的集合
	 */
	private ArrayList<ArrayList<PrintItem>> pageslist = new ArrayList<>();
			
	public Printer() {
	}
	
	/**

	 * 打印任务

	 * @param printPager	页面对象

	 * @param printerName	打印机名称(Windows控制面板-->设备和打印机-->打印机名称) 支持共享打印机

	 */
	public void printJob(PrintPager printPager, String printerName){
		try{
			if(printPager==null)return;
			int printSize = 1;
			
			this.printPager = printPager;
			final PrinterJob pj = PrinterJob.getPrinterJob();//创建一个打印任务
			
	        PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
	        Paper paper = pf.getPaper();
	        paper.setSize(printPager.pagerWidth, printPager.pagerHeight);
	        paper.setImageableArea(printPager.offsetX, printPager.offsetY, printPager.pagerWidth, printPager.pagerHeight);
	        pf.setPaper(paper);
			HashAttributeSet hs = new HashAttributeSet();
			hs.add(new PrinterName(printerName, null));
			// 获取打印服务对象
			PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
			PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, hs);
			if (printService.length > 0) {
				PrintService ps = printService[0];
				pj.setPrintService(ps);
				Media[] res = (Media[]) ps.getSupportedAttributeValues(Media.class, null, null);
				
				for (Media media : res) {
				    if (media instanceof MediaTray){
				    	MediaTray tray = (MediaTray)media;
				    	//TODO: 使用佳博打印机, 有source选项Document[Cut], 不清楚其他打印机是不是相同. 使用该选项, 到文档结束才会切纸.
				    	if (tray.toString().equals("Document[Cut]")){
				    		System.out.println(media + ", value = " + tray.getValue() + ", name = "+ tray.getName());
				    		attr_set.add(tray);
				    	}
				    }
				}
			}
			
	        pj.setPrintable(this, pf);
	        for (int i = 0; i < printSize; i++) {
				pj.print(attr_set); 
			}
		} catch (Exception e){
			e.printStackTrace();
			logger.error("打印异常", e);
		}
	}
	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D g2 = (Graphics2D)graphics;
		g2.setColor(Color.black);
		int offSetY = printPager.offsetY;
		int offSetX = printPager.offsetX;
		g2.translate(offSetX, offSetY);
		if (pageslist.isEmpty()){
			//处理内容项

			printBody(g2, pageFormat, offSetX, offSetY);
		}
		
		if (pageIndex>=pageslist.size()) 
			return NO_SUCH_PAGE;
		
		float[] dash = { 2.0f };
		// 设置打印线的属性。虚线="线+缺口+线+缺口+线+缺口……" 

		// 1.线宽 2.不同的线端 3.当两条线连接时，连接处的形状 4.缺口的宽度(默认10.0f) 5.虚线的宽度 6.偏移量

		g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
		
		ArrayList<PrintItem> printitemList = pageslist.get(pageIndex);
		for (int j = 0; j < printitemList.size(); j++) {
			PrintItem item = printitemList.get(j);
			g2.setFont(item.font);
			g2.drawString(item.s, item.x, item.y);
		}
		return PAGE_EXISTS;
	}
	
	/**

	 * 

	 * @param g2			画笔

	 * @param pageFormat	页面

	 * @param offSetX		起始坐标x

	 * @param offSetY		起始坐标y

	 * @return

	 */
	private int printBody(Graphics2D g2, PageFormat pageFormat,int offSetX, int offSetY){
		
		ArrayList<PrintItem> itemInPageList = new ArrayList<>();
		
		List<_PagerBody> list = printPager.list;
		
		float[] dash = { 2.0f };
		// 设置打印线的属性。虚线="线+缺口+线+缺口+线+缺口……" 

		// 1.线宽 2.不同的线端 3.当两条线连接时，连接处的形状 4.缺口的宽度(默认10.0f) 5.虚线的宽度 6.偏移量

		g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash, 0.0f));
		
		if (list!=null && list.size()>0) {
			
			for (_PagerBody body : list) {
				if (body.getImg()!=null) {
					PagerImages qrcode = body.getImg();
					if (StrKit.notBlank(qrcode.getPath())) {
						ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(PathKit.getWebRootPath()+"/img/"+qrcode.getPath()));
						int drawWidth = (int)pageFormat.getImageableWidth();
						if (body.getAlign().equals(PrintAlignment.center)) {
							offSetX = (drawWidth-qrcode.getWidth())/2;
						} else if (body.getAlign().equals(PrintAlignment.right)) {
							offSetX = drawWidth-body.getImg().getWidth() - 20;
						}
						g2.drawImage(icon.getImage(), offSetX, offSetY, qrcode.getWidth(), qrcode.getHeight(), icon.getImageObserver());
					}
					if (body.isFeeLine()) {
						offSetX = printPager.offsetX;
						// 5=行间距

						offSetY+=qrcode.getHeight() + 5;
					}else{
						offSetX+=qrcode.getWidth()+printPager.offsetX;
						offSetY+=qrcode.getHeight()/2 + 5;
					}
					continue;
				}
				//设置字体

//				Font font = new Font(printPager.fontFamily,body.getFontStyle(),body.getFontSize());
				Font font = getFontFromStore(printPager.fontFamily,body.getFontStyle(),body.getFontSize());
				g2.setFont(font);
				//字体高度  

				float heigth = font.getSize2D();
				String str = body.getContent();
				
				//文本宽度

				int strWidth = g2.getFontMetrics().stringWidth(str);
				
				if (body.getAlign().equals(PrintAlignment.center)) {
					offSetX += (printPager.pagerWidth - strWidth)/2;
				} else if (body.getAlign().equals(PrintAlignment.right)) {
					offSetX += printPager.pagerWidth - strWidth;
				}
				
				String[] content = getChangeStr(g2.getFontMetrics(), str, printPager.pagerWidth, strWidth);
				for (String string : content) {
					//绘制文本

//					g2.drawString(string, offSetX, offSetY+5);
					PrintItem item = new PrintItem(string, offSetX, offSetY + 5, font);
					itemInPageList.add(item);
					if (offSetY > printPager.pagerHeight){
						pageslist.add(itemInPageList);
						itemInPageList = new ArrayList<>();
						offSetY = 0;
					}
					//换行

					if(content.length>1){
						offSetX = printPager.offsetX;
						offSetY += heigth;
					}
				}
				if (body.isFeeLine()) {
					offSetX = printPager.offsetX;
					offSetY += heigth;
				}else{
					offSetX += strWidth;
				}
			}
			//绘制虚线

			//x1,y1起始位置 x2,y2结束位置

//			g2.drawLine(printPager.offsetX, offSetY, printPager.pagerWidth, offSetY);

		}
		if (!itemInPageList.isEmpty()){
			pageslist.add(itemInPageList);
		}
		return offSetY;
	}
	
	private Font getFontFromStore(String name, int style, int size){
		for(Font f : fontList){
			if (f.getName().equals(name) && f.getStyle() == style && f.getSize() == size){
				return f;
			}
		}
		Font f = new Font(name, style, size);
		fontList.add(f);
		return f;
	}
	
	/**

	 * 

	 * @param metrics		字体属性

	 * @param str			要打印的文本

	 * @param pageWidth		打印页面宽度

	 * @param strWidth		文本在page里的宽度--不同字体大小宽度不一，使用metrics.stringWidth(str)获取

	 * @return

	 */
	private static String[] getChangeStr(FontMetrics metrics, String str, int pageWidth, int strWidth){
//		int StrPixelWidth = strWidth; // 字符串长度（像素） str要打印的字符串

		int lineSize = (int) Math.ceil(strWidth * 1.0 / pageWidth);// 要多少行

		lineSize = lineSize==0?1:lineSize;
		// 存储换行之后每一行的字符串

		String tempStrs[] = new String[lineSize];
		if (pageWidth < strWidth) {// 页面宽度（width）小于 字符串长度

			StringBuilder sb = new StringBuilder();// 存储每一行的字符串

			int j = 0;
			int tempStart = 0;
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				sb.append(ch);
				int tempStrPi1exlWi1dth = metrics.stringWidth(sb.toString());
				if (tempStrPi1exlWi1dth > pageWidth) {
					tempStrs[j++] = str.substring(tempStart, i);
					tempStart = i;
					sb.delete(0, sb.length());
					sb.append(ch);
				}
				if (i == str.length() - 1) {// 最后一行

					tempStrs[j] = str.substring(tempStart);
				}
			}
		}else{
			tempStrs[0] = str==null ? " " : str;
		}
		return tempStrs;
	}
}

class PrintItem{
	public String s;
	public int x;
	public int y;
	public Font font;
	public PrintItem(String s, int x, int y, Font font){
		this.s = s;
		this.x = x;
		this.y = y;
		this.font = font;
	}
}
