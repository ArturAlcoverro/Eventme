package com.androidprog2.eventme.persistance.API;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallSingelton {
    private static CallSingelton instance;
    private static String token;
    public static final String TOKEN="TOKEN";

    private CallSingelton() {
    }

    public static CallSingelton getInstance() {
        if (instance == null) {
            instance = new CallSingelton();
        }
        return instance;
    }

    public void setToken(String token) {
        CallSingelton.token = token;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getUserId() {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject;
        int id = 0;
        try {
            jsonObject = new JSONObject(payload);
            id = (int) jsonObject.get("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertUser(String firstName, String lastName, File image, String email, String password, Callback<User> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<User> call = userDAO.createUser(
                RequestBody.create(MultipartBody.FORM, firstName),
                RequestBody.create(MultipartBody.FORM, lastName),
                requestImage,
                RequestBody.create(MultipartBody.FORM, email),
                RequestBody.create(MultipartBody.FORM, password));

        call.enqueue(callback);
    }

    public void insertUser(String firstName, String lastName, String imageURL, String email, String password, Callback<User> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<User> call = userDAO.createUserWithoutImg(
                firstName,
                lastName,
                imageURL,
                email,
                password);

        call.enqueue(callback);
    }

    public void updateUser(File image, String firstName, String lastName, String email, Callback<User> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        if(image != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
            Call<User> call = userDAO.updateUserWithImage(
                    "Bearer " + token,
                    requestImage,
                    RequestBody.create(MultipartBody.FORM, firstName),
                    RequestBody.create(MultipartBody.FORM, lastName),
                    RequestBody.create(MultipartBody.FORM, email));

            call.enqueue(callback);
        }else{
            Call<User> call = userDAO.updateUser(
                    "Bearer " + token,
                    RequestBody.create(MultipartBody.FORM, firstName),
                    RequestBody.create(MultipartBody.FORM, lastName),
                    RequestBody.create(MultipartBody.FORM, email));

            call.enqueue(callback);
        }
    }

    public void loginUser(User user, Callback callback) {
        UserDAO userDAO = APIConnector.getRetrofitInstance().create(UserDAO.class);
        Call<String> call = userDAO.loginUser(user.getEmail(), user.getPassword());
        call.enqueue(callback);
    }

    public void getProfileUser(int id, Callback<List<User>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<User>> call = userDAO.getUserProfile("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getEvents(Callback callback) {
        EventDAO eventDAO = APIConnector.getRetrofitInstance().create(EventDAO.class);
        Call<ArrayList<Event>> call = eventDAO.getEvents();
        call.enqueue(callback);
    }

    public void getEvent(int id, Callback callback) {
        EventDAO eventDAO = APIConnector.getRetrofitInstance().create(EventDAO.class);
        Call<ArrayList<Event>> call = eventDAO.getEvent(id);
        call.enqueue(callback);
    }

    public void insertEvent(String name, File image, String location, String description, String eventStart_date,
                            String eventEnd_date, String type, String n_participators, Callback<Event> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventStart_date);
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventEnd_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", image.getName(), requestFile);
        Call<Event> call = eventDAO.createEvent(
                "Bearer " + token,
                RequestBody.create(MultipartBody.FORM, name),
                requestImage,
                RequestBody.create(MultipartBody.FORM, location),
                RequestBody.create(MultipartBody.FORM, description),
                startDate,
                endDate,
                RequestBody.create(MultipartBody.FORM, n_participators),
                RequestBody.create(MultipartBody.FORM, type));

        call.enqueue(callback);
    }

    public void insertEvent(String name, String image, String location, String description, String eventStart_date,
                            String eventEnd_date, String type, String n_participators, Callback<Event> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventStart_date);
            endDate = new SimpleDateFormat("dd/MM/yyyy").parse(eventEnd_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Call<Event> call = eventDAO.createEventWithoutImg(
                "Bearer " + token,
                name,
                image,
                location,
                description,
                startDate,
                endDate,
                n_participators,
                type);

        call.enqueue(callback);
    }

    public void getUserFriends(int id, Callback<List<User>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<User>> call = userDAO.getUserFriends("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserFriendsRequests(Callback<List<User>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        FriendDAO friendDAO = retrofit.create(FriendDAO.class);

        Call<List<User>> call = friendDAO.getUserFriendsRequests("Bearer " + token);
        call.enqueue(callback);
    }

    public void getUserEventsCreated(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userEvents("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserEventsCreatedFuture(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userEventsFuture("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserEventsCreatedFinished(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userEventsFinished("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserEventsCreatedCurrent(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userEventsCurrent("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void createAssistance(int eventId, Callback<String> callback){
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Call<String> call = eventDAO.createAssistance("Bearer " + token, eventId);
        call.enqueue(callback);
    }

    public void deleteAssistance(int eventId, Callback<String> callback){
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Call<String> call = eventDAO.deleteAssistance("Bearer " + token, eventId);
        call.enqueue(callback);
    }

    public void Assistance(int eventId, Callback<String> callback){
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        EventDAO eventDAO = retrofit.create(EventDAO.class);

        Call<String> call = eventDAO.createAssistance("Bearer " + token, eventId);
        call.enqueue(callback);
    }

    public void getUserAssistances(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userAssistances("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserAssistancesFuture(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userAssistancesFuture("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUserAssistancesFinished(int id, Callback<List<Event>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        UserDAO userDAO = retrofit.create(UserDAO.class);

        Call<List<Event>> call = userDAO.userAssistancesFinished("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void getUsersListChat(Callback<List<User>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        ChatDAO chatDAO = retrofit.create(ChatDAO.class);

        Call<List<User>> call = chatDAO.usersListChat("Bearer " + token);
        call.enqueue(callback);
    }

    public void getMessages(int id, Callback<List<Message>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        ChatDAO chatDAO = retrofit.create(ChatDAO.class);

        Call<List<Message>> call = chatDAO.getMessages("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void insertMessage(String content, int user_id_send, int user_id_recived, Callback<Message> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        ChatDAO chatDAO = retrofit.create(ChatDAO.class);

        Call<Message> call = chatDAO.createMessage("Bearer " + token, content, user_id_send, user_id_recived);
        call.enqueue(callback);
    }

    public void getEventAssistances(int id, Callback callback) {
        EventDAO eventDAO = APIConnector.getRetrofitInstance().create(EventDAO.class);
        Call<ArrayList<User>> call = eventDAO.getAssistances("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void acceptFriendShip(int id, Callback<Response<Void>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        FriendDAO friendDAO = retrofit.create(FriendDAO.class);

        Call<Response<Void>> call = friendDAO.acceptFriendShip("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void declineFriendShip(int id, Callback<Response<Void>> callback) {
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        FriendDAO friendDAO = retrofit.create(FriendDAO.class);

        Call<Response<Void>> call = friendDAO.declineFriendShip("Bearer " + token, id);
        call.enqueue(callback);
    }

    public void requestFriendShip(int id, Callback<Response<Void>> callback){
        Retrofit retrofit = APIConnector.getRetrofitInstance();
        FriendDAO friendDAO = retrofit.create(FriendDAO.class);

        Call<Response<Void>> call = friendDAO.requestFriendShip("Bearer " + token, id);
        call.enqueue(callback);
    }
}
