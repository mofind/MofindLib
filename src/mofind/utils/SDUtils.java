package mofind.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class SDUtils {

	public static String parentPath = "";

	/**
	 * @return 返回SD路径
	 */
	public static String sdPath(Context c) {
		String rootpath = c.getFilesDir().getAbsolutePath();
		if (isSDExists()) {
			rootpath = Environment.getExternalStorageDirectory().toString();
		}
		return rootpath;
	}

	/**
	 * @return 返回文件夹路径
	 */
	public static String initParentPath(Context c) {
		parentPath = sdPath(c) + File.separator + c.getPackageName();
		File file = new File(parentPath);
		if (!file.exists())
			file.mkdirs(); // 新建一个子目录
		System.out.println("父路径= " + parentPath);
		return parentPath;
	}

	public static String initParentPath(Context c, String parent) {
		parentPath = sdPath(c) + File.separator + parent;
		File file = new File(parentPath);
		if (!file.exists()) {
			file.mkdirs(); // 新建一个子目录
		}
		return parentPath;
	}

	/**
	 * 创建目录
	 * @param filePath 创建目标文件的目录，带文件名
	 * @return
	 */
	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);
		if (TextUtils.isEmpty(folderName)) {
			return false;
		}
		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}

	public static String getFolderName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}
		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * @param filePath
	 * @return
	 * @see #makeDirs(String)
	 */
	public static boolean makeFolders(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File folder = new File(filePath);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}
	
	/**
	 * 获取随机文件名
	 * @return
	 */
	public static String getFileName() {
		return getFileName("mf");
	}
	
	public static String getFileName(String prefix) {
		StringBuffer sb = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		String[] dictionaries = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		sb.append(prefix);
		sb.append(millis);
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			sb.append(dictionaries[random.nextInt(dictionaries.length - 1)]);
		}
		return sb.toString();
	};

	/**
	 * 检查SD卡是否可用
	 * 
	 * @return boolean
	 */
	public static boolean isSDExists() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}

		File file = new File(filePath);
		return (file.exists() && file.isFile());
	}

	/**
	 * 文件夹是否存在
	 * 
	 * @param directoryPath
	 * @return
	 */
	public static boolean isFolderExist(String directoryPath) {
		if (TextUtils.isEmpty(directoryPath)) {
			return false;
		}

		File dire = new File(directoryPath);
		return (dire.exists() && dire.isDirectory());
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFilePath
	 * @param destFilePath
	 * @return
	 */
	public static boolean copyFile(String sourceFilePath, String destFilePath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(sourceFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		}
		return writeFile(destFilePath, inputStream);
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return true;
		}

		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}

	/**
	 * get file size
	 */
	public static long getFileSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return -1;
		}

		File file = new File(path);
		return (file.exists() && file.isFile() ? file.length() : -1);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * <pre>
	 *      getFileExtension(null)               =   ""
	 *      getFileExtension("")                 =   ""
	 *      getFileExtension("   ")              =   "   "
	 *      getFileExtension("a.mp3")            =   "mp3"
	 *      getFileExtension("a.b.rmvb")         =   "rmvb"
	 *      getFileExtension("abc")              =   ""
	 *      getFileExtension("c:\\")              =   ""
	 *      getFileExtension("c:\\a")             =   ""
	 *      getFileExtension("c:\\a.b")           =   "b"
	 *      getFileExtension("c:a.txt\\a")        =   ""
	 *      getFileExtension("/home/admin")      =   ""
	 *      getFileExtension("/home/admin/a.txt/b")  =   ""
	 *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	 * </pre>
	 * 
	 * @param filePath
	 * @return
	 */
	public final static String FILE_EXTENSION_SEPARATOR = ".";

	public static String getFileExtension(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1) {
			return "";
		}
		return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path
	 *            : 相对于sd卡下的路径
	 * @param fileName
	 *            : 要读取内容的文件名字
	 * @return 读取到的内容；读取失败，返回null
	 */
	public static String readText(String path) {
		if (!isSDExists()) {
			return null;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
			br.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static StringBuilder readFile(String filePath, String charsetName) {
		File file = new File(filePath);
		StringBuilder fileContent = new StringBuilder("");
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
			reader = new BufferedReader(is);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!fileContent.toString().equals("")) {
					fileContent.append("\r\n");
				}
				fileContent.append(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	public static boolean writeFile(String filePath, String content) {
		return writeFile(filePath, content, false);
	}

	public static boolean writeFile(String filePath, InputStream stream) {
		return writeFile(filePath, stream, false);
	}

	public static boolean writeFile(String filePath, InputStream stream, boolean append) {
		return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	}

	public static boolean writeFile(String filePath, String content, boolean append) {
		if (TextUtils.isEmpty(content)) {
			return false;
		}

		FileWriter fileWriter = null;
		try {
			makeDirs(filePath);
			fileWriter = new FileWriter(filePath, append);
			fileWriter.write(content);
			fileWriter.close();
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	public static boolean writeFile(File file, InputStream stream, boolean append) {
		OutputStream o = null;
		try {
			makeDirs(file.getAbsolutePath());
			o = new FileOutputStream(file, append);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				o.write(data, 0, length);
			}
			o.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (o != null) {
				try {
					o.close();
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * 读取字节文件内容
	 * 
	 * @param path
	 *            相对于sd卡下的路径
	 * @param fileName
	 *            要读取内容的文件名字
	 * @return 文件字节流；失败，返回null
	 */
	public static InputStream readStream(String path) {
		if (!isSDExists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(path);
			return fis;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取字节文件内容
	 * 
	 * @param path
	 *            相对于sd卡下的路径
	 * @param fileName
	 *            要读取内容的文件名字
	 * @return 文件字节数组；失败，返回null
	 */
	public static byte[] readStreamContent(String path) {
		if (!isSDExists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(path);
			byte[] content = readAllStream(fis);
			fis.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将输入流的内容读入到一个byte数组
	 * 
	 * @param is
	 *            输入流
	 * @return 输入流内容构成的byte[]
	 */
	public static byte[] readAllStream(InputStream is) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int size;
		try {
			while ((size = is.read(buffer, 0, 1024)) != -1) {
				bos.write(buffer, 0, size);
			}
			byte[] content = bos.toByteArray();
			bos.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
