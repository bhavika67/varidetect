package com.vari.varidetect.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedReportViewModel extends ViewModel {
    private final MutableLiveData<FilterOptions> filterOptions = new MutableLiveData<>();

    public void setFilterOptions(FilterOptions options){
        filterOptions.setValue(options);
    }

    public LiveData<FilterOptions> getFilterOptions(){
        return filterOptions;
    }

}
