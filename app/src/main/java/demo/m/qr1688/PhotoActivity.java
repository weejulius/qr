package demo.m.qr1688;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private String userId = "00000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        userId = getIntent().getStringExtra("USER_ID_INPUT");

        Log.d("saa", "num:" + Camera.getNumberOfCameras());

        mCamera = Camera.open();

        Camera.Parameters p = mCamera.getParameters();
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        Log.d("saa", mCamera.getParameters().getSupportedColorEffects().toString());
        Log.d("saa", mCamera.getParameters().getSupportedSceneModes().toString());
        Log.d("saa", mCamera.getParameters().getSupportedAntibanding().toString());

        p.setSceneMode(Camera.Parameters.SCENE_MODE_SUNSET);
        p.setColorEffect(Camera.Parameters.EFFECT_AQUA);
        mCamera.setParameters(p);
        Log.d("saa", mCamera.getParameters().flatten());


        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        imgPre = (ImageView) findViewById(R.id.img_pre);

        mSurfaceHolder = mSurfaceView.getHolder();

        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }


    public void doTakeIDPic(final View view) {
        doTake(userId, userId + "_id", 1);

    }

    public void doTakeStorePic(final View view) {

        doTake(userId, userId + "_store", 2);
    }

    public void doTakeBizPic(final View view) {

        doTake(userId, userId + "_biz", 3);
    }

    public void doTake(final String userId, final String picName, final int type) {

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
                                    picName,  /* prefix */
                                    ".jpg",         /* suffix */
                                    storageDir      /* directory */
                            );

                            compressImage(data, image);

                            Log.d("saa", "img:" + image.getAbsolutePath());

                            updateUser(userId, image.getAbsolutePath(), type);

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
                        imgPre.setPadding(1, 1, 1, 1);
                        imgPre.setBackgroundResource(R.color.background_material_light);
                        imgPre.setImageURI(Uri.fromFile(image));
                    }
                }.execute();
            }


        });


    }


    private void updateUser(String userId, String picName, int type) {
        UserListActivity.User user = UserListActivity.users.get(userId);

        if (user == null) {
            user = new UserListActivity.User();
            user.loginId = userId;
            UserListActivity.users.put(userId, user);
        }

        if (type == 1) {
            user.idImg = picName;
        } else if (type == 2) {
            user.storeImg = picName;
        } else if (type == 3) {
            user.bizImg = picName;
        }

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
        Log.d("saa", "des...");

    }


    public void doGoToList(View view) {

        Intent intent = new Intent(this,UserListActivity.class);
        startActivity(intent);
    }
}