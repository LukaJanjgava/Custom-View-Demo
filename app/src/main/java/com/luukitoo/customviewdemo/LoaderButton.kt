package com.luukitoo.customviewdemo

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.luukitoo.customviewdemo.databinding.ViewLoaderButtonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoaderButton(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(context, attrs, defStyleAttr, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)

    constructor(
        context: Context
    ) : this(context, null)

    private val binding by lazy {
        val inflater = LayoutInflater.from(context)
        ViewLoaderButtonBinding.inflate(inflater, this)
    }

    init {
        initializeAttributes(attrs, defStyleAttr)
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) return
        val attributesArray = context.obtainStyledAttributes(attrs, R.styleable.LoaderButton, defStyleAttr, 0)

        val buttonText = attributesArray.getString(R.styleable.LoaderButton_android_text) ?: ""
        text = buttonText

        val buttonColor = attributesArray.getColor(R.styleable.LoaderButton_backgroundTint, context.getColor(R.color.purple_500))
        color = buttonColor

        val isButtonInProgress = attributesArray.getBoolean(R.styleable.LoaderButton_inProgress, false)
        inProgress = isButtonInProgress

        val buttonTextColor = attributesArray.getColor(R.styleable.LoaderButton_android_textColor, context.getColor(R.color.white))
        textColor = buttonTextColor

        val buttonTextSize = attributesArray.getDimension(R.styleable.LoaderButton_android_textSize, 16f)
        textSize = buttonTextSize

        val progressBarSize = attributesArray.getDimension(R.styleable.LoaderButton_progressBarSize, 66f)
        this.progressBarSize = progressBarSize

        val cornerRadius = attributesArray.getDimension(R.styleable.LoaderButton_cornerRadius, 12f)
        this.cornerRadius = cornerRadius

        attributesArray.recycle()
    }

    var text: String = ""
        set(value) {
            field = value
            binding.materialButton.text = value
        }

    var color: Int = context.getColor(R.color.purple_500)
        set(value) {
            field = value
            binding.materialButton.backgroundTintList = ColorStateList.valueOf(value)
        }

    var inProgress: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.progressBar.visibility = VISIBLE
                binding.materialButton.isEnabled = false
                binding.materialButton.alpha = 0.5f
            } else {
                binding.progressBar.visibility = GONE
                binding.materialButton.isEnabled = true
                binding.materialButton.alpha = 1f
            }
        }

    var textColor: Int = context.getColor(R.color.white)
        set(value) {
            field = value
            binding.materialButton.setTextColor(value)
        }

    var textSize: Float = 16f
        set(value) {
            field = value
            binding.materialButton.textSize = value
        }

    var progressBarSize: Float = 24f
        set(value) {
            field = value
            binding.progressBar.updateLayoutParams<LayoutParams> {
                width = value.toInt()
            }
        }

    var cornerRadius: Float = 12f
        set(value) {
            field = value
            binding.materialButton.cornerRadius = value.toInt()
        }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener(listener)
        binding.materialButton.setOnClickListener(listener)
    }

    fun setOnAsyncClickListener(
        scope: CoroutineScope,
        context: CoroutineContext = Dispatchers.Main,
        block: suspend CoroutineScope.(View) -> Unit
    ) {
        binding.materialButton.setOnClickListener { view ->
            scope.launch(context) {
                block.invoke(this@launch, view)
            }
        }
    }

}