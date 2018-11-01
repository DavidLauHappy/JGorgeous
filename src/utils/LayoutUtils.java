package utils;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class LayoutUtils {

	public static GridData getMainLayoutData()
    {
    	   GridData gd_main=new GridData();
    	   gd_main.horizontalAlignment=GridData.FILL;
    	   gd_main.verticalAlignment=GridData.FILL;
    	   gd_main.grabExcessHorizontalSpace=true;
    	   gd_main.grabExcessVerticalSpace=true;
    	   gd_main.widthHint=860;
    	   gd_main.heightHint=600;
    	
		   return gd_main;
    }
	
	public static GridLayout getShellLayout()
	{
		GridLayout layout_grid=new GridLayout();
		layout_grid.numColumns=1;
		layout_grid.verticalSpacing=3;
		layout_grid.makeColumnsEqualWidth=true;
		return layout_grid;
	}
	
	//������ռ���20��ͼ��
	public static GridLayout getToolBarLayout()
	{
		GridLayout grid_row=new GridLayout();
		grid_row.makeColumnsEqualWidth=true;
		grid_row.numColumns=20;
		grid_row.horizontalSpacing=10;
		grid_row.verticalSpacing=10;
		return grid_row;
	}
	
	public static GridData getToolsLayoutData()
	{
		GridData griddata_row=new GridData();
		griddata_row.horizontalAlignment=GridData.FILL;
		griddata_row.verticalAlignment=GridData.FILL;
		//griddata_row.grabExcessVerticalSpace=true;
		griddata_row.widthHint=64;
		griddata_row.heightHint=64;
		//��ֱ�����ϲ��ֵ����
		griddata_row.verticalIndent=-5;
		return griddata_row;
	}
	
	
	
	public static GridLayout getWorkAreaLayout()
	{
		GridLayout layout_work=new GridLayout();
		layout_work.numColumns=1;
		layout_work.makeColumnsEqualWidth=true;
		return layout_work;
	}
	
	 public static GridData getWorkAreaLayoutData()
	    {
	    	   GridData gd_work=new GridData();
			   gd_work.horizontalAlignment=GridData.FILL;
			   gd_work.verticalAlignment=GridData.FILL;
			   gd_work.grabExcessHorizontalSpace=true;
			   gd_work.grabExcessVerticalSpace=true;
			   return gd_work;
	    }
	 
	  public static GridData getSourceViewLayoutData()
	    {
	    	   GridData gd_sourceview=new GridData();
	    	   gd_sourceview.horizontalAlignment=GridData.FILL;
	    	   gd_sourceview.verticalAlignment=GridData.FILL;
	    	   gd_sourceview.grabExcessHorizontalSpace=true;
	    	   gd_sourceview.grabExcessVerticalSpace=true;
			   return gd_sourceview;
	    }
	  
	   //��߹������ڵĲ���
		public static GridLayout getSourceViewLayout(ScrolledComposite sc)
		{
			GridLayout layout_sourceview=new GridLayout();
			layout_sourceview.numColumns=1;
			layout_sourceview.makeColumnsEqualWidth=true;
			
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			sc.setAlwaysShowScrollBars(true);
			return layout_sourceview;
		}
		
		 //�༭����Ĳ���
	    public static GridData getEidtViewLayoutData()
	    {
	    	   GridData gd_edit=new GridData();
	    	   gd_edit.horizontalAlignment=GridData.FILL;
	    	   gd_edit.verticalAlignment=GridData.FILL;
	    	   gd_edit.grabExcessHorizontalSpace=true;
	    	   gd_edit.grabExcessVerticalSpace=true;
	    	   gd_edit.grabExcessVerticalSpace=true;
			   return gd_edit;
	    }

	    public static GridLayout getEditViewLayout()//(ScrolledComposite sc)
		{
			GridLayout layout_edit=new GridLayout();
			layout_edit.numColumns=1;
			layout_edit.makeColumnsEqualWidth=true;
			//sc.setExpandHorizontal(true);
			//sc.setExpandVertical(true);
			//sc.setAlwaysShowScrollBars(true);
			return layout_edit;
		}
	    
		public static FillLayout getTabFloderLayout(CTabFolder tabFolder)
		{
			FillLayout layout_tab=new FillLayout();
			//tabFolder
			tabFolder.setMaximizeVisible(true);
			tabFolder.setMinimizeVisible(true);
			tabFolder.setTabHeight(20);
			return layout_tab;
		}
		
		public static GridData getCommomGridData()
	    {
			GridData gd_com=new GridData();
			gd_com.horizontalAlignment=GridData.FILL;
			gd_com.verticalAlignment=GridData.FILL;
			gd_com.grabExcessHorizontalSpace=true;
			gd_com.grabExcessVerticalSpace=true;
	      return  gd_com;
	    }
		
		public static GridLayout getDiagramViewLayout(ScrolledComposite sc)
		{
			GridLayout layout_diagramview=new GridLayout();
			layout_diagramview.numColumns=1;
			layout_diagramview.makeColumnsEqualWidth=true;
			
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			sc.setAlwaysShowScrollBars(true);
			return layout_diagramview;
		}
		//�����в��ֹ���
		 public static GridLayout getCommandViewLayout()
			{
				GridLayout layout_edit=new GridLayout();
				layout_edit.numColumns=1;
				layout_edit.makeColumnsEqualWidth=true;
				//sc.setExpandHorizontal(true);
				//sc.setExpandVertical(true);
				//sc.setAlwaysShowScrollBars(true);
				return layout_edit;
			}
		 
		  public static GridData getCommandViewLayoutData()
		    {
		    	   GridData gd_edit=new GridData();
		    	   gd_edit.horizontalAlignment=GridData.FILL;
		    	   gd_edit.verticalAlignment=GridData.FILL;
		    	   gd_edit.grabExcessHorizontalSpace=true;
		    	   gd_edit.grabExcessVerticalSpace=true;
		    	   gd_edit.grabExcessVerticalSpace=true;
				   return gd_edit;
		    }
		  
		  public static GridLayout getDeployLayout()
			{
				GridLayout layout_treeCom=new GridLayout();
				layout_treeCom.numColumns=1;
				layout_treeCom.makeColumnsEqualWidth=true;
				return layout_treeCom;
			}
		  
		//���õ�G���ֹ�������
			public static GridData getComGridData(int h_align,int v_align,boolean h_grap,boolean v_grap,int h_span,int v_span,int defaultWidth,int defaultHeight)
		    {
				GridData gd_com=new GridData();
				gd_com.horizontalAlignment=h_align;//ˮƽ���뷽ʽ
				gd_com.verticalAlignment=v_align;//��ֱ���뷽ʽ
				gd_com.horizontalSpan=h_span;//ˮƽռ�ݵ�Ԫ����Ŀ
				gd_com.verticalSpan=v_span;//��ֱռ�ݵ�Ԫ����Ŀ
				if(defaultWidth!=0)
				{
				   gd_com.widthHint=defaultWidth;//�ؼ���ʼ���
				}
				if(defaultHeight!=0)
				{
				   gd_com.heightHint=defaultHeight;//�ؼ���ʼ�߶�
				}
				gd_com.grabExcessHorizontalSpace=h_grap;//ˮƽ�����Ƿ�Ŵ�
				gd_com.grabExcessVerticalSpace=v_grap;//��ֱ�����Ƿ�Ŵ�
		      return  gd_com;
		    }

			//���õ�G���ֹ���
			public static GridLayout getComGridLayout(int columns,int h_spacing){
				GridLayout gridLayout=new GridLayout();
				gridLayout.makeColumnsEqualWidth=true;
				gridLayout.numColumns=columns;
				if(h_spacing==0){
					gridLayout.horizontalSpacing=10;
				}
				else{
					gridLayout.horizontalSpacing=h_spacing;
					gridLayout.verticalSpacing=h_spacing;
				}
				return gridLayout;
			}	
}
