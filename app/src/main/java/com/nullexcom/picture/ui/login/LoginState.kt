package com.nullexcom.picture.ui.login

sealed class LoginState {
    object Authenticating : LoginState()
    class Error(val reason: String) : LoginState()
    class Successful(val isFirstUse: Boolean) : LoginState()
}