package com.example.santa.shrinkmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, Object>> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String string[] = {"1","2","3","4","5","1","2","3","4","5","1","2","3","4","5","1","2","3","4","5","1","2","3","4","5"};
//
//        ListView listView = (ListView) findViewById(R.id.listview);
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string));


        View view = findViewById(R.id.text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "你点击了内容", Toast.LENGTH_SHORT).show();
            }
        });

        initData();

        ShrinkLayout shrinkLayout = (ShrinkLayout) findViewById(R.id.shrinkLayout);
        shrinkLayout.addMenuItem(mData);
    }


    private void initData() {
        mData = new ArrayList<>();
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "备注一哈子");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.addremark));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "备注", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "设一个闹钟呗");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.clock));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "闹钟", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "日历");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.deadday));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "日历", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "朋友啊朋友。。。");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.friends));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "朋友", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "来拍一个照吧");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.camera));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "相机", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "地球啊地球,你真的是个球");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.earth));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "地球", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "哥哥，你的购物车已满");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.shoppingcar));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "shopping", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "哥哥，听歌吗？？？？？？？？？？");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.music));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "音乐", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ShrinkLayout.MENU_TEXT, "该发短信啦");
            map.put(ShrinkLayout.MENU_DRAWABLE, getResources().getDrawable(R.mipmap.message));
            map.put(ShrinkLayout.MENU_ACTION, new MenuItem.OnClickOnCircleListener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "短信", Toast.LENGTH_SHORT).show();
                }
            });
            mData.add(map);
        }
    }
}
