package com.ark.movieapp.data.cache;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 *
 * Created by ahmedb on 12/13/16.
 */

public class MovieContentProvider extends ContentProvider {

    public static String[] AVAILABLE_COLUMNS = { MovieDataBaseHelper.MOVIE_ID,
            MovieDataBaseHelper.MOVIE_NAME
            ,MovieDataBaseHelper.MOVIE_POSTER_URL
            ,MovieDataBaseHelper.OVERVIEW
            ,MovieDataBaseHelper.RATE
            ,MovieDataBaseHelper.RELEASE_DATE
            ,MovieDataBaseHelper.MOVIE_BANNER_URL
            ,MovieDataBaseHelper.MOVIE_VOTE_NUMBER
            ,MovieDataBaseHelper.IS_FAV};

    // used for the UriMacher
    private static final int MOVIES = 10;
    private static final int MOVIE_ID = 20;

    public static final String AUTHORITY = "com.ark.movieapp.contentprovider";

    private static final String BASE_PATH = "movies";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/movie";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);
    }

    private MovieDataBaseHelper dataBaseHelper;


    @Override
    public boolean onCreate() {
        dataBaseHelper = new MovieDataBaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(MovieDataBaseHelper.MOVIES_TABLE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIES:
                break;
            case MOVIE_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(MovieDataBaseHelper.MOVIE_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MOVIES:
                id = sqlDB.insert(MovieDataBaseHelper.MOVIES_TABLE, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MOVIES:
                rowsDeleted = sqlDB.delete(MovieDataBaseHelper.MOVIES_TABLE, selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            MovieDataBaseHelper.MOVIES_TABLE,
                            MovieDataBaseHelper.MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            MovieDataBaseHelper.MOVIES_TABLE,
                            MovieDataBaseHelper.MOVIE_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case MOVIES:
                rowsUpdated = sqlDB.update(MovieDataBaseHelper.MOVIES_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MovieDataBaseHelper.MOVIES_TABLE,
                            values,
                            MovieDataBaseHelper.MOVIE_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MovieDataBaseHelper.MOVIES_TABLE,
                            values,
                            MovieDataBaseHelper.MOVIE_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(AVAILABLE_COLUMNS));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }

    }
}
