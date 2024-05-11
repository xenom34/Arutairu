package fr.altairstudios.arutairu;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import fr.altairstudios.arutairu.databinding.ActivityPersonalizedLessonsBinding;

public class PersonalizedLessonsActivity extends AppCompatActivity {

    private ActivityPersonalizedLessonsBinding binding;
    private Button mTuto;
    private ListView listView;
    private TextView mEmptyMessage;
    private ImageView mEmptyLogo;
    private CustomLessons storage;
    private ActivityResultLauncher<Intent> launcher; // Initialise this object in Activity.onCreate()
    private Uri baseDocumentTreeUri;
    public void launchBaseDirectoryPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        launcher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data == null){
                            //gérer
                        }
                        baseDocumentTreeUri = Objects.requireNonNull(result.getData()).getData();
                        final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        // take persistable Uri Permission for future use
                        getContentResolver().takePersistableUriPermission(result.getData().getData(), takeFlags);
                        //SharedPreferences preferences = getSharedPreferences("com.example.geofriend.fileutility", Context.MODE_PRIVATE);
                        //preferences.edit().putString("filestorageuri", result.getData().getData().toString()).apply();

                        //File file = new File(getPath(getApplicationContext(), data.getData()));

                        //Toast.makeText(getApplicationContext(), file.exists() ? "YES" : "NO", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), file.getPath(), Toast.LENGTH_LONG).show();
                        try {
                                processCSV(data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    public void writeIntoFile(Context context, String fileName, String content) throws IOException {
//        File appSpecificExternalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File appSpecificInternalStorageDirectory = context.getFilesDir();
        File file = new File(appSpecificInternalStorageDirectory, fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file, false);
        fos.write(content.getBytes());
        fos.close();
    }


    public String readFromFile(String filePath) throws IOException {
        //        File appSpecificExternalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        File appSpecificInternalStorageDirectory = this.getFilesDir();
        File file = new File(appSpecificInternalStorageDirectory, filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        fis.close();
        return builder.toString();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the file-related task you need to do.
                    openFileChooser();
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }*/


    private void processCSV(Uri pathToCSV) throws IOException {
        String row;
        ArrayList<String> japanese = new ArrayList<>();
        ArrayList<String> source = new ArrayList<>();
        ArrayList<String> romaji = new ArrayList<>();

        String title = new File(getPath(getApplicationContext(), pathToCSV)).getName();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(getPath(getApplicationContext(), pathToCSV)));

        while ((row = bufferedReader.readLine()) != null) {
            String[] data = row.split(";(?=(?:[^”]*”[^”]*”)*(?![^”]*”))");
            // do something with the data

            japanese.add(cleanTextContent(data[0]));
            romaji.add(cleanTextContent(data[1]));
            source.add(cleanTextContent(data[2]));
        }
        bufferedReader.close();

        storage.addLesson(new Lesson(japanese.toArray(new String[0]), source.toArray(new String[0]), romaji.toArray(new String[0]), title.split("\\.")[0]));
        saveCustom();
        final LessonsAdapter lessonsAdapter = new LessonsAdapter(this, storage.getList(), this);
        listView.setAdapter(lessonsAdapter);
        mEmptyMessage.setVisibility(View.INVISIBLE);
        mEmptyLogo.setVisibility(View.INVISIBLE);
    }

    private String cleanTextContent(String text)
    {
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

    public String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;


                if(new File(Environment.getExternalStorageDirectory() + "/" + split[1]).exists()){

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                } else {

                    return "/storage/" + type +"/"+ split[1];
                }

            } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
                return uri.getPath();
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);

            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static final int PICK_CSV_FILE = 2;

    public  boolean isStoragePermissionGranted() {
        boolean ret = false;
        //Check if the app has permission to read internal storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //If not, ask for permission
            Log.d("PERM :","REVOKED");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //If the app has permission, show the file picker
            Log.d("PERM :","GRANTED");
            ret = true;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = new ActivityResultLauncher<Intent>() {
            @Override
            public void launch(Intent input, @Nullable @org.jetbrains.annotations.Nullable ActivityOptionsCompat options) {

            }

            @Override
            public void unregister() {

            }

            @NonNull
            @Override
            public ActivityResultContract<Intent, ?> getContract() {
                return null;
            }
        };

        binding = ActivityPersonalizedLessonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = findViewById(R.id.listLessons);
        mEmptyLogo = findViewById(R.id.emptyLogo);
        mEmptyMessage = findViewById(R.id.emptyMessage);
        mTuto = findViewById(R.id.tutorial);

        mTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://altair-studios.fr/tutorial"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        try {
            loadCustom();
        } catch (IOException e) {
            storage = new CustomLessons();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        final LessonsAdapter lessonsAdapter = new LessonsAdapter(this, storage.getList(), this);
        listView.setAdapter(lessonsAdapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (isStoragePermissionGranted())
                //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                //someActivityResultLauncher.launch(Intent.createChooser(intent, "Choisir un fichier"));
                openFileChooser();
                //Log.d("PATH :",getApplicationContext().getFilesDir().getPath());
            }
        });

        if (!storage.isEmpty()){
            mEmptyMessage.setVisibility(View.INVISIBLE);
            mEmptyLogo.setVisibility(View.INVISIBLE);
            mTuto.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Ici, on sauvegarde l'objet sérialisable voulu dans "saves.dat"
     * @throws IOException
     */
    void saveCustom() throws IOException {
        FileOutputStream fos = getApplicationContext().openFileOutput("custom.dat", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(storage);
        os.close();
        fos.close();
    }

    /**
     * Ici, on récupère l'objet stocké dans "saves.dat"
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void loadCustom() throws IOException, ClassNotFoundException {
        FileInputStream fis = getApplicationContext().openFileInput("custom.dat");
        ObjectInputStream is = new ObjectInputStream(fis);
        storage = (CustomLessons) is.readObject();
        is.close();
        fis.close();
    }

    public void openFileChooser(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");

        someActivityResultLauncher.launch(Intent.createChooser(intent, "Choisir un fichier"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK){
            if (data == null){
                //gérer
            }

            Uri uri = data.getData();
            Toast.makeText(getApplicationContext(), uri.getPath(), Toast.LENGTH_LONG).show();
        }
    }
}