package me.khrystal.taggroupdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.view.OnTagClickListener;
import me.khrystal.view.TagGroup;
import me.khrystal.view.TagScrollView;

public class MainActivity extends AppCompatActivity implements OnTagClickListener<TagBean> {

    private TagGroup<TagBean> mTagGroup;
    private TagScrollView mTagScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setClazz(TagBean.class);
        mTagGroup.setOnTagClickListener(this);
        mTagScrollView = (TagScrollView)findViewById(R.id.tag_sv);
        List<TagBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TagBean bean = new TagBean();
            bean.setId("" + i);
            bean.setName("bean" + i);
            list.add(bean);
        }
        mTagGroup.setTags(list);
        if (mTagGroup.getInputTag() == null)
            mTagGroup.appendInputTag();
    }


    @Override
    public void onTagClick(TagGroup.TagView tagView, TagBean tag, boolean isChecked) {
        if (isChecked) {
            mTagGroup.removeView(tagView);
            Toast.makeText(MainActivity.this, "remove:" + tag.getName(), Toast.LENGTH_SHORT).show();;
        } else {
            Toast.makeText(MainActivity.this, "click:" + tag.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
