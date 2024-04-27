package com.example.debatazo.imgur;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImgurService {
    private static ImgurService instacia;
    private static ImgurApi repositorio;
    private final String IMGUR_URL = "https://api.imgur.com/3/";

    private ImgurService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IMGUR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repositorio = retrofit.create(ImgurApi.class);
    }
    public  static ImgurService getInstance(){
        if(instacia == null){
            instacia = new ImgurService();
        }
        return instacia;
    }

    public ImgurApi getRepor(){
        if(repositorio == null){
            return null;
        }
        return repositorio;
    }
}
