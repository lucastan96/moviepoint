package com.fuego.moviepoint.Utilities;

import android.util.Log;

import com.fuego.moviepoint.Cast.Cast;
import com.fuego.moviepoint.Movies.Movie;
import com.fuego.moviepoint.Search.SearchedMovies;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static ArrayList<Movie> fetchData(String url) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseJson(results, movies);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<SearchedMovies> fetchSearchData(String url) {
        ArrayList<SearchedMovies> movies = new ArrayList<>();
        try {
            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseSearchJson(results, movies);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static JSONObject fetchMovieDetails(String url) {
        JSONObject movieDetails = null;
        try {
            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            movieDetails = new JSONObject(IOUtils.toString(inputStream));
            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return movieDetails;
    }

    public static ArrayList<Cast> fetchCastData(String url) {
        ArrayList<Cast> cast = new ArrayList<>();
        try {
            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            Log.d(TAG, "fetchCastData: " + results);
            parseCastJson(results, cast);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cast;
    }

    private static void parseSearchJson(String data, ArrayList<SearchedMovies> list) {
        try {
            JSONObject mainObject = new JSONObject(data);
            JSONArray resArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                SearchedMovies movie = new SearchedMovies();
                movie.setTmdbId(jsonObject.getInt("id"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setImagePath(jsonObject.getString("poster_path"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setDate(jsonObject.getString("release_date"));
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }
    }

    private static void parseCastJson(String data, ArrayList<Cast> list) {
        try {
            JSONObject mainObject = new JSONObject(data);
            JSONArray resArray = mainObject.getJSONArray("cast");
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Cast cast = new Cast();
                cast.setName(jsonObject.getString("name"));
                cast.setRole(jsonObject.getString("character"));
                cast.setImage(jsonObject.getString("profile_path"));
                Log.d(TAG, "parseCastJson: " + cast.toString());
                list.add(cast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }
    }

    private static void parseJson(String data, ArrayList<Movie> list) {
        try {
            JSONObject mainObject = new JSONObject(data);
            JSONArray resArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTmdbId(jsonObject.getInt("id"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setImagePath(jsonObject.getString("poster_path"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setDate(jsonObject.getString("release_date"));
                list.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }
    }
}
