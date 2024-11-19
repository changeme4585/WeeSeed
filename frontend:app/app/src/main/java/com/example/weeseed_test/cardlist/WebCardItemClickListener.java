package com.example.weeseed_test.cardlist;

import com.example.weeseed_test.dto.Aac_item;

public interface WebCardItemClickListener {
    void onItemClick_aac(Aac_item aacItem);
    void onItemClick_aacDelete(Aac_item aacItem, boolean isselected);
    void onItemLongClick_aac(Aac_item aacItem);
}
