package android.bignerdranch.com.beatbox

import android.bignerdranch.com.R
import android.bignerdranch.com.Sound
import android.bignerdranch.com.SoundViewModel
import android.bignerdranch.com.databinding.FragmentBeatBoxBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.bignerdranch.com.databinding.ListItemSoundBinding
import androidx.recyclerview.widget.RecyclerView




class BeatBoxFragment : Fragment() {
    companion object {
        fun newInstance(): BeatBoxFragment {
            return BeatBoxFragment()
        }
    }

    private lateinit var mBeatBox: BeatBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBeatBox = BeatBox(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentBeatBoxBinding>(inflater, R.layout.fragment_beat_box, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        binding.recyclerView.adapter = SoundAdapter(mBeatBox.getSounds())
        return binding.root
    }

    private inner class SoundHolder(private var mBinding: ListItemSoundBinding) : RecyclerView.ViewHolder(mBinding.root) {
        init {
            mBinding.viewModel = SoundViewModel(mBeatBox)
        }

        fun bind(sound: Sound) {
            mBinding.viewModel?.setSound(sound)
            mBinding.executePendingBindings()
        }
    }

    private inner class SoundAdapter(private val mSounds: MutableList<Sound>) : RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val inflater = LayoutInflater.from(activity)
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(inflater, R.layout.list_item_sound, parent, false)
            return SoundHolder(binding)
        }

        override fun getItemCount(): Int {
            return mSounds.size
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = mSounds[position]
            holder.bind(sound)
        }

    }
}