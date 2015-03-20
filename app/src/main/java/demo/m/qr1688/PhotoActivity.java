package demo.m.qr1688;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jyu on 15-3-18.
 */
public class PhotoActivity extends Activity implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageView imgPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        Log.d("saa", "num:" + Camera.getNumberOfCameras());

        mCamera = Camera.open();

        Camera.Parameters p = mCamera.getParameters();
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        Log.d("saa", mCamera.getParameters().getSupportedColorEffects().toString()) ;
        Log.d("saa", mCamera.getParameters().getSupportedSceneModes().toString()) ;
        Log.d("saa", mCamera.getParameters().getSupportedAntibanding().toString()) ;

        p.setSceneMode(Camera.Parameters.SCENE_MODE_SUNSET);
        p.setColorEffect(Camera.Parameters.EFFECT_AQUA);
        mCamera.setParameters(p);
        Log.d("saa", mCamera.getParameters().flatten()) ;


        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        imgPre = (ImageView) findViewById(R.id.img_pre);

        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);




    }


    public void doTake(final View view) {

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {

                Log.d("saa", "taking pic");

                mCamera.startPreview();


                new AsyncTask<Object, Object, File>() {

                    @Override
                    protected File doInBackground(Object[] params) {
                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);

                        try {
                            File image = File.createTempFile(
                                    "test",  /* prefix */
                                    ".jpg",         /* suffix */
                                    storageDir      /* directory */
                            );

                            compressImage(data, image);

                            Log.d("saa", "img:" + image.getAbsolutePath());
                            return image;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }


                    @Override
                    protected void onPostExecute(File image) {
                        super.onPostExecute(image);

                        Log.d("saa", "rending img");
                        imgPre.setPadding(1,1,1,1);
                        imgPre.setBackgroundResource(R.color.background_material_light);
                        imgPre.setImageURI(Uri.fromFile(image));
                    }
                }.execute();
            }


        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        mCamera.release();
    }

    public void compressImage(byte[] bytes, File path) {

        Bitmap capturedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Bitmap bitmap = Bitmap.createScaledBitmap(capturedImage, 800, 600, true);

        try {
            FileOutputStream baos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


        if (mSurfaceHolder.getSurface() == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mCamera.release();
    }


}