package com.nesib.countriesapp.ui.countries

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.nesib.countriesapp.R
import com.nesib.countriesapp.databinding.SingleChipViewBinding

class SingleChipSelector @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var currentChip = SortBy.None

    private val binding = SingleChipViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val primaryColor: Int
        get() = ContextCompat.getColor(context, R.color.colorPrimary)


    var chipSelectListener: ((SortBy) -> Unit)? = null


    init {
        binding.sortArea.setOnClickListener {
            if (currentChip == SortBy.Area) {
                chipSelectListener?.invoke(SortBy.None)
            } else {
                chipSelectListener?.invoke(SortBy.Area)
            }
        }
        binding.sortPopulation.setOnClickListener {
            if (currentChip == SortBy.Population) {
                chipSelectListener?.invoke(SortBy.None)
            } else {
                chipSelectListener?.invoke(SortBy.Population)
            }
        }
    }

    fun populationSelected() {
        chipSelectListener?.invoke(SortBy.Population)
        binding.sortArea.setTextColor(primaryColor)
        binding.sortArea.setBackgroundResource(R.drawable.filter_bg_outline)

        binding.sortPopulation.setTextColor(Color.WHITE)
        binding.sortPopulation.setBackgroundResource(R.drawable.filter_bg_filled)

        currentChip = SortBy.Population
    }

    fun areaSelected() {
        chipSelectListener?.invoke(SortBy.Area)
        binding.sortArea.setTextColor(Color.WHITE)
        binding.sortArea.setBackgroundResource(R.drawable.filter_bg_filled)

        binding.sortPopulation.setTextColor(primaryColor)
        binding.sortPopulation.setBackgroundResource(R.drawable.filter_bg_outline)

        currentChip = SortBy.Area
    }

    fun resetChips() = with(binding) {
        sortArea.setTextColor(primaryColor)
        sortArea.setBackgroundResource(R.drawable.filter_bg_outline)
        sortPopulation.setBackgroundResource(R.drawable.filter_bg_outline)
        sortPopulation.setTextColor(primaryColor)

        currentChip = SortBy.None
    }


    enum class SortBy {
        Population,
        Area,
        None
    }


}


