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
				WritableSheet sheet = book.createSheet( " �ļ��嵥 " , 0 );
				CellView cellView = new CellView();  
				cellView.setAutosize(true); //�����Զ���С  
				sheet.setColumnView(0, cellView);//���������Զ������п� 
				sheet.setColumnView(1, cellView);//���������Զ������п� 
				sheet.setColumnView(2, cellView);//���������Զ������п� 
				sheet.setColumnView(3, cellView);//���������Զ������п� 
				sheet.setColumnView(4, cellView);//���������Զ������п� 
				sheet.setColumnView(5, cellView);//���������Զ������п� 
				sheet.setColumnView(6, cellView);//���������Զ������п� 
				sheet.setColumnView(7, cellView);//���������Զ������п� 
				WritableFont fontheader =new WritableFont(WritableFont.createFont("΢���ź�"), 10 ,WritableFont.BOLD);
				WritableCellFormat wcfHeader = new WritableCellFormat(fontheader);
				wcfHeader.setAlignment(Alignment.CENTRE);  //ƽ�о���
				wcfHeader.setVerticalAlignment(VerticalAlignment.CENTRE);  
				wcfHeader.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); 
				String[] header=CompareFileView.getInstance(null).getHeader();
				for(int w=0;w<header.length;w++){
					sheet.addCell(new Label(w,0, header[w],wcfHeader));
				}
				WritableFont fontData =new WritableFont(WritableFont.createFont("΢���ź�"), 10 ,WritableFont.NO_BOLD);
				WritableCellFormat wcfData = new WritableCellFormat(fontData);
				wcfData.setAlignment(Alignment.CENTRE);  //ƽ�о���
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
			Logger.getInstance().error("ExcelWriter.makeFileXls()����xls�ļ�["+path+"]�쳣��"+e.toString());
		}
	}

}
