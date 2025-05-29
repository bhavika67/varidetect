package com.vari.varidetect.Fragment.History;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vari.varidetect.Activity.Home.HomeActivity;
import com.vari.varidetect.Adapter.ReportAdapter;
import com.vari.varidetect.Model.FilterOptions;
import com.vari.varidetect.Model.Reports;
import com.vari.varidetect.Model.SharedReportViewModel;
import com.vari.varidetect.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }




    ImageView sortIV;
    RecyclerView dataRV;
    ReportAdapter reportAdapter;
    List<Reports> reportsList;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        firestore = FirebaseFirestore.getInstance();
        sortIV = v.findViewById(R.id.sortBy);
        reportsList = new ArrayList<>();
        dataRV = v.findViewById(R.id.recyclerViewData);
        dataRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        SharedReportViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedReportViewModel.class);
        viewModel.getFilterOptions().observe(getViewLifecycleOwner(),options ->{
            if (options != null){
                applyFilters(options);
            }
        });
        sortIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSortByFragment();
            }
        });
        reportAdapter = new ReportAdapter(reportsList,getContext());
        dataRV.setAdapter(reportAdapter);
        firestore.collection("Reports")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reportsList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots){
                        Reports reports = doc.toObject(Reports.class);
                        reportsList.add(reports);
                    }
                    reportAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e ->{
                    Toast.makeText(getContext(), "Failed to load reports", Toast.LENGTH_SHORT).show();
                });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        HomeActivity.layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.layoutBottomNavigation.setVisibility(View.VISIBLE);
    }

    private void navigateToSortByFragment(){
        SortByFragment sortByFragment = new SortByFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,sortByFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void applyFilters(FilterOptions options){
        Query query = firestore.collection("Reports");

        boolean hasFileType = options.fileType != null && !options.fileType.isEmpty();
        boolean hasConfidence = options.minConfidence > 0;
        boolean filterByDate = options.sortBy != null && options.sortBy.equals("date");

        if (!options.reset) {
            // 1. Filter by mediaType
            if (hasFileType) {
                query = query.whereEqualTo("mediaType", options.fileType);
            }

            // 2. Filter by today's date
            if (filterByDate) {
                long start = getStartOfToday();
                long end = getEndOfToday();
                query = query.whereGreaterThanOrEqualTo("timestamp", start)
                        .whereLessThan("timestamp", end);
            }

            // 3. Filter by confidence (prediction)
            if (hasConfidence) {
                query = query.whereGreaterThanOrEqualTo("prediction", options.minConfidence);
            }
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            reportsList.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots){
                Reports report = doc.toObject(Reports.class);
                reportsList.add(report);
            }
            reportAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to apply filter", Toast.LENGTH_SHORT).show();
        });
    }

    private long getStartOfToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private long getEndOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}