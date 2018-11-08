package com.customchips.arnavagarwal.chips;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChipView extends LinearLayout {

    @BindView(R.id.actv_chip_name) AppCompatTextView chipNameActv;
    @BindView(R.id.remove_chip_view) LinearLayout removeChipView;

    private Context context;

    private Long id;
    private String name;
    private InteractionListener interactionListener;

    public ChipView(Context context) {
        super(context);
    }

    public ChipView(Context context, String name, Long id,
        InteractionListener interactionListener) {
        super(context);
        this.context = context;
        this.id = id;
        this.name = name;
        this.interactionListener = interactionListener;
        init();
    }

    private void init() {
        inflate(context, R.layout.chip_view, this);
        findViews();
        bindViews();
    }

    private void findViews() {
        ButterKnife.bind(this);
    }

    private void bindViews() {
        chipNameActv.setText(name);
        removeChipView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (interactionListener != null) {
                    interactionListener.removeChip(id);
                }
            }
        });
    }

    public interface InteractionListener {
        void removeChip(Long id);
    }
}
