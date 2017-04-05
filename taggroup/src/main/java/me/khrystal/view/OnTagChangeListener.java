package me.khrystal.view;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */
public interface OnTagChangeListener<T extends TagGroup.TagAble> {
    void onAppend(TagGroup tagGroup, T tag);
    void onDelete(TagGroup tagGroup, T tag);
}
