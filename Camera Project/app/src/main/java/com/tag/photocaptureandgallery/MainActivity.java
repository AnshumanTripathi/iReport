package com.tag.photocaptureandgallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.takeimage.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

	private LinearLayout lnrImages;
	private ArrayList<String> imagesPathList;
	private Bitmap yourbitmap;
	private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	private Button btnSelect;
	private ImageView ivImage;
	private String userChoosenTask;
	private final int PICK_IMAGE_MULTIPLE =1;
	private final ArrayList<String> imageStringArray= new ArrayList<String>();
	private final ArrayList<Bitmap> ResponseimageArray = new ArrayList<Bitmap>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lnrImages = (LinearLayout)findViewById(R.id.lnrImages);
		btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		ivImage = (ImageView) findViewById(R.id.ivImage);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if(userChoosenTask.equals("Take Photo"))
						cameraIntent();
					else if(userChoosenTask.equals("Choose from Library"))
					{
						ResponseimageArray.clear();
						imageStringArray.clear();
						galleryIntent();

					}

				} else {
					//code for deny
				}
				break;
		}
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				boolean result=Utility.checkPermission(MainActivity.this);

				if (items[item].equals("Take Photo")) {
					userChoosenTask ="Take Photo";
					if(result)
						cameraIntent();

				} else if (items[item].equals("Choose from Library")) {
					userChoosenTask ="Choose from Library";

					Log.d("In Custom gallary","In custom galalry");
					Intent intent = new Intent(MainActivity.this,CustomPhotoGalleryActivity.class);
					startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void galleryIntent()
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		intent.setAction(Intent.ACTION_GET_CONTENT);//
		startActivityForResult(Intent.createChooser(intent, "Select File"),1);
	}

	private void cameraIntent()
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == SELECT_FILE){
				onSelectFromGalleryResult(data);
			}

			else if (requestCode == REQUEST_CAMERA) {
				Log.d("InResult","Image captured");
				onCaptureImageResult(data);
			}
		}
	}

	private void onCaptureImageResult(Intent data) {
		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

		File destination = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");
		Log.d("captured",destination.getPath());

		FileOutputStream fo;
		try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ivImage.setImageBitmap(thumbnail);
	}

	@SuppressWarnings("deprecation")
	private void onSelectFromGalleryResult(Intent data) {

		imagesPathList = new ArrayList<String>();

		String[] imagesPath = data.getStringExtra("data").split("\\|");

		try{
			lnrImages.removeAllViews();
		}catch (Throwable e){
			e.printStackTrace();
		}
		for (int i=0;i<imagesPath.length;i++){
			imagesPathList.add(imagesPath[i]);
			yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
			imageStringArray.add(getStringImage(yourbitmap));
		}
		getImage();

		for(int i=0;i<ResponseimageArray.size();i++)
		{
			yourbitmap = ResponseimageArray.get(i);
			ImageView imageView = new ImageView(this);
			imageView.setImageBitmap(yourbitmap);
			imageView.setAdjustViewBounds(true);
			ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(lnrImages.getLayoutParams());
			params.setMargins(10, 10, 10, 10);
			imageView.setPadding(10,10,10,10);
			lnrImages.addView(imageView, params);
		}

	}

	/*
        Post Report method
    */
	public void PostReport()
	{
		String url = "http://192.168.0.19:3000/postReport";
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				//Log.d("Response: ",response.toString());
				try {
					String imageStrings=response;
					String ResponseImageArrayString[] = imageStrings.split(",");
					Log.d("length_response", String.valueOf(ResponseImageArrayString.length));
					for(String str:ResponseImageArrayString)
					{
						Log.d("Image content",str.toString());

						//for decoding string string to image
						byte[] decodedString = Base64.decode(str.getBytes(), Base64.DEFAULT);
						Bitmap Responseimage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						Log.d("decoded string ", decodedString.toString());
						Log.d("bitmap of image ", Responseimage.toString());
						ResponseimageArray.add(Responseimage);

					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("Error",error.toString());
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_email", "aparna15kulkarni@gmail.com");
				params.put("status","new");
				params.put("size","small");
				params.put("severity_level","high");
				params.put("description", "This is very bad kachra..need to be cleaned");
				params.put("lat", String.valueOf(33.1234));
				params.put("long",String.valueOf(-32.1234));
				StringBuffer Finalstr= new StringBuffer();
				String prefix="";
				for(String str:imageStringArray)
				{
					Finalstr.append(prefix);
					prefix=",";
					Finalstr.append(str);
				}
				params.put("pictures",Finalstr.toString());
				return params;
			}
		};

		queue.add(stringRequest);
	}
	/*
	Get Report method
	 */
	public void getReport()
	{
				RequestQueue queue = Volley.newRequestQueue(this);
		String url1 ="http://192.168.0.19:3000/getReport";
// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Display the first 500 characters of the response string.
						try {
							JSONObject json = new JSONObject(response.toString());
							Log.d("Json object",json.toString());
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("That didn't work!","No");
			}
		});
// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}

	/*
	This method is for testing..
	You can modify it to any other function
	 */
	public void getImage()
	{
		String url ="http://192.168.0.19:3000/testPic";
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				//Log.d("Response: ",response.toString());
				try {
					String imageStrings=response;
					String ResponseImageArrayString[] = imageStrings.split(",");
					Log.d("length_response", String.valueOf(ResponseImageArrayString.length));
					for(String str:ResponseImageArrayString)
					{
						Log.d("Image content",str.toString());
						//for decoding string json to image
						byte[] decodedString = Base64.decode(str.getBytes(), Base64.DEFAULT);
						Bitmap Responseimage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						Log.d("decoded string ", decodedString.toString());
						Log.d("bitmap of image ", Responseimage.toString());
						ResponseimageArray.add(Responseimage);

					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("Error","in error");
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("email", "aparna15kulkarni@gmail.com");
				StringBuffer Finalstr= new StringBuffer();
				String prefix="";
				for(String str:imageStringArray)
				{
					Finalstr.append(prefix);
					prefix=",";
					Finalstr.append(str);
				}
				params.put("images",Finalstr.toString());
				return params;
			}
		};

		queue.add(stringRequest);
	}

/*
This method converts Image to String
 */
	public String getStringImage(Bitmap bmp){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		//Log.d("String Image",encodedImage);
		return encodedImage;

	}



}
