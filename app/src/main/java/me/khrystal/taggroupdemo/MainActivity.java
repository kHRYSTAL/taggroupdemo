package me.khrystal.taggroupdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.view.TagGroup;
import me.khrystal.view.TagScrollView;

public class MainActivity extends AppCompatActivity implements TagGroup.OnTagClickListener {

    private TagGroup mTagGroup;
    private TagScrollView mTagScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagScrollView = (TagScrollView)findViewById(R.id.tag_sv);

        mTagGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float[] rowHeightAndSpacing = mTagGroup.getRowHeightAndSpacing();
                Log.d("Main", rowHeightAndSpacing[0] + ", " + rowHeightAndSpacing[1]);
                mTagScrollView.setMaxHeight((int)( 3 * rowHeightAndSpacing[0] + 4 * rowHeightAndSpacing[1]));
            }
        });
        mTagGroup.setOnTagClickListener(this);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(""+ i);
        }
        mTagGroup.setTags(list);

    }

    @Override
    public void onTagClick(TagGroup.TagView tagView, String tag, boolean isChecked) {
        if (isChecked) {
            mTagGroup.removeView(tagView);
            Toast.makeText(MainActivity.this, "remove:" + tag, Toast.LENGTH_SHORT).show();;
        } else {
            Toast.makeText(MainActivity.this, "click:" + tag, Toast.LENGTH_SHORT).show();
        }
    }
}
