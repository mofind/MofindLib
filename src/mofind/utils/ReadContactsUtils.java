package mofind.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

public class ReadContactsUtils {
	public static List<PhoneContact> readContact(Context context){
		List<PhoneContact>data=new ArrayList<PhoneContact>();
		ContentResolver resolver=context.getContentResolver();
		String columns[]={Contacts._ID,Contacts.DISPLAY_NAME};
		Cursor contactCur=resolver.query(Contacts.CONTENT_URI, columns, null, null, null);
		while(contactCur.moveToNext()){
			long id=contactCur.getLong(0);
			String name = contactCur.getString(1);
			//获取图片
			InputStream is=Contacts.openContactPhotoInputStream(resolver, ContentUris.withAppendedId(Contacts.CONTENT_URI, id));
			Bitmap bmp=BitmapFactory.decodeStream(is);
			//获取电话号码
			Cursor telcursor=resolver.query(Phone.CONTENT_URI, 
					new String[]{Phone.NUMBER,Phone.TYPE},
					Phone.CONTACT_ID+"="+id,
					null,
					null);
			String homeTel=null;
			while(telcursor.moveToNext()){
				if(telcursor.getInt(1)==Phone.TYPE_HOME){
					homeTel=telcursor.getString(0);
				}
				
				
			}
			telcursor.close();
			data.add(new PhoneContact(id, name, homeTel, bmp));
		}
		
		contactCur.close();
		return data;
		
	}

}
