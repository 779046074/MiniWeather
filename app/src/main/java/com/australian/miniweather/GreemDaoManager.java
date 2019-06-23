package com.australian.miniweather;

import android.content.Context;
import android.database.DatabaseUtils;

import com.australian.miniweather.model.WeatherModel;
import com.australian.miniweather.model.WeatherModelDao;
import com.australian.miniweather.utils.DataUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreemDaoManager {

    private Context mContext;
    private WeatherModelDao mWeatherModelDao;

    public GreemDaoManager(Context context) {
        mContext = context;
        mWeatherModelDao = MyApplication.mSession.getWeatherModelDao();
    }

    /**
     * 添加所有数据到数据库
     */
    public void insertWeather(){
        String json = DataUtils.getJson("weather.json",mContext);
        List<WeatherModel> weatherModels = DataUtils.getWeatherModels(json);
        mWeatherModelDao.insertOrReplaceInTx(weatherModels);
    }


    //筛选
    public List<WeatherModel> queryWeather(String location){

        QueryBuilder query = mWeatherModelDao.queryBuilder();
        query = query.where(WeatherModelDao.Properties.Location.eq(location))
        .orderAsc(WeatherModelDao.Properties.Id);
        return query.list();

    }

    //删除指定数据
    public void deleteWeatherInfo(WeatherModel weatherModel){
        mWeatherModelDao.delete(weatherModel);
    }

    //修改数据
    public void updateWeatherInfo(WeatherModel weatherModel){
        mWeatherModelDao.updateInTx(weatherModel);
    }

}
