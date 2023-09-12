package com.feeling.emotion.phpassion.cluster

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.feeling.emotion.phpassion.R
import com.feeling.emotion.phpassion.databinding.ActivityStartBinding
import com.feeling.emotion.phpassion.game.GameEngineFactory
import com.feeling.emotion.phpassion.game.stonewars.deathstar.MilkyWayAlert
import com.feeling.emotion.phpassion.global.AbstractDAO


/**
 * Navigation activity
 */
class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding:ActivityStartBinding = ActivityStartBinding.inflate(layoutInflater);
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationBackground)
        }
        AbstractDAO.init(this)

        // build view ----
        binding.clusterView.clusterViewParent = binding.clusterViewParent
        binding.clusterView.setSelectTargetButton(binding.selectTarget)
        binding.selectTarget.setOnClickListener { binding.clusterView.selectTarget() }

        // set data ----
        binding.clusterView.setModel(ClusterViewModel(Cluster1.spaceObjects, GameEngineFactory().getPlanet(), resources, MilkyWayAlert(this)))

        // ensure new daily planet is visible if player is already in delta quadrant ----
        Cluster1Aufdeckungen(Cluster1.spaceObjects).fix()
    }
}
