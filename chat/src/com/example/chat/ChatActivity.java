package com.example.chat;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class ChatActivity extends Activity {
	public static final String MY_ACTION_UPDATE = "UPDATE_UI_ACTION";
	
	private String registrationId = "";
	
	public Context context;
	private LinearLayout sendButton;
    private EditText inputText;
    private TextView infoText;
    private ListView chatlistView;
    private View rDrawerView;
    private LinearLayout openRDrawerButton;
    private LinearLayout developerLayout;
    private DrawerLayout drawerLayout;
    
    private TextView rDrawerHotelButton;
    private TextView rDrawerSpotButton;
    private TextView rDrawerGourmetButton;
    private TextView rDrawerSpotButton2;
    
    private TextView rDrawerMyGuideButton;
    private TextView rDrawerGuideButton;
    private TextView rDrawerFavoriteButton;
    private TextView rDrawerClearLogButton;
    private TextView rDrawerMenuButton;
    
    private SQLiteDatabase db;
    private SharedPreferencesHelper spHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy':'MM':'dd':'kk':'mm':'ss':'", Locale.JAPANESE);
    
    private ChatAdapter chatAdapter;
    private ArrayList<Chat> chatlist = new ArrayList<Chat>();
    private ArrayList<Member> memberList = new ArrayList<Member>();
    
    private UpdateBroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private BitmapCache iconCache; 
    private int iconSize;
    
    private boolean isDeveloper = false;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		getActionBar().hide();
		context = this;
			
		iconCache = new BitmapCache();
		iconSize = ImageLoadTask.getPxFromDp(context, 48);
		
		SQLiteOpenHelper helper = new SQLiteHelper(getApplicationContext(), "my.db", null, 1);
		db = helper.getWritableDatabase();
		spHelper = new SharedPreferencesHelper(this);
		registrationId = spHelper.getRegistrationId();
		
		spHelper.editCount(0);
		
		inputText = (EditText)findViewById(R.id.editText_input);
		infoText = (TextView)findViewById(R.id.textView_notice);
		sendButton = (LinearLayout)findViewById(R.id.button_send);
		chatlistView = (ListView)findViewById(R.id.listView);
		openRDrawerButton = (LinearLayout)findViewById(R.id.button_open_rdrawer);
		rDrawerView = (View)findViewById(R.id.right_drawer_layout);
		rDrawerGuideButton = (TextView)findViewById(R.id.right_drawer_button_guide);
		rDrawerGourmetButton = (TextView)findViewById(R.id.right_drawer_button_gourmet);
		rDrawerHotelButton = (TextView)findViewById(R.id.right_drawer_button_hotel);
		rDrawerSpotButton = (TextView)findViewById(R.id.right_drawer_button_spot);
		rDrawerSpotButton2 = (TextView)findViewById(R.id.right_drawer_button_spot2);
		rDrawerMyGuideButton = (TextView)findViewById(R.id.right_drawer_button_my_guide);
		rDrawerFavoriteButton = (TextView)findViewById(R.id.right_drawer_button_favorite);
		rDrawerMenuButton = (TextView)findViewById(R.id.right_drawer_button_menu);
		rDrawerClearLogButton = (TextView)findViewById(R.id.right_drawer_button_clear_log);
		developerLayout = (LinearLayout)findViewById(R.id.right_drawer_developer_layout);
		developerLayout.setVisibility(View.GONE);
		
		LayoutInflater inflater = this.getLayoutInflater();
        View footer = inflater.inflate(R.layout.item_footer, null);
        chatlistView.addFooterView(footer);
        chatAdapter = new ChatAdapter(this, chatlist);
        chatlistView.setAdapter(chatAdapter);
			
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = inputText.getText().toString();
				if (!msg.equals("")) {
					inputText.setText("送信中...");
					inputText.setEnabled(false);
					
					Date date = new Date();
					String time = dateFormat.format(date);
					
					PostServerTask sendTask = new PostServerTask(URLManager.SEND_MESSAGE_URL) {
						@Override
						protected void onPostExecute(Boolean result) {
							inputText.setText("");
							inputText.setEnabled(true);
						}
					};
					sendTask.setPostData("message", msg);
					sendTask.setPostData("regi_id", registrationId);
					sendTask.setPostData("time", time);
					sendTask.execute();
		            
		            //PostServerTask extractTask = new PostServerTask(URLManager.EXTRACT_MESSAGE_URL);
					PostServerTask extractTask = new PostServerTask(URLManager.EXTRACT_MESSAGE_NEW_URL);
		            extractTask.setPostData("message", msg);
		            extractTask.setPostData("regi_id", registrationId);
		            extractTask.setPostData("time", time);
		            extractTask.execute();
				}
				
	            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		
		openRDrawerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(rDrawerView);
			}
		});
		
		rDrawerGuideButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, SearchGuideActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerGourmetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, SearchGourmetActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerHotelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, SearchHotelActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerSpotButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, SearchSpotActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerSpotButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, SearchSpotActivity2.class);
				startActivity(i);
			}
		});
		
		rDrawerMyGuideButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, MyGuideActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerFavoriteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ChatActivity.this, FavoriteActivity.class);
				startActivity(i);
			}
		});
		
		rDrawerMenuButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (isDeveloper) {
					isDeveloper = false;
					developerLayout.setVisibility(View.GONE);
				} else {
					isDeveloper = true;
					developerLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		
		rDrawerClearLogButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					PostServerTask psTask = new PostServerTask(URLManager.DELETE_LOG_URL) {
					
					@Override
		        	protected void onPostExecute(Boolean result) {
						if (taskResult) {
							Toast.makeText(context, "ログの初期化が完了しました", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "ログの初期化が失敗しました", Toast.LENGTH_SHORT).show();
						}
		        	}
					
				};
				psTask.execute();
			}
		});
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, R.drawable.icon_app3, 
	    		R.string.drawer_open, R.string.drawer_close) {

					@Override
					public void onDrawerOpened(View drawerView) {
						super.onDrawerOpened(drawerView);
						if(drawerView == rDrawerView) {
							
						}
					}
					
					@Override
					public void onDrawerClosed(View drawerView) {
						super.onDrawerClosed(drawerView);
						if(drawerView == rDrawerView) {
							
						}
					}

	    });
	    
	    // need to return "true".
	    rDrawerView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	    
	}
    
    @Override
	protected void onStart() {
		super.onStart();
		
		receiver = new UpdateBroadcastReceiver();
	    intentFilter = new IntentFilter();
	    intentFilter.addAction(MY_ACTION_UPDATE);
	    registerReceiver(receiver, intentFilter);
	    Handler handler = new Handler();
	    Runnable runnable = new Runnable() {
	    	
			@Override
			public void run() {
				initList();
			}
			
	    };
	    receiver.setHandler(handler);
	    receiver.setRunnable(runnable); 
	    loadMember();
        initList();
        
	}
    
    @Override
	protected void onResume() {
		super.onResume();
		checkCocnection();
	}
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
     
    private void initList() {
    	spHelper.editCount(0);
    	chatAdapter.clear();
		
		String[] columns = {"ID", "REGI_ID", "TALK", "KIND", "SPOT_ID", "TIME"};
		String where = "";
		String[] whereArgs = {};
		Cursor cursor = db.query("CHAT_TABLE", columns, where, whereArgs, null, null, null);
		
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("ID"));
			String regiId = cursor.getString(cursor.getColumnIndex("REGI_ID"));
			String talk = cursor.getString(cursor.getColumnIndex("TALK"));
			int kind = cursor.getInt(cursor.getColumnIndex("KIND"));
			int spot_id = cursor.getInt(cursor.getColumnIndex("SPOT_ID"));
			String time = cursor.getString(cursor.getColumnIndex("TIME"));
			Chat cd = new Chat(id, regiId, talk, kind, spot_id, time);
			cd.setMember(getMember(regiId));
			
			chatAdapter.add(cd);
		}
		cursor.close();
		
		chatAdapter.notifyDataSetChanged();
		
		int last = chatlistView.getCount();
		chatlistView.setSelection(last);
	}
    
    private void loadMember() {
    	memberList.clear();
    	
    	Member system = Member.getSystemMember();
    	memberList.add(system);
    	
    	Member unknown = Member.getUnknownMember();
    	memberList.add(unknown);
    	
    	String[] columns = {"MY_ID", "REGI_ID", "NAME"};
		String where = "";
		String[] whereArgs = {};
		Cursor cursor = db.query("MEMBER_TABLE", columns, where, whereArgs, null, null, null);
		while (cursor.moveToNext()) {
			String regiId = cursor.getString(cursor.getColumnIndex("REGI_ID"));
			String myId = cursor.getString(cursor.getColumnIndex("MY_ID"));
			String name = cursor.getString(cursor.getColumnIndex("NAME"));
			Member member = new Member(regiId, myId, name);
			memberList.add(member);
		}
		
		ImageFileHelper ifh = new ImageFileHelper(context);
		for (int i = 0; i < memberList.size(); i++) {
			Member member = memberList.get(i);
			Bitmap bitmap = ifh.loadBitmap(member.myId, iconSize, iconSize);
			if (member.myId != null) {
				if (bitmap != null) {
					iconCache.putBitmap(member.myId, bitmap);
				} else {
					bitmap = ifh.loadBitmap(Member.getUnknownMember().myId, iconSize, iconSize);
					iconCache.putBitmap(member.myId, bitmap);
				}
			}
		}
	}
    
    private Member getMember(String regiId) {
    	for (int i = 0; i < memberList.size(); i++) {
    		String nowId = memberList.get(i).regiId;
    		if (nowId != null) { 
	    		if (nowId.equals(regiId)) {
	    			return memberList.get(i);
	    		}
    		}
    	}
    	return Member.getUnknownMember();
    }
    
    private void checkCocnection() {
    	
    	PostServerTask psTask = new PostServerTask(URLManager.CHECK_SERVER_URL){
        	
    		@Override
        	protected void onPostExecute(Boolean result) {
        		if (!taskResult) {
        			infoText.setText("ネットワークに接続されてません");
        			TranslateAnimation translate = new TranslateAnimation(0, 0, -100, 0);
        			translate.setDuration(1000);
        			translate.setFillAfter(true);
        			infoText.startAnimation(translate);
        		}
        	}	
        };
        psTask.execute();
    }
      
    private class Chat {
    	public String talk = "";
    	public String time = "";
    	public int kind = -1;
    	public int dataId = -1;
    	public Member member = null;
    	
    	public Chat(int id, String regiId, String t, int k, int s, String time) {
    		this.talk = t;
    		this.kind = k;
    		this.dataId = s;
    		this.time = time;
    	}
    	
    	void setMember(Member member) {
    		this.member = member;
    	}
    }
      
    public class ChatAdapter extends ArrayAdapter<Chat> {
	    private LayoutInflater layoutInflater;
	    private ArrayList<Chat> list;
	    String systemMessage[] = {"なんかどうですか?", "がおすすめですよ?", "に行ってみたらどう?", "に一度は行ってみたらどう?", "に行こうよ!!!"};
	    String systemMessageG[] = {"で美味しいものでも", "がおすすめですよ?", "に行ってみたらどう?"};
	    
	    public ChatAdapter (Context context, ArrayList<Chat> list) {
	        super(context, 0, list);
	        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        this.list = list;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	convertView = null;
	    	View view = convertView;
	    	
	        final Chat itemData = list.get(position);
	        final Member member = itemData.member;
	        int kind = itemData.kind;
	       
	        if (kind == 0) {
	        	view = layoutInflater.inflate(R.layout.item_chat_mine, null);
	        } else if (kind == 1) {
	        	view = layoutInflater.inflate(R.layout.item_chat_member, null);
	        } else if (kind == 2){
	        	view = layoutInflater.inflate(R.layout.item_chat_system, null);
	        } else if (kind == 3){
	        	view = layoutInflater.inflate(R.layout.item_chat_system_gourmet, null);
	        }
	        
	        LinearLayout layout = (LinearLayout)view.findViewById(R.id.chat_layout);
	        TextView voiceText = (TextView)view.findViewById(R.id.chat_item_voice);
	        TextView nameText = (TextView)view.findViewById(R.id.chat_item_name);
	        TextView timeText = (TextView)view.findViewById(R.id.chat_item_time);
	        ImageView image = (ImageView)view.findViewById(R.id.chat_item_image);
	        
	        nameText.setText(member.name);
	        String date[] = itemData.time.split(":");
	        String timeString = date[3] + ":" + date[4];  
	        timeText.setText(timeString);
	        
	        Bitmap bitmap = iconCache.getBitmap(member.myId);
	        if (bitmap != null && kind != 2 && kind != 3) {
	        	image.setImageBitmap(bitmap);
	        }
	        
	        if (kind == 2 || kind == 3) {
	        	//Random r = new Random();
	        	//int p = r.nextInt(systemMessage.length);
	        	
	        	String talkText = getColorStr("「" + itemData.talk + "」", "#ff4500") + "<br/>" + systemMessage[1];
	        	voiceText.setText(Html.fromHtml(talkText));
	        } else {
	        	voiceText.setText(itemData.talk);
	        }
	        
	        if (kind == 0) {
	        	//自分
	        	layout.setGravity(Gravity.END);
	        } else if (kind == 1) {
	        	//メンバー
	        	layout.setGravity(Gravity.START);
	        } else if (kind == 2) {
	        	//観光地
	        	layout.setGravity(Gravity.START);
	        } else if (kind == 3) {
	        	//グルメ
	        	layout.setGravity(Gravity.START);
	        }
	        
	        if (kind == 2) {
	        	voiceText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(ChatActivity.this, SpotDialogActivity.class);
						i.putExtra(IntentKey.SPOT_ID, itemData.dataId);
						startActivity(i);
						overridePendingTransition(R.animator.appearance, 0);
					}
					
				});
	        }
	        
	        if (kind == 3) {
	        	voiceText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(ChatActivity.this, GourmetDialogActivity.class);
						i.putExtra(IntentKey.GOURMET_ID, String.valueOf(itemData.dataId));
						startActivity(i);
						overridePendingTransition(R.animator.appearance, 0);
					}
					
				});
	        }
	        
	        return view;
	    }
	    
	    private String getColorStr(String str, String color) {
	    	return "<font color=" + color + ">" + str + "</font>";
	    }
    }
     
}
