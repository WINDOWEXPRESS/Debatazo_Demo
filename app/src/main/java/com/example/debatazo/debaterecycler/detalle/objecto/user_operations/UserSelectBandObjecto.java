package com.example.debatazo.debaterecycler.detalle.objecto.user_operations;

public class UserSelectBandObjecto extends UserOperations{
    private int debate_id;
    private int band_id;

    public UserSelectBandObjecto(int debate_id, int user_id, int band_id) {
        super(user_id);
        this.debate_id = debate_id;
        this.band_id = band_id;
    }

    public int getDebate_id() {
        return debate_id;
    }

    public int getBand_id() {
        return band_id;
    }
}
