package com.example.mycookie.ui.main

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mycookie.R
import com.example.mycookie.SettingsHandler
import kotlinx.android.synthetic.main.fragment_shopping.*
import kotlinx.android.synthetic.main.fragment_shopping.monster


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {
    private lateinit var pageViewModel: PageViewModel

    var image: Int = R.drawable.monster2
        set(value) {
            field = value
            saveInfo("image", value)
        }

    var background: Int = R.drawable.monster2
        set(value) {
            field = value
            saveInfo("image", value)
        }

    var bonus: Int = R.drawable.monster2
        set(value) {
            field = value
            saveInfo("image", value)
        }


    private fun setValueImg(root: View, img: Int): ImageView {
        return root.findViewById(img)
    }

    private fun setValueBackground(root: View, background: Int): TextView {
        return root.findViewById(background)
    }

    private fun loadImage(image: Int, background: Int, bonus: Int): Array<Int> {
        return arrayOf(image, background, bonus)
    }

    private fun loadText(): Array<String> {
        return arrayOf("Monster", "Background", "Bonus")
    }

    private fun saveInfo(name: String, elem: Int) {
        val pref = this.activity!!.getSharedPreferences("info", MODE_PRIVATE)
        var editor = pref.edit();

        editor.putInt(name, elem);
        editor.apply()
    }

    private fun getSaveInfo(name: String): Int {
        var shared = this.activity!!.getSharedPreferences("info", MODE_PRIVATE)
        var stringTemp = shared.getInt(name, 0);

        return stringTemp
    }

    private fun howMuch(arg: CharSequence): Int {
        val defaultPrice = arrayOf<Int>(0, 100, 1000, 2000, 3000)


        for (value in defaultPrice) {
            if (arg.toString() == value.toString()) {
                SettingsHandler.instance.save = value
            }
        }
        return SettingsHandler.instance.save
    }

    private fun changeId(mDescription: TextView, palier: Int, giveMoney: CharSequence, newElem: Int, image: String) {
        if (getSaveInfo("scoreLevel") >= palier) {
            if (getSaveInfo("score") >= howMuch(giveMoney)) {
                if (getSaveInfo(image) != newElem) {
                    SettingsHandler.instance.chooseImage = 0
                    saveInfo(image, newElem)
                } else {
                    SettingsHandler.instance.chooseImage = 1
                    Toast.makeText(getActivity(), "You have already select this Items, please choose another one!", Toast.LENGTH_SHORT).show();
                }
                saveInfo("score", getSaveInfo("score") - howMuch(giveMoney))
            } else {
                Toast.makeText(getActivity(), "Score too low. You need ${getSaveInfo("score") - howMuch(giveMoney)}!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Level too low. New items on level $palier!", Toast.LENGTH_SHORT).show();
        }
    }

    private fun clickListener(root: View, mDescription: TextView, zoneInfo: Int, newImage: Int, newBackground: Int, giveMoney: CharSequence, palier: Int) {
        if (mDescription.text == "Monster") {
            changeId(mDescription, palier, giveMoney, newImage, "image")
        }
        if (mDescription.text == "Background") {
            changeId(mDescription, palier, giveMoney, newBackground, "background")
            constraintLayout.setBackgroundResource(getSaveInfo("background"))
        }
        if (mDescription.text == "Bonus") {
            changeId(mDescription, palier, giveMoney, newImage, "bonus")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onResume() {
        super.onResume()
        constraintLayout.setBackgroundResource(getSaveInfo("background"))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shopping, container, false)
        val mDescription = setValueBackground(root, R.id.tabTextView)

        pageViewModel.text.observe(this, Observer<Int> {
            setValueImg(root, R.id.monster).setImageResource(loadImage(R.drawable.monster2, R.drawable.fond, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster4).setImageResource(loadImage(R.drawable.monster4, R.drawable.back1, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster3).setImageResource(loadImage(R.drawable.monster3, R.drawable.back2, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster8).setImageResource(loadImage(R.drawable.monster8, R.drawable.back, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster10).setImageResource(loadImage(R.drawable.monster10, R.drawable.back9, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster1).setImageResource(loadImage(R.drawable.monster1, R.drawable.back10, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster0).setImageResource(loadImage(R.drawable.monster0, R.drawable.back4, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster6).setImageResource(loadImage(R.drawable.monster6, R.drawable.back5, R.drawable.bonus)[it])
            setValueImg(root, R.id.monster9).setImageResource(loadImage(R.drawable.monster9, R.drawable.back7, R.drawable.bonus)[it])
            setValueBackground(root, R.id.tabTextView).text = loadText()[it]

            monster.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster2, R.drawable.fond, textView5.text, 10)
            }
            monster4.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster4, R.drawable.back1, textView6.text, 10)
            }
            monster3.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster3, R.drawable.back2, textView7.text, 10)
            }
            monster8.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster8, R.drawable.back, textView8.text, 100)
            }
            monster10.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster10, R.drawable.back9, textView9.text, 100)
            }
            monster1.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster1, R.drawable.back10, textView10.text, 100)
            }
            monster0.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster0, R.drawable.back4, textView12.text, 1000)
            }
            monster6.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster6, R.drawable.back5, textView13.text, 1000)
            }
            monster9.setOnClickListener {
                clickListener(root, mDescription, R.id.relativeLayout, R.drawable.monster9, R.drawable.back7, textView14.text, 1000)
            }
        })
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}