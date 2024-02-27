package com.example.debatazo.debaterecycler.detalle;

import com.example.debatazo.debaterecycler.DebateProducto;

import java.util.List;

public class DebateDetalleObjeto {
    private DebateProducto list;
    private List<ComentarioObjeto> comment;

    public DebateDetalleObjeto(DebateProducto list, List<ComentarioObjeto> comment) {
        this.list = list;
        this.comment = comment;
    }

    public DebateProducto getList() {
        return list;
    }

    public void setList(DebateProducto list) {
        this.list = list;
    }

    public List<ComentarioObjeto> getComment() {
        return comment;
    }

    public void setComment(List<ComentarioObjeto> comment) {
        this.comment = comment;
    }
}
