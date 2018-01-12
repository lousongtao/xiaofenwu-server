package com.shuishou.retailer.importtool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.shuishou.retailer.ConstantValue;

public class MainFrameImportMember extends JFrame {

	private JTextField tfFileName = new JTextField();
	private JFileChooser fc = new JFileChooser();
	private JButton btnSubmit = new JButton("Submit");
	private JButton btnChoose = new JButton("Choose");
	private JLabel lbStatus = new JLabel();
	
	private List<String> sqls = new ArrayList<String>();
	public MainFrameImportMember(){
		initUI();
		
	}
	
	private void executeDB() throws Exception{
		File file = new File("dbproperties.properties");
		Reader reader = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			reader = new FileReader(file);
			Properties ps = new Properties();
			ps.load(reader);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ps.getProperty("db"), ps.getProperty("username"), ps.getProperty("password"));
			stmt = conn.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				System.out.println(sqls.get(i));
				stmt.execute(sqls.get(i));
				lbStatus.setText("execute " + (i+1) + "/" + sqls.size() +" sentences...");
			}
		} catch (Exception e1) {
			throw e1;
		} finally{
			try {
				if (reader != null)
					reader.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void initUI(){
		tfFileName.setEditable(false);
		lbStatus.setSize(100, 25);
		lbStatus.setText("status bar...");
		JPanel pContent = new JPanel(new GridBagLayout());
		pContent.add(tfFileName, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
		pContent.add(btnChoose, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 20), 0, 0));
		pContent.add(btnSubmit, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
		
		pContent.setBorder(BorderFactory.createLineBorder(Color.red));
		tfFileName.setBorder(BorderFactory.createLineBorder(Color.green));
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(pContent, BorderLayout.CENTER);
		c.add(lbStatus, BorderLayout.SOUTH);
		
		btnChoose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fc.setMultiSelectionEnabled(false);
				fc.setFileFilter(new ImportFileFilter("xls"));
				fc.setFileFilter(new ImportFileFilter("xlsx"));
				fc.showDialog(MainFrameImportMember.this, "Choose");
				File file = fc.getSelectedFile();
				if (file != null)
					tfFileName.setText(file.getAbsolutePath());
			}
		});
		
		btnSubmit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doSubmit();
				
			}}
		);
	}
	
	private void doSubmit(){
		File file = fc.getSelectedFile();
		if (file == null)
			return;
		try {
			readFileBuildSQL();
			executeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void readFileBuildSQL() throws Exception{
		if (fc.getSelectedFile() == null)
			return;
		String now = ConstantValue.DFYMDHMS.format(new Date());
		sqls.clear();
		sqls.add("delete from member_consumption;");
		sqls.add("delete from member_score;");
		sqls.add("delete from member;");
		Workbook wb = null;
		try {
			StringBuffer sbSql = new StringBuffer();
			wb = WorkbookFactory.create(fc.getSelectedFile());
			HSSFSheet sheetC1 = (HSSFSheet) wb.getSheetAt(0);
			for (int i = 1; i < sheetC1.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheetC1.getRow(i);
				sbSql.delete(0, sbSql.length());
				sbSql.append("insert into member(id, createTime, discountRate, lastModifyTime, memberCard, name, score, telephone) values (");
				sbSql.append(i + ",");//id
				sbSql.append("'" + transcateSpecialChar(row.getCell(7).getStringCellValue()) + "',");//create time
				sbSql.append((Double.parseDouble(row.getCell(3).getStringCellValue()) / 100) + ",");//discountRate
				sbSql.append("'" + now +"',");//lastModifyTime
				sbSql.append("'"+row.getCell(0).getStringCellValue() +"',");//memberCard
				sbSql.append("'" + transcateSpecialChar(row.getCell(1).getStringCellValue()) +"',");//name
				sbSql.append(Double.parseDouble(row.getCell(2).getStringCellValue()) +",");//score
				sbSql.append("'" + row.getCell(5).getStringCellValue() +"');");//telephone
				sqls.add(sbSql.toString());
			}
			
			sqls.add("commit");
		} catch (Exception e) {
			throw e;
		} finally{
			if (wb != null)
				wb.close();
		}
	}
	
	private String transcateSpecialChar(String s){
		String news = s;
		if (s.indexOf("'") >= 0){
			news = news.replaceAll("'", "''");
		}
		if (s.indexOf("\"") >= 0){
			news = news.replaceAll("\"", "\\\"");
		}
		return news;
	}				
	
	public static void main(String[] args){
		MainFrameImportMember f = new MainFrameImportMember();
		f.setSize(new Dimension(300, 300));
		f.setResizable(false);
		f.setLocation(500, 500);
		f.setTitle("Import Member Tool");
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setVisible(true);
		
	}
}
