package com.shuishou.retailer.printertool.param;

import java.awt.Font;

public class _PagerBody {

	//打印内容 
	private String content = "";
	// 字体大小(默认:9) 
	private int fontSize = 9;
	// 是否走纸(默认:否) 
	private boolean feeLine = true;
	// 字体样式(默认:平滑) 
	private int fontStyle = Font.PLAIN;
	// 打印位置(默认:left) 
	private PrintAlignment align = PrintAlignment.left;
	// 图片对象 
	private PagerImages img;

	public _PagerBody() {
	}

	public PagerImages getImg() {
		return img;
	}

	public _PagerBody setImg(PagerImages img) {
		this.img = img;
		return this;
	}

	public String getContent() {
		return content;
	}

	public int getFontSize() {
		return fontSize;
	}


	public int getFontStyle() {
		return fontStyle;
	}

	public _PagerBody setContent(String content) {
		this.content = content;
		return this;
	}

	public _PagerBody setFontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	private _PagerBody setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
		return this;
	}

	public _PagerBody isBold(boolean bold) {
		if (bold) {
			setFontStyle(Font.BOLD);
		}
		return this;
	}

	public PrintAlignment getAlign() {
		return align;
	}

	public _PagerBody setAlign(int align) {
		switch (align) {
		case 0:
			this.align = PrintAlignment.left;
			break;
		case 1:
			this.align = PrintAlignment.center;
			break;
		case 2:
			this.align = PrintAlignment.right;
			break;

		default:
			this.align = PrintAlignment.left;
			break;
		}
		
		return this;
	}

	public boolean isFeeLine() {
		return feeLine;
	}

	public _PagerBody setFeeLine(boolean feeLine) {
		this.feeLine = feeLine;
		return this;
	}

}