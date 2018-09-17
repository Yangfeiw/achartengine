package com.example.administrator.achartengine;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RtChartsActivity extends Activity {
	
	private Timer timer = new Timer();
    private GraphicalView chart;
    private TextView textview;
    private TimerTask task;
    private int addY = -1;
	private long addX;
	/**��������*/
    private static final int SERIES_NR=1;
    private static final String TAG = "message";
    private TimeSeries series1;
    private XYMultipleSeriesDataset dataset1;
    private Handler handler;
    private Random random=new Random();
    
    /**ʱ������*/
    Date[] xcache = new Date[20];
	/**����*/
    int[] ycache = new int[20];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rtchart);
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayout);
        //����ͼ��
		chart = ChartFactory.getTimeChartView(this, getDateDemoDataset(), getDemoRenderer(), "hh:mm:ss");
		layout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT,380));
		//ΪTextView����¼�
		textview = (TextView)findViewById(R.id.myview);
		textview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(RtChartsActivity.this, "ceshiview", 1).show();
				Intent intent = new Intent();
				intent.setClass(RtChartsActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		handler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		//ˢ��ͼ��
        		updateChart();
        		super.handleMessage(msg);
        	}
        };
        task = new TimerTask() {
        	@Override
        	public void run() {
        		Message message = new Message();
        	    message.what = 200;
        	    handler.sendMessage(message);
        	}
        };
        timer.schedule(task, 2*1000,1000);
	}
	
	   private void updateChart() {
		    //�趨����Ϊ20
		    int length = series1.getItemCount();
		    if(length>=20) length = 20;
		    addY=random.nextInt()%10;
		    addX=new Date().getTime();
		    
		    //��ǰ��ĵ���뻺��
			for (int i = 0; i < length; i++) {
				xcache[i] =  new Date((long)series1.getX(i));
				ycache[i] = (int) series1.getY(i);
			}
		    
			series1.clear();
			//���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
			//�����������һ�°�˳��ߵ�������ʲôЧ������������ѭ���壬������²����ĵ�
			series1.add(new Date(addX), addY);
			for (int k = 0; k < length; k++) {
	    		series1.add(xcache[k], ycache[k]);
	    	}
			//�����ݼ�������µĵ㼯
			dataset1.removeSeries(series1);
			dataset1.addSeries(series1);
			//���߸���
			chart.invalidate();
	    }
	/**
	 * �趨�����ʽ
	 * @return
	 */
	   private XYMultipleSeriesRenderer getDemoRenderer() {
		    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setChartTitle("ʵʱ����");//����
		    renderer.setChartTitleTextSize(20);
		    renderer.setXTitle("ʱ��");    //x��˵��
		    renderer.setAxisTitleTextSize(16);
		    renderer.setAxesColor(Color.BLACK);
		    renderer.setLabelsTextSize(15);    //����̶������С
		    renderer.setLabelsColor(Color.BLACK);
		    renderer.setLegendTextSize(15);    //����˵��
		    renderer.setXLabelsColor(Color.BLACK);
		    renderer.setYLabelsColor(0, Color.BLACK);
		    renderer.setShowLegend(false);
		    renderer.setMargins(new int[] {20, 30, 100, 0});
		    XYSeriesRenderer r = new XYSeriesRenderer();
		    r.setColor(Color.BLUE);
		    r.setChartValuesTextSize(15);
		    r.setChartValuesSpacing(3);
		    r.setPointStyle(PointStyle.CIRCLE);
		    r.setFillBelowLine(true);
		    r.setFillBelowLineColor(Color.WHITE);
		    r.setFillPoints(true);
		    renderer.addSeriesRenderer(r);
		    renderer.setMarginsColor(Color.WHITE);
		    renderer.setPanEnabled(false,false);
		    renderer.setShowGrid(true);
		    renderer.setYAxisMax(50);
		    renderer.setYAxisMin(-30);
		    renderer.setInScroll(true);  //������С
		    return renderer;
		  }
	   /**
	    * ���ݶ���
	    * @return
	    */
	   private XYMultipleSeriesDataset getDateDemoDataset() {
		    dataset1 = new XYMultipleSeriesDataset();
		    final int nr = 10;
		    long value = new Date().getTime();
		    Random r = new Random();
		    for (int i = 0; i < SERIES_NR; i++) {
		      series1 = new TimeSeries("Demo series " + (i + 1));
		      for (int k = 0; k < nr; k++) {
		        series1.add(new Date(value+k*1000), 20 +r.nextInt() % 10);
		      }
		      dataset1.addSeries(series1);
		    }
		    Log.i(TAG, dataset1.toString());
		    return dataset1;
		  }
	    @Override
	    public void onDestroy() {
	    	//����������ʱ�ص�Timer
	    	timer.cancel();
	    	super.onDestroy();
	    };
}
