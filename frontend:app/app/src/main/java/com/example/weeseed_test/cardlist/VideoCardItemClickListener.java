package com.example.weeseed_test.cardlist;

import com.example.weeseed_test.dto.Video_item;

public interface VideoCardItemClickListener {
    void onItemClick_vid(Video_item videoItem);
    void onItemClick_vidDelete(Video_item videoItem, boolean isselected);
    void onItemLongClick_vid(Video_item videoItem);
}
