package com.example.m8_prak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.m8_prak.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{
    private static final String TAG = "debug MainAct";
    ActivityMainBinding bindingMain;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private GestureDetector mDetector;                                                              // gesture detectore

    private PointF start;                                                                           //  posisi awal dari garis. Silahkan membaca referensi PointF
    private Paint strokePaint;                                                                      //  style dari coretan yang digunakan
    private boolean canvasInitiated;                                                                //  menandakan apakah canvas sudah diinisialisasi atau belum. Pengguna tidak dapat menggambar sebelum canvas diinisialisasi.

    private PointF startPt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {                                            Log.d(TAG, "onCreate: masuk");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindingMain = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(bindingMain.getRoot());




        // 4. Draw canvas
        // Fill the entire canvas with this solid color
        /*int mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.design_default_color_primary, null);                                        // R.color.colorPrimary , gaada

        mCanvas.drawColor(mColorBackground);*/





        // create gesture detector + listener
        this.mDetector = new GestureDetector(this, new MyCustomGestureListener());

        // listens
        this.bindingMain.btnAction.setOnClickListener(this);
        this.bindingMain.btnBlack.setOnClickListener(this);
        this.bindingMain.btnBlue.setOnClickListener(this);
        this.bindingMain.btnRed.setOnClickListener(this);
        this.bindingMain.btnGreen.setOnClickListener(this);

        // atribut initialization
        this.canvasInitiated = false;
        // initiateCanvas();
    }


    @Override
    public void onClick(View view) {                                                                Log.d(TAG, "onClick: masuk");
        //btn new : initiate canvas , change to reset button
        //btn reset : resetcanvas
            // new & reset same button, dibedain getText()
        //color 1-4 : change color


        if (view == this.bindingMain.btnAction) {                                                   Log.d(TAG, "onClick: view = buttonAction");
            if (this.bindingMain.btnAction.getText().equals("New")) {                               Log.d(TAG, "onClick: action button 'New'");
                // set ke reset
                this.bindingMain.btnAction.setText("Reset");

                initiateCanvas();
                this.canvasInitiated = true;
            } else {                                                                                // berarti lg "Reset"
                this.resetCanvas();
                                                                                                    Log.d(TAG, "onClick: action button 'Reset' ");
            }
        } // colors
        else if (view == this.bindingMain.btnBlack) {                                               Log.d(TAG, "onClick: view = button Black");
            if (this.canvasInitiated) {                                                             // kl sudah diinisiasi
                this.changeStrokeColor(ResourcesCompat.getColor( getResources(), R.color.black, null) );  // set warnanya ke black
                            //parameternya (int Color)
            }
        }
        else if (view == this.bindingMain.btnRed) {                                                 Log.d(TAG, "onClick: view = button red");
            if (this.canvasInitiated) {
                this.changeStrokeColor(ResourcesCompat.getColor( getResources(), R.color.red, null) );
            }
        }
        else if (view == this.bindingMain.btnGreen) {                                               Log.d(TAG, "onClick: view = button green");
            if (this.canvasInitiated) {
                this.changeStrokeColor(ResourcesCompat.getColor( getResources(), R.color.green, null) );
            }
        }
        else if (view == this.bindingMain.btnBlue) {                                                Log.d(TAG, "onClick: view = button blue");
            if (this.canvasInitiated) {
                this.changeStrokeColor(ResourcesCompat.getColor( getResources(), R.color.blue, null) );
            }
        }
    }

    public void initiateCanvas(){                                                                   Log.d(TAG, "initiateCanvas: masuk");
        // 1. Create Bitmap
//        this.mBitmap = Bitmap.createBitmap(200, 200,
//                Bitmap.Config.ARGB_8888);
        this.mBitmap = Bitmap.createBitmap(bindingMain.ivCanvas.getWidth(), bindingMain.ivCanvas.getHeight(),
                Bitmap.Config.ARGB_8888);

        // 2. Associate the bitmap to the ImageView.
        this.bindingMain.ivCanvas.setImageBitmap(mBitmap);                                          // diset View -> Bitmap

        // 3. Create a Canvas with the bitmap.
        this.mCanvas = new Canvas(mBitmap);                                                         // Canvas -> Bitmap


        // new paint for stroke + style (Paint.Style.STROKE)
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);

        //resetCanvas
        this.resetCanvas();
        this.bindingMain.ivCanvas.setOnTouchListener(this);
    }

    public void resetCanvas(){                                                                      Log.d(TAG, "resetCanvas: masuk");
        /*Tombol Reset : membersihkan kanvas (menimpa dengan warna background) dan
        mengembalikan warna dan besar coretan. (default warna : hitam, default width:3)*/

        // 4. Draw canvas background
        this.mCanvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.design_default_color_background, null));

        // 5. force draw
        this.bindingMain.ivCanvas.invalidate();

        // 6. reset stroke width + color
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.changeStrokeColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        this.strokePaint.setStrokeWidth(3);
    }

    private void changeStrokeColor(int color) {                                                     Log.d(TAG, "changeStrokeColor: masuk");
        //change stroke color using parameter (color resource id)
        this.strokePaint.setColor(color);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {                                    Log.d(TAG, "onTouch: masuk");
        // gesture listener
        return this.mDetector.onTouchEvent(motionEvent);

        // super.onTouchEvent(motionEvent)
    }

    private class MyCustomGestureListener extends GestureDetector.SimpleOnGestureListener{
        private String TAG = "debug myCustGestList";

        @Override
        public boolean onDown(MotionEvent e) {                                                      Log.d(TAG, "onDown: masuk");
            //new start point with position if null, else set start position
            if (startPt == null) {                                                                  Log.d(TAG, "onDown: startPt null");
                startPt = new PointF(e.getX(), e.getY());
            }else {                                                                                 Log.d(TAG, "onDown: startPt NOT null");
                startPt.set(e.getX(), e.getY());
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {     Log.d(TAG, "onScroll: masuk;    onScroll TRUE");
            //set path
            Path strokePath = new Path();

            //change start point for next path
            strokePath.moveTo(startPt.x, startPt.y);

            //draw path
            /* dari modul prak
            Path strokePath = new Path();
            strokePath.moveTo(<startX>, <startY>);
            strokePath.lineTo(<stopX>, <stopY>);
            strokePath.close();
            <canvas>.drawPath(strokePath, <strokePaint>);
            */

            strokePath.lineTo(e2.getX(), e2.getY());
            strokePath.close();

            Log.d(TAG, "onScroll: colorintnya: " + strokePaint.getColor());
            mCanvas.drawPath(strokePath, strokePaint);
            startPt.set(e2.getX(), e2.getY());
                                                                                                    Log.d(TAG, "onScroll: PAINT TRUE");

            //redraw
            bindingMain.ivCanvas.invalidate();

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress: masuk");
            Log.d(TAG, "onLongPress: Stroke Size: " + strokePaint.getStrokeWidth());


            //toggle change stroke + show toast
            //syntax : Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            //			toast.show();
            strokePaint.setStrokeWidth(strokePaint.getStrokeWidth() == 3.0f ? 20.0f : 3.0f);
            Toast.makeText(getApplicationContext(),
                    "Stroke size changed to " + strokePaint.getStrokeWidth(),
                    Toast.LENGTH_SHORT).show();



        }
    }
}