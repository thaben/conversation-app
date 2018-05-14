package com.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//without salt !!

public class EncryptPassword {

	private MessageDigest digester;

	public MessageDigest getDigester() {
		return digester;
	}

	public void setDigester(MessageDigest digester) {
		this.digester = digester;
	}

	private void crypt() {
		try {
			setDigester(MessageDigest.getInstance("MD5"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String encryptPassword(String str) {
		this.crypt();
//	

		digester.update(str.getBytes());
		byte[] hash = digester.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}
		return hexString.toString();
	}
}

