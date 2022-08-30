package android.example.customview1

import android.content.Context
import android.content.res.ColorStateList
import android.example.customview1.databinding.PartButtonsBinding
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout


enum class BottomButtonAction {
    POSITIVE,
    NEGATIVE
}

typealias OnBottomButtonsActionListener = (BottomButtonAction) -> Unit

class BottomButtonsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bottomButtonsStyle,//можно присвоить 0 если не используеца во View атребут к примеру bottomPositiveBackgroundColor или атребут Style то будет использоваца это
    defStyleRes: Int = R.style.MyBottomButtonsStyle//можно присвоить 0 если не указать R.attr.bottomButtonsStyle и не использовать во View атребут к примеру bottomPositiveBackgroundColor или атребут Style то будет использоваца это
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {

    private val binding: PartButtonsBinding
    private var listener: OnBottomButtonsActionListener? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)

        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        initListeners()
    }

    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        desStyleRes: Int
    ) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomButtonsView,
            defStyleAttr,
            desStyleRes
        )

        binding.apply {
            val positiveButtonText = typedArray.getString(R.styleable.BottomButtonsView_bottomPositiveButtonText)//получаем данные из атребута если атребут не использовалься то null
            setPositiveButtonText(positiveButtonText)

            val negativeButtonText = typedArray.getString(R.styleable.BottomButtonsView_bottomNegativeButtonText)
            setNegativeButtonText = negativeButtonText ?: ""

            val positiveButtonColor = typedArray.getColor(R.styleable.BottomButtonsView_bottomPositiveBackgroundColor, Color.WHITE)//по умольчанию Color.WHITE будет использоваца если не указали R.attr.bottomButtonsStyle или R.style.MyBottomButtonsStyle или атребут bottomPositiveBackgroundColor или атребут Style
            positiveButton.backgroundTintList = ColorStateList.valueOf(positiveButtonColor)

            val negativeButtonColor = typedArray.getColor(R.styleable.BottomButtonsView_bottomNegativeBackgroundColor, Color.WHITE)
            negativeButton.backgroundTintList = ColorStateList.valueOf(negativeButtonColor)

            val isProgressMode = typedArray.getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)
            if (isProgressMode) {
                positiveButton.visibility = INVISIBLE
                negativeButton.visibility = INVISIBLE
                progress.visibility = VISIBLE
            } else {
                positiveButton.visibility = VISIBLE
                negativeButton.visibility = VISIBLE
                progress.visibility = GONE
            }
        }

        typedArray.recycle()
    }

    private fun initListeners() {
        binding.positiveButton.setOnClickListener {
            listener?.invoke(BottomButtonAction.POSITIVE)
        }
        binding.negativeButton.setOnClickListener {
            listener?.invoke(BottomButtonAction.NEGATIVE)
        }
    }

    fun setListener(listener: OnBottomButtonsActionListener) {
        this.listener = listener
    }

    fun setPositiveButtonText(text: String?) {
        binding.positiveButton.text = text ?: ""
    }

    var setNegativeButtonText: String
        set(value) {
            binding.negativeButton.text = value
        }
        get() = binding.negativeButton.text.toString()

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState() ?: return null
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.positiveButton.text.toString()
        savedState.negativeButtonText = binding.negativeButton.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.positiveButton.text = savedState.positiveButtonText
        binding.negativeButton.text = savedState.negativeButtonText
    }

    class SavedState: BaseSavedState {

        var positiveButtonText: String? = null
        var negativeButtonText: String? = null

        constructor(superState: Parcelable) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            positiveButtonText = parcel.readString()//считали (считивать должны в том же порятке что и записивали чтобы получить именно те данные)
            negativeButtonText = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)//записали
            out.writeString(negativeButtonText)
        }

        companion object {
            @JvmField //переменная станет статик когда превратица в java код
            val CREATOR : Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return Array(size) {null}
                }
            }
        }
    }
}