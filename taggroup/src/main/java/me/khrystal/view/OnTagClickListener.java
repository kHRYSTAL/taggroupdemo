package me.khrystal.view;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */

public interface OnTagClickListener<T extends TagGroup.TagAble> {
    void onTagClick(TagGroup.TagView tagView, T tag, boolean isChecked);
}
