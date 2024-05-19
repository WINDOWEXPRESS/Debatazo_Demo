package com.example.debatazo.debaterecycler.detalle.objecto.user_operations;

public class UserFollwUserObject extends UserOperations{
    private int followed_by;

    public UserFollwUserObject(int user_id, int followed_by){
        super(user_id);
        this.followed_by = followed_by;
    }

    public int getFollowed_by() {
        return followed_by;
    }
}
