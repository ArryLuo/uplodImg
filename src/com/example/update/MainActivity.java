package com.example.update;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		img = (ImageView) findViewById(R.id.img);
		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				// MediaStore媒体库,
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 100);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 100) {
			if (data != null) {
				// img.setImageURI(data.getData());
				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				Toast.makeText(MainActivity.this, originalUri + "", 1).show();
				String path = getPath(originalUri);
				// 使用ContentProvider通过URI获取原始图片

				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					// 获取这个图片的宽和高
					Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
					options.inJustDecodeBounds = false;
					int heig = options.outHeight;
					Log.v("TAG", heig + "图片高度");
					// 计算缩放比
					int be = (int) (options.outHeight / (float) 200);
					if (be <= 0)
						be = 1;
					options.inSampleSize = be;
					// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
					bitmap = BitmapFactory.decodeFile(path, options);
					int w = bitmap.getWidth();
					Log.v("TAG", w + "图片高度");
					int h = bitmap.getHeight();
					Log.v("TAG", h + "图片高度");
					System.out.println(w + "   " + h);
					img.setImageBitmap(bitmap);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
