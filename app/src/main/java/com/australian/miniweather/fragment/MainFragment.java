package com.australian.miniweather.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.australian.miniweather.GreemDaoManager;
import com.australian.miniweather.R;
import com.australian.miniweather.model.WeatherModel;
import com.australian.miniweather.utils.NetworkUtil;
import com.google.gson.Gson;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * 主界面视图
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainFragment";
    private final String KEY = "bba6db50910f456ba32432d1fb763e29";
    private String cityname = "beijing";
    private Context mContext;
    private TextView city_tv;
    private TextView temperature_tv;
    private TextView weather_tv;
    private TextView wind_tv;
    private TextView update_time_tv;
    private Handler handler;
    private TextView wind_level;
    private TextView wind_speed;
    private LinearLayout weather_layout_top;
    private GreemDaoManager mDbManager;
    private int menu_index = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDbManager = new GreemDaoManager(getActivity());
        mDbManager.insertWeather();
        binID();
        initOnClickListener();
        initHandler();
        initDate();
    }

    private void getWeatherFromDb() {
        List<WeatherModel> dataSource = mDbManager.queryWeather(cityname);
        city_tv.setText(cityname);
        temperature_tv.setText(dataSource.get(0).getFl() + "°C");
        weather_tv.setText(dataSource.get(0).getCond_txt());
        wind_tv.setText(dataSource.get(0).getWind_dir());
        wind_level.setText(dataSource.get(0).getWind_sc());
        wind_speed.setText(dataSource.get(0).getWind_spd() + "km/h");
        update_time_tv.setText(dataSource.get(0).getLoc());
    }

    private void initOnClickListener() {
        weather_layout_top.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Now now = (Now) msg.obj;
                switch (msg.what) {
                    case 1:
                        city_tv.setText(now.getBasic().getLocation());
                        temperature_tv.setText(now.getNow().getFl() + "°C");
                        weather_tv.setText(now.getNow().getCond_txt());
                        wind_tv.setText(now.getNow().getWind_dir());
                        wind_level.setText(now.getNow().getWind_sc());
                        wind_speed.setText(now.getNow().getWind_spd() + "km/h");
                        update_time_tv.setText(now.getUpdate().getLoc());
                        //添加到数据库操作
                        break;
                }
                Log.d(TAG, "onResponse: " + String.valueOf(now.getUpdate().getLoc()));

            }

        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    private void initDate() {
        //网络可用
        if(NetworkUtil.isNetworkAvailable(getActivity())){
            new MyAsyncTask().execute();
        }else{
            getWeatherFromDb();
            Toast.makeText(getActivity(),"Internet error", Toast.LENGTH_LONG).show();
        }
    }

    private void binID() {
        weather_layout_top = getView().findViewById(R.id.weather_layout_top);
        city_tv = getView().findViewById(R.id.city_tv);
        temperature_tv = getView().findViewById(R.id.temperature_tv);
        weather_tv = getView().findViewById(R.id.weather_tv);
        wind_tv = getView().findViewById(R.id.wind_tv);
        wind_level = getView().findViewById(R.id.wind_level);
        wind_speed = getView().findViewById(R.id.wind_speed);
        update_time_tv = getView().findViewById(R.id.update_time_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather_layout_top:
                PopupMenu memu = new PopupMenu(getActivity(),weather_layout_top);
                memu.getMenuInflater().inflate(R.menu.location,memu.getMenu());
                memu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.beijing:
                                cityname = "beijing";
                                menu_index = 1;
                                initDate();
                                break;
                            case R.id.shanghai:
                                cityname = "shanghai";
                                menu_index = 2;
                                initDate();
                                break;
                            case R.id.dalian:
                                cityname = "dalian";
                                menu_index = 3;
                                initDate();
                                break;
                        }
                        return false;
                    }
                });
                memu.show();
                break;
        }
    }

    /**
     * 实况天气
     * 实况天气即为当前时间点的天气状况以及温湿风压等气象指数，具体包含的数据：体感温度、
     * 实测温度、天气状况、风力、风速、风向、相对湿度、大气压强、降水量、能见度等。
     *
     *  context  上下文
     *  location 地址详解
     *  lang     多语言，默认为简体中文
     *  unit     单位选择，公制（m）或英制（i），默认为公制单位
     *  listener 网络访问回调接口
     */

    class MyAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            //判断网络状态
            //没有网络，从数据库拉去保存的数据 handler.sendMessage(message)
            //有网络，获取最新数据 handler.sendMessage(message) 写入数据库
            HeWeather.getWeatherNow(getActivity(), cityname, Lang.ENGLISH, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "Weather Now onError: ", e);
                }

                @Override
                public void onSuccess(Now dataObject) {
                    Log.i(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                        //此时返回数据
                        Now now = dataObject;
                        WeatherModel weatherModel = new WeatherModel();
                        weatherModel.setLocation(cityname);
                        weatherModel.setLoc(now.getUpdate().getLoc());
                        weatherModel.setFl(now.getNow().getFl());
                        weatherModel.setWind_spd(now.getNow().getWind_spd());
                        weatherModel.setWind_sc(now.getNow().getWind_sc());
                        weatherModel.setWind_dir(now.getNow().getWind_dir());
                        weatherModel.setCond_txt(now.getNow().getCond_txt());
                        weatherModel.setId(menu_index);
                        mDbManager.updateWeatherInfo(weatherModel);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = now;
                        handler.sendMessage(message);
                    } else {
                        //在此查看返回数据失败的原因
                        String status = dataObject.getStatus();
                        Code code = Code.toEnum(status);
                        Log.i(TAG, "failed code: " + code);
                    }
                }
            });
            return null;
        }
    }
}
