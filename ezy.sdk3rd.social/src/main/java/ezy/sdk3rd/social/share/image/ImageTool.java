package ezy.sdk3rd.social.share.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTool {
    public static final int SIZE_3M = 3072;
    public static int MAX_WIDTH = 768;
    public static int MAX_HEIGHT = 1024;


    public static byte[] compress(Bitmap bitmap, int maxSize, Bitmap.CompressFormat format) {
        ByteArrayOutputStream os = null;
        try {
            int size = bitmap.getRowBytes() * bitmap.getHeight();
            int quality = 100;
            if (size > maxSize) {
                quality = (int) (1f * quality * maxSize / size);
            }
            Log.d("compress", "compress quality:" + quality);

            os = new ByteArrayOutputStream();
            bitmap.compress(format, quality, os);
            return os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
        }
        return null;
    }

    public static byte[] compress(byte[] bytes, int maxSize, Bitmap.CompressFormat format) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] out = compress(bitmap, maxSize, format);
        bitmap.recycle();
        return out;
    }

    public static byte[] compress(File file, int maxSize, Bitmap.CompressFormat format) {
        if ((file == null) || (!file.getAbsoluteFile().exists())) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
        byte[] out = compress(bitmap, maxSize, format);
        bitmap.recycle();
        return out;
    }

    public static byte[] compressCircularly(Bitmap bitmap, int maxSize, Bitmap.CompressFormat format) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            for (int i = 1; i <= 10; i++) {
                bitmap.compress(format, (int) (100 * Math.pow(0.8, i)), os);
                Log.e("compress", "size = " + os.size());
                if (os.size() > maxSize) {
                    break;
                } else {
                    os.reset();
                }
            }
            return os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
        }
        return null;
    }

    public static byte[] compressCircularly(byte[] bytes, int maxSize, Bitmap.CompressFormat format) {
        if (bytes == null || bytes.length <= maxSize) {
            return bytes;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        byte[] out = compressCircularly(bitmap, maxSize, format);
        bitmap.recycle();
        return out;
    }

    public static BitmapFactory.Options decodeScaledOptions(byte[] bytes, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        int sw = (int) Math.ceil(options.outWidth / maxWidth);
        int sh = (int) Math.ceil(options.outHeight / maxHeight);

        if ((sh > 1) && (sw > 1)) {
            options.inSampleSize = Math.max(sw, sh);
        } else if (sh > 1) {
            options.inSampleSize = sh;
        } else if (sw > 1) {
            options.inSampleSize = sw;
        }

        options.inJustDecodeBounds = false;
        return options;
    }

    public static byte[] loadNetImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            if (connection.getResponseCode() == 301) {
                String redirect = connection.getHeaderField("Location");
                if (redirect.equals(url)) {
                    Log.e("image", "重定向问题：url和重定向url相同");
                    return null;
                }
                return loadNetImage(redirect);
            }
            is = connection.getInputStream();
            Log.d("image", "load image from url ==> " + url);

            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int i;
            while ((i = is.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
            return os.toByteArray();
        } catch (Exception e) {
            Log.e("image", "图片下载失败：" + e.getMessage());
            return null;
        } finally {
            close(is);
            close(os);
        }
    }



    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(format, 100, os);
        return os.toByteArray();
    }

    public static byte[] toBytes(Context context, int resId, boolean raw, Bitmap.CompressFormat format) {
        return toBytes(toBitmap(context, resId, raw), format);
    }

    public static Bitmap toBitmap(Context context, int resId, boolean raw) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (!raw) {
            options.inScaled = true;
            options.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        }
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    public static Bitmap toBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap toBitmap(File file) {
        return BitmapFactory.decodeFile(file.toString());
    }

    public static Bitmap toBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void saveTo(byte[] bytes, File file) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(bos);
        }
    }
}