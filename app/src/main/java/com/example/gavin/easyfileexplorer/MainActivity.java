package com.example.gavin.easyfileexplorer;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ListView listView;
    Button button;
    File pre = null;
    File cur = null;
    File[] fileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.text);
        listView = (ListView) findViewById(R.id.list);

        try {
            //获取sdcard的根目录
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cur = new File(Environment.getExternalStorageDirectory().getCanonicalPath());
                textView.setText(cur.toString());
                //获取文件列表
                fileList = cur.listFiles();
                initListView(fileList);
            }else{
                Toast.makeText(MainActivity.this, "ooooops,something wrong!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //如果为文件
                if(fileList[position].isFile()) {
                    Toast.makeText(MainActivity.this, "File!", Toast.LENGTH_SHORT).show();
                    return;
                }
                File[] temp = fileList[position].listFiles();
                try{
                    if(temp != null || temp.length != 0){
                        cur = fileList[position];
                        textView.setText(cur.getCanonicalPath());
                        fileList = temp;
                        initListView(fileList);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Nothing in there", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonClick();
            }
        });

    }

    private void backButtonClick(){
        try{
            if(cur.getCanonicalPath().equals(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                Toast.makeText(MainActivity.this, "No Directories", Toast.LENGTH_SHORT).show();
            }
            else{
                //获取父对象
                cur = cur.getParentFile();
                fileList = cur.listFiles();
                initListView(fileList);
            }
        }catch(Exception e){
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void initListView(File[] File){
        //创建一个kv表
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        int i;
        //为表中的每一个项配置
        for(i = 0; i<File.length; i++){
            Map<String, Object> item = new HashMap<String, Object>();
            if(File[i].isDirectory()){
                item.put("icon", R.drawable.icon1);
            }
            else{
                item.put("icon", R.drawable.icon);
            }
            item.put("name", File[i].getName());
            //将独立的表项添加进表内
            mapList.add(item);
        }
        //创建一个适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                mapList, R.layout.list_layout, new String[] {"icon", "name"}, new int[] {R.id.imageView, R.id.textView});
        //绑定适配器
        listView.setAdapter(simpleAdapter);
        try{
            textView.setText(cur.getCanonicalPath());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
