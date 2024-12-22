package com.example.sleepsound.API;

import static android.content.ContentValues.TAG;

import com.example.sleepsound.model.Sound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import android.content.Context;
import android.util.Log;

public class JsonHelper {
    private static <T> List<T> loadJsonFile(Context context, String fileName, Type type) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            return new Gson().fromJson(json, type);
        } catch (IOException e) {
            Log.e(TAG, "Error reading " + fileName, e);
            return Collections.emptyList();
        }
    }

    public static List<Sound> loadSounds(Context context) {
        try {
            // Mở file sounds.json từ assets
            InputStream inputStream = context.getAssets().open("sounds.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Chuyển dữ liệu từ bytes sang String JSON
            String json = new String(buffer, "UTF-8");

            // Sử dụng Gson để parse JSON thành danh sách Sound
            Type soundListType = new TypeToken<List<Sound>>() {}.getType();
            return new Gson().fromJson(json, soundListType);
        } catch (IOException e) {
            Log.e(TAG, "Error reading sounds.json", e);
            return Collections.emptyList(); // Trả về list rỗng nếu gặp lỗi
        }
    }
}
