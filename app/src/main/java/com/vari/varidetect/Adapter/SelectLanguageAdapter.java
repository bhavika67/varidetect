package com.vari.varidetect.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vari.varidetect.Model.LanguageModel;
import com.vari.varidetect.R;

import java.util.List;

public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.ViewHolder> {

    List<LanguageModel> langList;
    Context context;

    private int selectedPosition = 0;
    private OnLanguageSelectedListener listener;

    private String selectedLangCode;

    public interface OnLanguageSelectedListener{
        void onSelected(String langCode);
    }


    public SelectLanguageAdapter(List<LanguageModel> langList, Context context,OnLanguageSelectedListener listener, String selectedLangCode) {
        this.langList = langList;
        this.context = context;
        this.listener = listener;
        this.selectedLangCode = selectedLangCode;
        this.selectedPosition = findSelectedPosition();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_select_language,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LanguageModel model = langList.get(position);
        holder.langData.setText(model.getLangName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

                if(listener != null){
                    listener.onSelected(model.getLangCode());
                }

            }
        });

        if(selectedPosition==position){
            holder.layoutSelect.setBackground(context.getDrawable(R.drawable.bg_select_language_item));
            holder.iconIV.setVisibility(View.VISIBLE);
        }else {
            holder.layoutSelect.setBackground(context.getDrawable(R.drawable.bg_unselect_language_item));
            holder.iconIV.setVisibility(View.GONE);
        }

    }

    private int findSelectedPosition(){
        for (int i=0;i< langList.size();i++){
            if(langList.get(i).getLangCode().equals(selectedLangCode)){
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return langList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout layoutSelect;
        TextView langData;
        ImageView iconIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutSelect = itemView.findViewById(R.id.layoutSelectLanguage);
            langData = itemView.findViewById(R.id.languageTV);
            iconIV = itemView.findViewById(R.id.checkIcon);
        }
    }

}
