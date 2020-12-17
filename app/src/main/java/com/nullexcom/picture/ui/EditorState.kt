package com.nullexcom.picture.ui

sealed class EditorState(var shouldCreate : Boolean = false) {
    object ShouldFinish: EditorState()
    object Error : EditorState()
    object Adjust: EditorState()
    object Filter: EditorState()
    object Histogram: EditorState()
    object Blur: EditorState()
    object More: EditorState()
    object AskSave: EditorState() {
        var previousPage: EditorState? = null
    }
    object Saving: EditorState()
    object Completed: EditorState()
    object Done: EditorState(shouldCreate = true)
}