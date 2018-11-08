package com.customchips.arnavagarwal.chips;

import android.widget.BaseAdapter;
import android.widget.Filterable;

public abstract class ChipAdapter extends BaseAdapter implements Filterable {

    protected ChipAdapter() {
    }

    public String getChipName(int position) {
        return null;
    }

    public String getChipName(Object object) {
        return null;
    }

    public long getId() {
        return -1;
    }

    public boolean isMultiValued() {
        return true;
    }
}
