package com.customchips.arnavagarwal.chips;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChipComponent extends LinearLayout {

    @BindView(R.id.actv_search) AutoCompleteTextView searchActv;
    @BindView(R.id.ll_added_chips) LinearLayout addedChipsView;

    private Map<Long, ChipView> chipViewMap;
    private Map<Long, Object> objectMap;

    private OnSelectListener onSelectListener;
    private Context context;

    private ChipAdapter ad;

    public ChipComponent(Context context, ChipAdapter adapter) {
        super(context);
        this.context = context;
        this.ad = adapter;
        init();
    }

    public ChipComponent(Context context) {
        super(context);
    }

    private void init() {
        inflate(context, R.layout.chip_component_layout, this);
        findViews();
        bindViews();
    }

    public void chipListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void addChip(Object object) {
        long id = ad.getId();
        ChipView chipView = getChip(-1, id, true, object);
        addChipToMap(id, chipView, -1, object, true);
    }

    public void removeChip(Long id) {
        removeChipFromMap(id, false);
    }

    private void findViews() {
        ButterKnife.bind(this);
    }

    private void bindViews() {
        searchActv.setAdapter(ad);
        searchActv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position,
                long id) {
                searchActv.setText("");
                if (chipViewMap != null && chipViewMap.containsKey(id)) {
                    return;
                }
                ChipView chipView = getChip(position, id, false, null);
                addChipToMap(id, chipView, position, null, false);
            }
        });
    }

    private ChipView getChip(final int position, long id, final boolean isCustomChip,
        final Object object) {
        return new ChipView(context,
            isCustomChip ? ad.getChipName(object) : ad.getChipName(position), id,
            new ChipView.InteractionListener() {
                @Override public void removeChip(Long id) {
                    removeChipFromMap(id, false);
                }
            });
    }

    private void addChipToMap(long id, ChipView chipView, int position, Object object,
        boolean isCustomChip) {
        boolean isFirstChip = false;
        if (chipViewMap == null) {
            chipViewMap = new LinkedHashMap<>();
        }
        if (objectMap == null) {
            objectMap = new LinkedHashMap<>();
        }
        if (!ad.isMultiValued()) {
            if (objectMap.size() == 0) {
                isFirstChip = true;
            }
            removeAllChipViews(true);
        }
        chipViewMap.put(id, chipView);
        objectMap.put(id, isCustomChip ? object : ad.getItem(position));
        addedChipsView.addView(chipView);
        onSelectListener.onChipAdded(isCustomChip ? object : ad.getItem(position), id, isFirstChip);
    }

    private void removeAllChipViews(boolean isReplaced) {
        if (!Utils.isEmptyMap(objectMap)) {
            for (Map.Entry<Long, Object> entry : objectMap.entrySet()) {
                onSelectListener.onChipRemoved(entry.getValue(), entry.getKey(), isReplaced);
            }
            addedChipsView.removeAllViews();
        }
        chipViewMap = new LinkedHashMap<>();
        objectMap = new LinkedHashMap<>();
    }

    private void removeChipFromMap(long id, boolean isReplaced) {
        if (chipViewMap != null && chipViewMap.containsKey(id)) {
            ChipView toRemoveChipView = chipViewMap.get(id);
            Object object = objectMap.get(id);
            addedChipsView.removeView(toRemoveChipView);
            chipViewMap.remove(id);
            objectMap.remove(id);
            onSelectListener.onChipRemoved(object, id, isReplaced);
        }
    }

    public interface OnSelectListener {

        /**
         * @param isFirstChip = This is to indicate that the chip added is the first chip that is
         * being added.
         */
        void onChipAdded(Object object, Long id, boolean isFirstChip);

        /**
         * @param isReplaced = This is to indicate whether the chip was removed manually or it was
         * removed when some
         * other chip was added.
         * The 2nd case occurs when multiValued flag is set to false and a new chip is added but
         * there was an already existing
         * chip (which is this chip and has to be removed since at any point of time only 1 chip can
         * exist in the component)
         */
        void onChipRemoved(Object object, Long id, boolean isReplaced);
    }
}
