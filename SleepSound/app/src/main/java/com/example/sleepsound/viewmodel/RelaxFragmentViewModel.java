package com.example.sleepsound.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sleepsound.model.Mix;
import com.example.sleepsound.model.Sound;
import com.example.sleepsound.shared_preferences.MySharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class RelaxFragmentViewModel extends ViewModel {
    private List<Mix> mixList;

    private MutableLiveData<List<Mix>> liveMixList;

    private MySharedPreferences mySharedPreferences;

    public final int CATEGORY_PIANO_RELAX = 4;
    public final int CATEGORY_RAIN = 3;
    public final int CATEGORY_CITY = 2;
    public final int CATEGORY_MEDITATION = 5;
    public final int CATEGORY_FOCUS = 6;
    public final int CATEGORY_ALL = 0;

    public RelaxFragmentViewModel(){
        liveMixList = new MutableLiveData<>();
        loadMixes();
    }

    public void loadMixes() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Mix>>() {
        }.getType();
        try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("assets/mixs.json"))) {
            mixList = gson.fromJson(reader, listType);
            for (Mix m : mixList){
                System.out.println(m);
            }
            liveMixList.setValue(mixList);
        }catch (Exception e) {
            System.err.println(e);
        }
    }

    public void filterMixesByCategory(int category) {
        if (category == CATEGORY_ALL) {
            // Hiển thị tất cả các mục nếu category là "All"
            liveMixList.setValue(mixList);
        } else {
            // Lọc danh sách mix theo category
            List<Mix> filteredList = mixList.stream()
                    .filter(mix -> mix.getCategory() == category)
                    .collect(Collectors.toList());
            liveMixList.setValue(filteredList);
        }
    }


    public List<Mix> getMixList() {
        return mixList;
    }

    public void setMixList(List<Mix> mixList) {
        this.mixList = mixList;
    }

    public MutableLiveData<List<Mix>> getLiveMixList() {
        return liveMixList;
    }

    public void setLiveMixList(MutableLiveData<List<Mix>> liveMixList) {
        this.liveMixList = liveMixList;
    }

    public MySharedPreferences getMySharedPreferences() {
        return mySharedPreferences;
    }

    public void setMySharedPreferences(MySharedPreferences mySharedPreferences) {
        this.mySharedPreferences = mySharedPreferences;
    }
}
