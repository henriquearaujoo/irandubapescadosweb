package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Parcela;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.model.Produto;

import java.util.List;

public interface ParcelaDAO extends DAO<Parcela, Long>{

	public List<Parcela> getParcelasPorPedido(Pedido pedido);
}
