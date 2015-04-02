package utility.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 提供的针对文件的加压解压的操作工具类
 * 
 */
public class ZipUtil {

	/**
	 * 解压一个压缩文档 到指定位置
	 * 
	 * @param zipFileString
	 *            压缩包的名字
	 * @param outPathString
	 *            指定的路径
	 * @throws Exception
	 */
	public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
		java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new FileInputStream(zipFileString));
		java.util.zip.ZipEntry zipEntry;
		String szName = "";

		while ((zipEntry = inZip.getNextEntry()) != null) {
			szName = zipEntry.getName();

			if (zipEntry.isDirectory()) {
				// get the folder name of the widget
				szName = szName.substring(0, szName.length() - 1);
				File folder = new File(outPathString + File.separator + szName);
				folder.mkdirs();
			} else {

				File file = new File(outPathString + File.separator + szName);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				// read (len) bytes into buffer
				while ((len = inZip.read(buffer)) != -1) {
					// write (len) byte from buffer at the position 0
					out.write(buffer, 0, len);
					out.flush();
				}
				out.close();
			}
		}// end of while

		inZip.close();

	}// end of func

	/**
	 * 给定一个List文件然后生成一个Zip文件
	 * 
	 * @param fileName
	 * @param files
	 * @return
	 * @descrition
	 */
	public static File createZipFile(String path, String fileNamet, List<File> files) {
		File parentDirs = new File(path);
		if (!parentDirs.exists()) {
			parentDirs.mkdirs();
		}
		ZipOutputStream zipOutputStream = null;
		File zip = null;
		try {
			zip = new File(parentDirs, fileNamet + ".zip");
			zip.createNewFile();
			zipOutputStream = new ZipOutputStream(new FileOutputStream(zip));
			// 文件 比如图片文件
			if (files != null && files.size() > 0) {
				for (File file : files) {
					if (file.exists()) {
						putFileInZip(zipOutputStream, file, file.getName());
					}
				}
			}
			return zip;
		} catch (Exception e) {
		} finally {
			try {
				if (zipOutputStream != null) {
					zipOutputStream.close();
				}
			} catch (Exception e) {
			}
		}
		return zip;

	}

	// 添加文件在 zip中
	private static void putFileInZip(ZipOutputStream zipOutputStream, File file, String fileName) throws Exception {
		FileInputStream fileInputStream = null;
		try {
			ZipEntry entry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(entry);

			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[1024 * 20];
			int length = -1;
			while ((length = fileInputStream.read(buffer)) != -1) {
				zipOutputStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
