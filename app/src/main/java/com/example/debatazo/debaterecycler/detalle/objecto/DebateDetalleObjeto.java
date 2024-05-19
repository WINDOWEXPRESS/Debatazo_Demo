package com.example.debatazo.debaterecycler.detalle.objecto;

import com.example.debatazo.band.BandObject;
import com.example.debatazo.debaterecycler.DebateProducto;

import java.util.List;

public class DebateDetalleObjeto {
    private DebateProducto list;
    private  List<BandObject> bands;
    private int like;
    private int bandSelected;
    private boolean hasFollow;
    private boolean hasLike;
    private List<ComentarioObjeto> comments;

    public DebateDetalleObjeto(DebateProducto list, List<BandObject> bands, int like, int bandSelected, boolean hasFollow, boolean hasLike, List<ComentarioObjeto> comments) {
        this.list = list;
        this.bands = bands;
        this.like = like;
        this.bandSelected = bandSelected;
        this.hasFollow = hasFollow;
        this.hasLike = hasLike;
        this.comments = comments;
    }

    public DebateProducto getList() {
        return list;
    }

    public List<BandObject> getBands() {
        return bands;
    }

    public int getLike() {
        return like;
    }

    public int getBandSelected() {
        return bandSelected;
    }

    public boolean isHasFollow() {
        return hasFollow;
    }

    public boolean isHasLike() {
        return hasLike;
    }

    public List<ComentarioObjeto> getComments() {
        return comments;
    }
}
