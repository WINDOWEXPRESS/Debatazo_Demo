package com.example.debatazo.debaterecycler.detalle.objecto.user_operations;

public class UserLikeDebatesObject extends UserOperations{

    private int debate_id;

    public UserLikeDebatesObject(int debate_id, int user_id){
        super(user_id);
        this.debate_id = debate_id;
    }

    public int getDebate_id() {
        return debate_id;
    }
}
