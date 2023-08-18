import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.blankapptest.CustomStaggeredGridLayoutManager


internal class GridSpanDecoration(private val padding: Int) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val gridLayoutManager = parent.layoutManager as CustomStaggeredGridLayoutManager?
        val spanCount = gridLayoutManager!!.spanCount
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex
        val spanSize = params.spanSize

        // If it is in column 0 you apply the full offset on the start side, else only half
        if (spanIndex == 0) {
            outRect.left = padding
        } else {
            outRect.left = padding / 2
        }

        // If spanIndex + spanSize equals spanCount (it occupies the last column) you apply the full offset on the end, else only half.
        if (spanIndex + spanSize == spanCount) {
            outRect.right = padding
        } else {
            outRect.right = padding / 2
        }

        // just add some vertical padding as well
        outRect.top = padding / 2
        outRect.bottom = padding / 2
        if (isLayoutRTL(parent)) {
            val tmp = outRect.left
            outRect.left = outRect.right
            outRect.right = tmp
        }
    }

    companion object {
        @SuppressLint("NewApi", "WrongConstant")
        private fun isLayoutRTL(parent: RecyclerView): Boolean {
            return parent.layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
        }
    }
}