package com.example.dayplan;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	FrameLayout fl;
	LayoutInflater layoutInflater;
	Button b;
	Button b2;
	ArrayList<LinearLayout> llist=new ArrayList<LinearLayout>();
	ArrayList<Data> dlist=new ArrayList<Data>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		fl=(FrameLayout)findViewById(R.id.flayout);
		layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		for(int i=0; i<5; i++){
			dlist.add(new Data("レイアウト"+i, i));
		}
		
		init();
		
		b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Data d=new Data("レイアウト"+dlist.size(), 0);
				dlist.add(d);
				init();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	void addLayout(FrameLayout fl, Data data){
		LinearLayout l = (LinearLayout)layoutInflater.inflate(R.layout.item, null);
		//XMLで設定しても効かない、レイアウトのパラメータを設定しないと駄目。
		l.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT));
		llist.add(l);
		
		
		int mytime=data.time;
		TextView t=(TextView)l.findViewById(R.id.textView);
		t.setText(data.name+" time: "+mytime+" : 00 ~");
		
		fl.addView(l);
		
		//位置を設定
		MarginLayoutParams lparams = (MarginLayoutParams)l.getLayoutParams();
		lparams.leftMargin = 55;
		lparams.topMargin = (int)(data.time*(50/this.getResources().getDisplayMetrics().density))  ;
		l.setLayoutParams(lparams);
		
		//listenerの設定
		DragViewListener listener = new DragViewListener(this, l, data);
		l.setOnTouchListener(listener);
		
		l.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	//dlistのデータ数分レイアウトに追加。
	void init(){
		fl.removeAllViews();
		llist.clear();
		for(int i=0; i<dlist.size(); i++){
			addLayout(fl, dlist.get(i));
		}
	}
	
	class Data{
		String name;
		int time;
		int time2;
		
		Data(String name, int time){
			this.name=name;
			this.time=time;
			this.time2=time*50;
		}
	}
	
	
	public class DragViewListener implements OnTouchListener {
		Activity activity;
		LinearLayout dragView;
		Data data;
		private int oldx;
		private int oldy;
		private int top;
		private int downy;
		
		public DragViewListener(Activity activity, LinearLayout layout, Data data) {
			this.activity=activity;
			this.dragView=layout;
			this.data=data;
			MarginLayoutParams lp = (MarginLayoutParams)layout.getLayoutParams();
			oldx=dragView.getLeft() + lp.leftMargin;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			downy = (int) event.getRawY();
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				top = dragView.getTop() + (downy-oldy);
				new Thread(new Runnable(){
		            @Override
		            public void run() {
		                activity.runOnUiThread(new Runnable(){
		                    @Override
		                    public void run() {
		                    	dragView.layout(oldx, top, oldx + dragView.getWidth(), top+dragView.getHeight());
		                        dragView.invalidate();
		                    }
		                });
		            }

		        }).start();
				break;
			case MotionEvent.ACTION_UP:
				data.time=(int)(top/50);
				init();
				break;
			}
			
			oldy = downy;
			
			return false;
		}

		
	}

}
