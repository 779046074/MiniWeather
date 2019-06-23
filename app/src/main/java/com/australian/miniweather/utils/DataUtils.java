package com.australian.miniweather.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.australian.miniweather.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 获取数据
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<WeatherModel> getWeatherModels (String json){
        List<WeatherModel> result = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0 ; i < jsonArray.length() ; i ++) {
                JSONObject object = jsonArray.getJSONObject(i);
                WeatherModel weatherModel = new WeatherModel();
                weatherModel.setId(object.getInt("locationId"));
                weatherModel.setLocation(object.getString("location"));
                weatherModel.setLoc(object.getString("loc"));
                weatherModel.setFl(object.getString("fl"));
                weatherModel.setCond_txt(object.getString("cond_txt"));
                weatherModel.setWind_dir(object.getString("wind_dir"));
                weatherModel.setWind_sc(object.getString("wind_sc"));
                weatherModel.setWind_spd(object.getString("wind_spd"));
                result.add(weatherModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
