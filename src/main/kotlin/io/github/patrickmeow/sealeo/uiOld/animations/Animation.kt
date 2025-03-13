package io.github.patrickmeow.sealeo.uiOld.animations

class Animation(var duration: Long) {

    var animating = false
    private var lastTime: Long = 0
    private var percent: Float = 0f

    fun start(): Boolean {
        if (!animating) {
            percent = 0f
            animating = true
            lastTime = System.currentTimeMillis()
            return true
        }
        return false
    }

    fun setPercent(percent: Float) {
        this.percent = percent
    }

    fun getPercent(): Float {
            if(animating) {
                percent = (((System.currentTimeMillis() - lastTime) / duration.toDouble())).toFloat()
            }

            if(percent >= 1) {
                animating = false
            }
           return percent

    }
}
