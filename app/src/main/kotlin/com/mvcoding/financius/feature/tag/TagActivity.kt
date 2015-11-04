package com.mvcoding.financius.feature.tag

import android.content.Context
import android.os.Bundle
import com.mvcoding.financius.R
import com.mvcoding.financius.feature.ActivityStarter
import com.mvcoding.financius.feature.BaseActivity

class TagActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, TagActivity::class)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_tag)
    }
}