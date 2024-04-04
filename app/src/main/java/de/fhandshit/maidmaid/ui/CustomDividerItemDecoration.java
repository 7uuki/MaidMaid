package de.fhandshit.maidmaid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import de.fhandshit.maidmaid.R;

//-----DIVIDER-----
public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    private int orientation;

    public CustomDividerItemDecoration(Context context, int orientation) {
        divider = ContextCompat.getDrawable(context, R.drawable.devider_drawable);
        this.orientation = orientation;
    }
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int dividerLeft, dividerRight, dividerTop, dividerBottom;
        int childCount = parent.getChildCount();
        if (orientation == DividerItemDecoration.VERTICAL) {
            dividerLeft = parent.getPaddingLeft();
            dividerRight = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount - 1; i++) {
                if (isLastItem(i, parent)) {
                    continue; // Skip drawing divider for last item
                }
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                dividerTop = child.getBottom() + params.bottomMargin;
                dividerBottom = dividerTop + divider.getIntrinsicHeight();
                divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                divider.draw(c);
            }
        } else {
            // Add logic for horizontal orientation if needed
        }
    }

    private boolean isLastItem(int position, RecyclerView parent) {
        return position == parent.getAdapter().getItemCount() - 1;
    }

}