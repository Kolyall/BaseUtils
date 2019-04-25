package com.github.kolyall.androidutils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import java.security.Key;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

/**
 * Created by pavelhunko on 11.9.16.
 */
public class Utils {

    public static boolean isPasswordValid(String pass, EditText editText, String message) {
        if (pass.length() >= 6) {
            return true;
        }
        editText.setError(message);
        editText.requestFocus();
        return false;
    }

    public static boolean isPasswordValid(String pass) {
        if (!TextUtils.isEmpty(pass) && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    public static boolean isNameValid(String name) {
        if (!TextUtils.isEmpty(name) && name.length() > 0 && name.length() <= 30) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email, EditText editText, String message) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        editText.setError(message);
        editText.requestFocus();
        return false;
    }

    public static boolean isEmailValid(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static String md5(String pass) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(pass.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

//    public static synchronized void requestPermissions(final AppCompatActivity activity, String[] requestedPermissions, final int requestPermissionCode) {
//        final ArrayList<String> permissions = preparePermissionsList(activity, requestedPermissions);
//        OnPermissions listener = (OnPermissions) activity;
//        if (permissions != null && !permissions.isEmpty()) {
//            String explanationPopup = "";
//            int i = 0;
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions.get(i)) && explanationPopup.isEmpty()) {
//                DialogClass.showOneButtonWithoutTitleDialog(activity, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), requestPermissionCode);
//                    }
//                });
//            } else {
//                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), requestPermissionCode);
//            }
//        } else {
//            listener.onPermissionsGranted(requestPermissionCode);
//        }
//    }
//
//    public static synchronized void requestPermissions(final Fragment fragment, String[] requestedPermissions, final int requestPermissionCode) {
//        OnPermissions listener = (OnPermissions) fragment;
//        final ArrayList<String> permissions = preparePermissionsList(fragment.getActivity(), requestedPermissions);
//        if (permissions != null && !permissions.isEmpty()) {
//            String explanationPopup = "";
//            int i = 0;
//            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), permissions.get(i)) && explanationPopup.isEmpty()) {
//                DialogClass.showOneButtonWithoutTitleDialog(fragment.getActivity(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        fragment.requestPermissions(permissions.toArray(new String[permissions.size()]), requestPermissionCode);
//                    }
//                });
//            } else {
//                fragment.requestPermissions(permissions.toArray(new String[permissions.size()]), requestPermissionCode);
//            }
//        } else {
//            listener.onPermissionsGranted(requestPermissionCode);
//        }
//    }


//    private static ArrayList<String> preparePermissionsList(Context context, String[] permissions) {
//        ArrayList<String> notGrantedPermissions = new ArrayList<>();
//        for (String permissionName : permissions) {
//            if (ActivityCompat.checkSelfPermission(context, permissionName) != PackageManager.PERMISSION_GRANTED) {
//                notGrantedPermissions.add(permissionName);
//            }
//        }
//        return notGrantedPermissions;
//    }

//    public static boolean isValidToken(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context).getLong(Constants.EXPIRATION, 0) > System.currentTimeMillis();
////            return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.AUTH_HASH, "");
////        else return updateToken(context);
//    }
//    public static String createPaymentSignature(String merchantID, String orderID, String orderAmount, AssistPaymentData.Currency currency) {
//        String sourceString = merchantID + ";" + orderID + ";" + orderAmount + ";" + currency.toString() + ";" + merchantID;
//        String digest = md5(sourceString);
//        try {
//            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//            generator.initialize(1024);
//            return encryptToBase64(generator.generateKeyPair().getPrivate(), digest);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Base64.encodeToString(digest.getBytes(), Base64.DEFAULT);
//    }

    private static String encryptToBase64(Key privateKey, String toBeCiphred) {
        byte[] cyphredText = encrypt(privateKey, toBeCiphred.getBytes());
        return Base64.encodeToString(cyphredText, Base64.DEFAULT);
    }

    private static byte[] encrypt(Key publicKey, byte[] toBeCiphred) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "SC");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return rsaCipher.doFinal(toBeCiphred);
        } catch (Exception e) {
            Log.e(Utils.class.getSimpleName(), "Error while encrypting data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    public static void saveImageToInternalStorage(Bitmap bitmap, Context context) {
//        ContextWrapper contextWrapper = new ContextWrapper(context.getApplicationContext());
//        File directory = contextWrapper.getDir(Constants.USER_PIC_PATH, Context.MODE_PRIVATE);
//        File userPic = new File(directory, Constants.USER_PIC_NAME);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(userPic);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static Bitmap loadImageFromInternalStorage(Context context) {
//        Bitmap bitmap = null;
//        try {
//            File f = new File(new ContextWrapper(context.getApplicationContext()).getDir(Constants.USER_PIC_PATH, Context.MODE_PRIVATE), Constants.USER_PIC_NAME);
//            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }

//    public static void removeImageFromInternalStorage(Context context) {
//        try {
//            File f = new File(new ContextWrapper(context.getApplicationContext()).getDir(Constants.USER_PIC_PATH, Context
//                    .MODE_PRIVATE), Constants.USER_PIC_NAME);
//
//            boolean deleted = f.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}