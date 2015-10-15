package com.wyj.mytestjpush;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.wyj.mytestjpush.adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

//实现的功能：1、实现用drawable来防止图片加载时的错位，并自己练习图片的二次采样
//          2、练习使用极光推送，将推送整合到项目中 AppKey：ea3738f922e0f05db815e6b2
public class MainActivity extends Activity {
    private ListView listView;

    private List<String> list;

    private ImageAdapter adapter;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView)findViewById(R.id.list_view);

        list = new ArrayList<String>();
        list.add("http://h.hiphotos.baidu.com/zhidao/pic/item/6a63f6246b600c3320b14bb3184c510fd8f9a185.jpg");
        list.add("http://attach.bbs.miui.com/forum/201502/08/021410qwvwbpdkiasdhwha.jpg");
        list.add("http://attachments.gfan.com/forum/attachments2/day_121023/1210232138c4878e8a7973a2d4.jpg");
        list.add("http://e.hiphotos.baidu.com/zhidao/pic/item/38dbb6fd5266d01695ab94bf952bd40734fa35f2.jpg");
        list.add("http://img3.douban.com/view/photo/raw/public/p1678149275.jpg");
        list.add("http://pic2.nipic.com/20090506/1055421_080356081_2.jpg");
        list.add("http://pica.nipic.com/2008-01-05/200815121123445_2.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1103325186,3037174239&fm=21&gp=0.jpg");
        list.add("http://e.hiphotos.baidu.com/zhidao/pic/item/42a98226cffc1e179e26529c4890f603728de986.jpg");
        list.add("http://d.3987.com/ziyanghua_140731/003.jpg");
        list.add("http://e.hiphotos.baidu.com/album/pic/item/902397dda144ad343548b329d0a20cf430ad85ac.jpg?psign=3548b329d0a20cf431adcbef76094b36adaf2edda2cca194");
        list.add("http://attach.bbs.miui.com/forum/201502/08/021337iretfkky8rxr51xl.jpg");
        list.add("http://fa.topit.me/a/d2/c0/119285754686dc0d2ao.jpg");
        list.add("http://www.33.la/uploads/20140407pic/544.jpg");
        list.add("http://b.hiphotos.baidu.com/zhidao/pic/item/faf2b2119313b07e9b7570800cd7912396dd8c69.jpg");
        list.add("http://attach.bbs.miui.com/forum/201408/29/123450r617238484s76q78.jpg");
        list.add("http://pic23.nipic.com/20120905/8135252_162441670186_2.jpg");
        list.add("http://c.hiphotos.baidu.com/zhidao/pic/item/a8773912b31bb051baab40b2347adab44bede099.jpg");

        adapter = new ImageAdapter(this,list);

        listView.setAdapter(adapter);
    }

}
