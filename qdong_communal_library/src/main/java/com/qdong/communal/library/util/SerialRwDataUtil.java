package com.qdong.communal.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * SerialRwDataUtil
 * 对象序列化,反序列化工具
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/20  10:35
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class SerialRwDataUtil {

	public SerialRwDataUtil() {
	}

	public void SaveMessageData(String path, Object sod, Context ctx, String key) {
		ByteArrayOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			// 写入字节流
			// fos = ctx.openFileOutput(path, Context.MODE_PRIVATE);
			SharedPreferences mySharedPreferences = ctx.getSharedPreferences(path, Context.MODE_PRIVATE);
			fos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(fos);
			oos.writeObject(sod);

			String productBase64 = (new BASE64Encoder()).encode(fos.toByteArray());
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString(key, productBase64);
			editor.commit();
		} catch (Exception e) {
			Log.e("[error]", e.toString());
			// 这里是保存文件产生异常
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// fos流关闭异常
					Log.e("[error]", e.toString());
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// oos流关闭异常
					Log.e("[error]", e.toString());
				}
			}
		}
	}

	public Object GetMessageData(String path, Context ctx, String key) {
		ByteArrayInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			// fis = ctx.openFileInput(path);
			SharedPreferences mySharedPreferences = ctx.getSharedPreferences(path, Context.MODE_PRIVATE);
			String contentString = mySharedPreferences.getString(key, "null");
			if (!contentString.equals("null")) {
				fis = new ByteArrayInputStream((new BASE64Decoder()).decodeBuffer(contentString));

			}

			ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch (Exception e) {
			Log.e("[error]", e.toString());
			// 这里是读取文件产生异常
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// fis流关闭异常
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// ois流关闭异常
					Log.e("[error]", e.toString());
				}
			}
		}
		// 读取产生异常，返回null
		return null;
	}
}
