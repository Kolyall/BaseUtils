package by.kolyall.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.DrawableRes;


public class ImageUtil {

    public static final String TEMP_DIR = File.separator + "tmpimages";
    public static final String TEMP_IMAGE_PREFIX = "tmp";

    public static Bitmap decodeSampledBitmap(@DrawableRes int imageResource, Context context, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageResource, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), imageResource, options);
    }

    public static Bitmap decodeSampledBitmap(Uri imageLocation, Context context, int reqWidth, int reqHeight) throws IOException {
        InputStream in = getInputStreamForImage(imageLocation, context);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in, null, options);
    }

    public static InputStream getInputStreamForImage(Uri imageLocation, Context context) throws IOException {
        return context.getContentResolver().openInputStream(imageLocation);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int calculateInSampleSize(final int imageWidth, final int imageHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int inSampleSize = 1;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            final int halfHeight = imageHeight / 2;
            final int halfWidth = imageWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File createTempImageFile(Context context) throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = TEMP_IMAGE_PREFIX + timeStamp + "_";

        File storageDir = DiskUtil.getDiskCacheDir(context, TEMP_DIR);

        //noinspection ResultOfMethodCallIgnored
        storageDir.mkdir();

        if (!storageDir.isDirectory()) {
            throw new IOException("Could not open cache directory");
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    /**
     * Create an Intent to show all locations for selecting an image. This includes the camera, image applications, and file system
     * applications that allow selecting "image/*" files.
     *
     * @param outputFile The output file to use when taking a picture using a Camera.
     * @return An Intent to launch to show the media selection options.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Intent createImagePickerIntent(Context context, File outputFile) throws IOException {

        // Camera.
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        List<Intent> cameraIntents = new ArrayList<>();
        for (ResolveInfo resolveInfo : listCam) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
            cameraIntents.add(intent);
        }

        // Filesystem.
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        Intent chooserIntent =
                Intent.createChooser(galleryIntent, "Choose Image");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));


        return chooserIntent;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteTemporaryImages(Context context) {
        if (Environment.getExternalStorageState().startsWith(Environment.MEDIA_MOUNTED)) {
            new File(context.getExternalCacheDir(), TEMP_DIR).delete();
        }
        new File(context.getCacheDir(), TEMP_DIR).delete();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            view.setBackgroundDrawable(drawable);
        else
            view.setBackground(drawable);
    }

    public static Drawable getDrawable(Context context, int drawableRes) {
        Drawable drawable;
        Resources res = context.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            drawable = res.getDrawable(drawableRes, context.getTheme());
        else
            drawable = res.getDrawable(drawableRes);
        return drawable;
    }

}
