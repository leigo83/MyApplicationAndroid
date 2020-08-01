package com.example.mydribbble;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mydribbble.BaseFragment.SingleFragment;
import com.example.mydribbble.model.Shot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class UploadImageFragment extends SingleFragment implements View.OnClickListener {
    private Button chooseImageBtn;
    private Button uploadImageBtn;
    private ImageView imageView;
    private Uri filePath;
    private Bitmap bitmap = null;
    private  UploadTask uploadTask;
    private OkHttpClient client = new OkHttpClient();

    public static int PICK_IMAGE_REQUEST = 1025;
    public static int STORAGE_PERMISSION_CODE = 2324;
    @Override
    protected Fragment createFragment() {
        return null;
    }

    public static Fragment createInstance () {
        return new UploadImageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        requestStoragePermission();
        View view = inflater.inflate(R.layout.upload_image, container, false);
        chooseImageBtn = view.findViewById(R.id.chooseBtn);
        uploadImageBtn = view.findViewById(R.id.uploadBtn);
        imageView = view.findViewById(R.id.newImage);
        chooseImageBtn.setOnClickListener(this);
        uploadImageBtn.setOnClickListener(this);
        uploadTask = new UploadTask();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == chooseImageBtn) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
        } else if (v == uploadImageBtn) {
            uploadTask.execute();
        }
    }

    class UploadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
            String token = "d8db2a871b8c3a153611987baa4f7adb482435f9466c2a37fc21604469fa19ab";
            String ShotLinkSingle = "https://api.dribbble.com/v2/user/shots";
            StringBuilder sb = new StringBuilder ();
            sb.append(ShotLinkSingle);
            sb.append("?");
            sb.append("access_token=");
            sb.append(token);
//            RequestBody requestBody = new MultipartBody.Builder().addFormDataPart("title", "NEWIMAGE")
//                    .addFormDataPart("image", "test.jpg", RequestBody.create(new File(filePath.getPath()), MEDIA_TYPE_PNG)).build();
//            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("title", "NEWIMAGE")
//                    .addFormDataPart("image", "test.jpg", RequestBody.create(byteArray, MediaType.parse("image/*.jpg"))).build();
       //     File file = new File (filePath.getPath());
            File file = new File ("/storage/emulated/0/Pictures/Lei.jpg");
      //      File file = new File ("/external/images/media/2069/car.jpg");
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("title", "NEWIMAGE")
                    .addFormDataPart("image", "/storage/emulated/0/Pictures/Lei.jpg", RequestBody.create(file, MediaType.parse("image/jpg"))).build();
//            FormBody.Builder formBody = new FormBody.Builder()
//                    .add("title", "NEWIMAGE")
//                    .add("image", new File(filePath.getPath()));
 //           RequestBody requestBody = formBody.build();
            String query = Uri.parse(sb.toString()).buildUpon().build().toString();
            Request request = new Request.Builder().url(query).post(requestBody).build();

            try {
                Response response = client.newCall(request).execute();
                Log.d("LeiCreate", response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1 );
        cursor.close();

        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("LeiCreate", "Permission is already granted");
            return;
        }
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.getContext(), "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.getContext(), "Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK)  {
            filePath = data.getData();
            Log.d("LeiCreate", filePath.getPath());
        //    Log.d("LeiCreate", getPath(filePath));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);

//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(getContext().getApplicationContext().getContentResolver(), bitmap, "Title", null);
//            Uri uri = Uri.parse(path);
//
//            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            Log.d("LeiCreate",  cursor.getString(idx));


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContext().getApplicationContext().getContentResolver(), bitmap, "Lei", null);
            Uri uri = Uri.parse(path);

            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor =  getActivity().getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.d("LeiCreate",  cursor.getString(column_index));

           /* Uri cur = MediaStore.Images.Media.getContentUri(filePath.getPath());

            String[] filePathColumn = {MediaStore.Images.Media._ID};

            Cursor cursor = getActivity().getContentResolver().query(filePath, filePathColumn, null, null, null);

            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                long imageId = cursor.getLong(columnIndex);
                Uri uriImage = Uri.withAppendedPath(uriExternal, "" + imageId);
                Log.d("LeiCreate", uriImage.getPath());
                cursor.close();

            }*/
        }
    }
}
