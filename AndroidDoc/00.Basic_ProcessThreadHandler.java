iconDrawable.let { drawable ->
    val scaleX = lerp(0f, 1f, scaleIconFraction)
    val scaleY = scaleX
    val pivotX = width / 2f
    val pivotY = height / 2f
    canvas.withScale(scaleX, scaleY, pivotX, pivotY) {
        // rotate the icon if it appears
        if (scaleIconFraction > 0) {
            invalidate()
        }
        rotate(iconDegree, pivotX, pivotY)
        // Anticlockwise direction
        val iconSpeed = 6
        iconDegree = (iconDegree - iconSpeed) % 360
        drawable.draw(canvas)
    }
}
