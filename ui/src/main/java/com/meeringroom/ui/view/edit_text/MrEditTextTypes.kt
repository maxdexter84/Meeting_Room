package com.meeringroom.ui.view.edit_text

sealed class MrEditTextTypes(val id: Int) {
    class Password : MrEditTextTypes(1)
    class Login : MrEditTextTypes(2)

    companion object {
        fun fromId(id: Int): MrEditTextTypes {
            for (type in values()) {
                if (type.id == id) return type
            }
            throw IllegalArgumentException()
        }

        private fun values(): Array<MrEditTextTypes> {
            return arrayOf(Login(), Password())
        }
    }
}