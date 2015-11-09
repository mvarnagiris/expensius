package com.mvcoding.financius.feature.tag

import android.content.Context
import android.os.Bundle
import com.mvcoding.financius.R
import com.mvcoding.financius.feature.ActivityStarter
import com.mvcoding.financius.feature.BaseActivity
import kotlinx.android.synthetic.view_tag.tagView

class TagActivity : BaseActivity() {
    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"

        fun start(context: Context, tag: Tag) {
            ActivityStarter(context, TagActivity::class)
                    .extra(EXTRA_TAG, tag)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_tag)

        val tag = intent.getSerializableExtra(TagActivity.EXTRA_TAG) as Tag
        tagView.init(tag)
    }
}