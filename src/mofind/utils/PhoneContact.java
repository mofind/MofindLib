package mofind.utils;

import android.graphics.Bitmap;

public class PhoneContact {
	private long id;
	private String name, phonenum;
	private Bitmap photo;

	public PhoneContact(long id, String name, String phonenum, Bitmap photo) {
		super();
		this.id = id;
		this.name = name;
		this.phonenum = phonenum;
		this.photo = photo;
	}

	public PhoneContact(String name, String phonenum, Bitmap photo) {
		super();
		this.name = name;
		this.phonenum = phonenum;
		this.photo = photo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Contacts [id=" + id + ", name=" + name + ", phonenum=" + phonenum + ", photo=" + photo + "]";
	}

}
