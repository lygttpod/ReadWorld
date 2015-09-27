package com.allen.readworld.utils;

import android.content.Context;
import android.os.Environment;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by allen on 2015/8/26.
 */
public class FileUtils {
    /*
    判断是否有SDCard
     */
    public static boolean isHaveSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存二进制流到sdCard
     *
     * @param filename
     * @param filecontent
     * @throws IOException
     */
    public static void saveToSDCard(String filename, byte[] filecontent) throws IOException {
        if (isHaveSDCard()) {
            File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath(), filename);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(filecontent);
            outputStream.close();
        }

    }

    public static boolean saveInfo(Context context, String filename,byte[] filecontent) {
        //通过context得到data/data/files文件夹路径
        File file = new File(context.getFilesDir(), filename);
        try {
            //向文件写入注册信息
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(filecontent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }

}
