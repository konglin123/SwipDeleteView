package com.example.swipedeleteview;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private SwipDeleteView swipLayout;
    public ItemAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
       helper.setText(R.id.content,item);
       helper.addOnClickListener(R.id.content);
       helper.addOnClickListener(R.id.delete);
       SwipDeleteView swipDeleteView=helper.getView(R.id.sdv);
       swipDeleteView.setOnStateChangeListener(new SwipDeleteView.OnStateChangeListener() {
           @Override
           public void onOpen(SwipDeleteView swipDeleteView) {
               swipLayout=swipDeleteView;
           }

           @Override
           public void onClose(SwipDeleteView swipDeleteView) {
               if(swipLayout==swipDeleteView){
                   swipLayout=null;
               }
           }

           @Override
           public void onDown(SwipDeleteView swipDeleteView) {
                if(swipLayout!=null&&swipLayout!=swipDeleteView){
                    swipLayout.closeDelete();
                }
           }
       });
    }
}
