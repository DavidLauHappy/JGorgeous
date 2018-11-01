package business.deploy.core;


import java.io.File;
import java.util.List;

import business.deploy.bean.CompareData;
import business.deploy.figures.CompareFileView;

import resource.Logger;
import utils.StringUtil;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelWriter {
	public static void makeFileXls(String path){
		try{
				WritableWorkbook book = Workbook.createWorkbook( new File(path));
				WritableSheet sheet = book.createSheet( " 文件清单 " , 0 );
				CellView cellView = new CellView();  
				cellView.setAutosize(true); //设置自动大小  
				sheet.setColumnView(0, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(1, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(2, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(3, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(4, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(5, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(6, cellView);//根据内容自动设置列宽 
				sheet.setColumnView(7, cellView);//根据内容自动设置列宽 
				WritableFont fontheader =new WritableFont(WritableFont.createFont("微软雅黑"), 10 ,WritableFont.BOLD);
				WritableCellFormat wcfHeader = new WritableCellFormat(fontheader);
				wcfHeader.setAlignment(Alignment.CENTRE);  //平行居中
				wcfHeader.setVerticalAlignment(VerticalAlignment.CENTRE);  
				wcfHeader.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
				String[] header=CompareFileView.getInstance(null).getHeader();
				for(int w=0;w<header.length;w++){
					sheet.addCell(new Label(w,0, header[w],wcfHeader));
				}
				WritableFont fontData =new WritableFont(WritableFont.createFont("微软雅黑"), 10 ,WritableFont.NO_BOLD);
				WritableCellFormat wcfData = new WritableCellFormat(fontData);
				wcfData.setAlignment(Alignment.CENTRE);  //平行居中
				wcfData.setVerticalAlignment(VerticalAlignment.CENTRE);  
				wcfData.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
				List<CompareData> data=CompareFileView.getInstance(null).getFileList();
				if(data!=null){
					for(int i=0;i<data.size();i++){
						//bean.getVersionID(),bean.getFileName(),bean.getGroupName(),bean.getNodeName(),
						//bean.getMD5(),bean.getInstallMd5(),bean.getResult(),bean.getIp()});
						sheet.addCell(new Label( 0 , (i+1) ,data.get(i).getVersionID(),wcfData));
						sheet.addCell(new Label( 1 , (i+1) ,data.get(i).getFileName(),wcfData));
						sheet.addCell(new Label( 2 , (i+1) ,data.get(i).getGroupName(),wcfData));
						sheet.addCell(new Label( 3 , (i+1) ,data.get(i).getNodeName(),wcfData));
						sheet.addCell(new Label( 4 , (i+1) ,data.get(i).getMD5(),wcfData));
						sheet.addCell(new Label( 5 , (i+1) ,data.get(i).getInstallMd5(),wcfData));
						sheet.addCell(new Label( 6 , (i+1) ,data.get(i).getResult(),wcfData));
						sheet.addCell(new Label( 7 , (i+1) ,data.get(i).getIp(),wcfData));
					}
				}
				book.write();
				book.close();
		}
		catch(Exception e){
			Logger.getInstance().error("ExcelWriter.makeFileXls()生成xls文件["+path+"]异常："+e.toString());
		}
	}

}
