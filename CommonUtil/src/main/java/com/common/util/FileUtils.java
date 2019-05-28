package com.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static boolean isFileExists(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    public static boolean deleteFile(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).delete();
    }

    public static void storeJSON(Context context, String fileName, Object data, boolean encrypt) {
        try {
            String dataStr = JSONUtil.toJSONString(data);
            storeString(context, fileName, dataStr, encrypt);
        } catch (Exception e) {
        }
    }

    public static <T> T readJSON(Context context, String fileName, boolean encrypt, Class<T> cls) {
        try {
            String dataStr = readString(context, fileName, encrypt);
            return JSONUtil.parseObject(dataStr, cls);
        } catch (Exception e) {
        }
        return null;
    }

    public static void storeString(Context context, String fileName, String data, boolean encrypt) {
        FileOutputStream outStream = null;
        try {
            if (encrypt) {
                data = AESTools.encode(data);
            }
            outStream = context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outStream.write(data.getBytes());
        } catch (Exception e) {
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void storeList(Context context, String fileName, ArrayList tArrayList) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = context.getApplicationContext().openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File createDefaultCacheDir(Context paramContext) {
        File file = new File(paramContext.getApplicationContext().getCacheDir(), "picasso-cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static String readString(Context context, String fileName, boolean encrypt) {
        FileInputStream inputStream = null;
        try {
            inputStream = context.getApplicationContext().openFileInput(fileName);
            if (inputStream != null) {
                String data = new String(ByteStreams.toByteArray(inputStream));
                if (encrypt) {
                    data = AESTools.decode(data);
                }
                return data;
            }
        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static <T> List<T> readObjectList(Context context, String fileName, Class<T> tClass) {
        FileInputStream fileInputStream = null;
        List<T> savedArrayList;
        try {
            fileInputStream = context.getApplicationContext().openFileInput(fileName);
            if (fileInputStream != null) {
                savedArrayList = JSONUtil.parseArray(new String(ByteStreams.toByteArray(fileInputStream)), tClass);
                return savedArrayList;
            }
        } catch (Exception e) {
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static <T> T readObject(Context context, String fileName, Class<T> tClass) {
        FileInputStream fileInputStream = null;
        T result;
        try {
            fileInputStream = context.getApplicationContext().openFileInput(fileName);
            if (fileInputStream != null) {
                result = JSONUtil.parseObject(new String(ByteStreams.toByteArray(fileInputStream)), tClass);
                return result;
            }
        } catch (Exception e) {
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static List<Object> readList(Context context, String fileName) {
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        ArrayList<Object> savedArrayList;
        try {
            fileInputStream = context.getApplicationContext().openFileInput(fileName);
            if (fileInputStream != null) {
                objectInputStream = new ObjectInputStream(fileInputStream);
                savedArrayList = (ArrayList<Object>) objectInputStream.readObject();
                return savedArrayList;
            }
        } catch (Exception e) {
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static void moveFile(String sFile, String tFile) {
        new File(sFile).renameTo(new File(tFile));
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void setLastModifiedTime(String filePath, long time) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.setLastModified(time);
        }
    }

    public static File[] listFiles(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            return dir.listFiles();
        }
        return null;
    }

    public static void storeToExternalFile(String path, String data) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(path);
            outStream.write(data.getBytes());
        } catch (Exception e) {
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String readFromExternalFile(String path) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            String data = new String(ByteStreams.toByteArray(inputStream));
            return data;
        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static boolean checkAndMakeDir(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static long getFileSize(String path) {
        File file = new File(path);
        return file.length();

    }

    public static boolean mkdirs(String path) {
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            return parentFile.mkdirs();
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean isDirAvailable(String path) {
        if (!StringUtil.isEmpty(path)) {
            File file = new File(path);
            if (file.exists() && file.canWrite()) {
                StatFs statFs = new StatFs(path);
                long bytesAvailable = 0L;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    bytesAvailable = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
                } else {
                    bytesAvailable = (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
                }
                if (bytesAvailable / (1024.f * 1024.f) > 10) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deletePrivateFile(Context context, String filename) {
        if (TextUtils.isEmpty(filename)) {
            return false;
        }
        try {
            return context.getApplicationContext().deleteFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
