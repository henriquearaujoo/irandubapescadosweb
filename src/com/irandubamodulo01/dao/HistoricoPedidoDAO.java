package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoPedido;
import com.irandubamodulo01.model.Observacao;
import com.irandubamodulo01.model.Pedido;

import java.util.List;

public interface HistoricoPedidoDAO extends DAO<HistoricoPedido, Long>{

	public List<HistoricoPedido> getHistoricoPorPedido(Pedido pedido);
	
}
