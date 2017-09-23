package com.ceiling;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.adurosmart.sdk.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Call;


/**
 * Created by best on 2017/1/13.
 */

public class ComingActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView recyclerView;
    /**
     * 联网请求所需的url
     */
    public String url = "http://api.meituan.com/mmdb/movie/v2/list/rt/order/coming.json?ci=1&limit=12&token=" +
            "&__vhost=api.maoyan.com&utm_campaign=AmovieBmovieCD-1&movieBundleVersion=6801&utm_source=xiaomi&" +
            "utm_medium=android&utm_term=6.8.0&utm_content=868030022327462&net=255&dModel=MI%205&uuid=0894DE03C7" +
            "6F6045D55977B6D4E32B7F3C6AAB02F9CEA042987B380EC5687C43&lat=40.100673&lng=116.378619&__skck=6a375bce" +
            "8c66a0dc293860dfa83833ef&__skts=1463704714271&__skua=7e01cf8dd30a179800a7a93979b430b2&__skno=1a0b4a9b" +
            "-44ec-42fc-b110-ead68bcc2824&__skcy=sXcDKbGi20CGXQPPZvhCU3%2FkzdE%3D";
    private List<NameBean> dataList = null;
    private List<ComingBean> comingList = null;

    @Override
//    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceiling);
        mContext = this;
//        ButterKnife.bind(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        getDataFromNet();

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);
    }

    /**
     * 使用okhttpUtils进行联网请求数据
     */
    private void getDataFromNet() {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.e("TAG", "联网失败" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "联网成功==" + response);
                //联网成功后使用fastjson解析
                processData(response);
            }
        });
    }

    /**
     * 使用fastjson进行解析
     *
     * @param json
     */
    private void processData(String json) {
        try {
            //这里使用GsonFormat生成对应的bean类
            JSONObject jsonObject = JSON.parseObject(json);

            String data = jsonObject.getString("data");
            JSONObject dataObj = JSON.parseObject(data);
            String coming = dataObj.getString("coming");
            JSONArray jarr = JSONArray.parseArray(coming); //JSON.parseArray(jsonStr);

            comingList = new ArrayList<>();

            for (Iterator iterator = jarr.iterator(); iterator.hasNext(); ) {
                JSONObject job = (JSONObject) iterator.next();
                String name = job.get("scm").toString();
                String boxInfo = job.get("boxInfo").toString();
                String comingTitle = job.get("comingTitle").toString();
                String img = job.get("img").toString();
                ComingBean comingBean = new ComingBean();
                comingBean.setName(name);
                comingBean.setMv_date(boxInfo);
                comingBean.setMv_dec(comingTitle);
                comingBean.setImagUrl(img);

                comingList.add(comingBean);
                System.out.println("name = " + name + ";boxinfo = " + boxInfo + ";comingTitle = " + comingTitle + ",img = " + img);
            }

//            List<ComingBean> comingslist = parseArray(coming, ComingBean.class);

            setPullAction(comingList);
            //测试是否解析数据成功
//            String strTest = comingslist.get(0).getCat();
//            Log.e("TAG", strTest + "222");

            //解析数据成功,设置适配器-->

            recyclerView.addItemDecoration(new SectionDecoration(dataList, mContext, new SectionDecoration.DecorationCallback() {
                //返回标记id (即每一项对应的标志性的字符串)
                @Override
                public String getGroupId(int position) {
                    if (dataList.get(position).getName() != null) {
                        return dataList.get(position).getName();
                    }
                    return "-1";
                }

                //获取同组中的第一个内容
                @Override
                public String getGroupFirstLine(int position) {
                    if (dataList.get(position).getName() != null) {
                        return dataList.get(position).getName();
                    }
                    return "";
                }
            }));

            //解析数据成功,设置适配器
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(mContext, comingList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void setPullAction(List<ComingBean> comingslist) {
        dataList = new ArrayList<>();
        for (int i = 0; i < comingslist.size(); i++) {
            NameBean nameBean = new NameBean();
            String name0 = comingslist.get(i).getName();
            nameBean.setName(name0);
            dataList.add(nameBean);
        }
    }
}
