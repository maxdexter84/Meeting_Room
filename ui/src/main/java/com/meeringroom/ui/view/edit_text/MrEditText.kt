package com.meeringroom.ui.view.edit_text


import android.content.Context
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.widget.addTextChangedListener
import com.meetingroom.ui.R
import com.meetingroom.ui.databinding.CustomEdittextBinding

class MrEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var text: String? = ""
        set(value) {
            field = value ?: ""
            binding.editTextCustomEditText.setText(value)
        }
        get() = binding.editTextCustomEditText.text.toString()

    var textHint: String? = ""
        set(value) {
            field = value ?: ""
            binding.editTextCustomEditText.hint = value
        }

    var inputTypeTypes: MrEditTextTypes = MrEditTextTypes.Login()
        set(value) {
            field = value
            setPropertiesForToggleButtonAndEditText(value)
        }

    var textError: String? = ""
        set(value) {
            field = value ?: ""
            binding.errorTextCustomEditText.text = value

            if (field!!.isEmpty()) {
                binding.viewCustomEditText.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.light_grey,
                        null
                    )
                )
            } else {
                binding.viewCustomEditText.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.error_color_for_line_under_edittext,
                        null
                    )
                )
            }
        }

    private var binding: CustomEdittextBinding =
        CustomEdittextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupAttributes(attrs, defStyle)
    }

    private fun setupAttributes(attrs: AttributeSet?, defStyle: Int) {
        context.withStyledAttributes(attrs, R.styleable.MrEditText, defStyle, 0) {

            binding.editTextCustomEditText.hint = getString(R.styleable.MrEditText_setHintText)
            textHint = getString(R.styleable.MrEditText_setHintText)
            binding.errorTextCustomEditText.text =
                getString(R.styleable.MrEditText_setErrorText)
            textError = getString(R.styleable.MrEditText_setErrorText)

            inputTypeTypes =
                MrEditTextTypes.fromId(getInt(R.styleable.MrEditText_inputType, 2))
        }
        binding.editTextCustomEditText.addTextChangedListener {
            textError = ""
        }
    }

    private fun setPropertiesForToggleButtonAndEditText(value: MrEditTextTypes) {
        when (value) {
            is MrEditTextTypes.Password -> {
                binding.toggleButtonCustomEditText.setOnClickListener {
                    binding.editTextCustomEditText.transformationMethod =
                        if (binding.toggleButtonCustomEditText.isChecked)
                            PasswordTransformationMethod.getInstance()
                        else
                            HideReturnsTransformationMethod.getInstance()

                    binding.editTextCustomEditText.setSelection(binding.editTextCustomEditText.text.length)
                }
                binding.toggleButtonCustomEditText.visibility = View.VISIBLE
            }
            is MrEditTextTypes.Login -> {
                binding.toggleButtonCustomEditText.visibility = View.INVISIBLE
                binding.editTextCustomEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
            else -> {
                binding.toggleButtonCustomEditText.visibility = View.INVISIBLE
                binding.editTextCustomEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
    }

}