package com.example.roshk1n.foodcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by roshk1n on 7/7/2016.
 */
public class CustomImageView extends ImageView{

        public CustomImageView(Context context) {
            super(context);
        }

        public CustomImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            Path clipPath = new Path();
            RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
            clipPath.addRoundRect(rect, 180, 180, Path.Direction.CCW);

            canvas.clipPath(clipPath);
            super.onDraw(canvas);
        }

}
