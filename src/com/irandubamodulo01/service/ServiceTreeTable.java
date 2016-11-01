package com.irandubamodulo01.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.irandubamodulo01.model.Lote;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class ServiceTreeTable {

	private Map<String, ComponentFather> leafs;
	private TreeNode root;

	public ServiceTreeTable() {
		root = new DefaultTreeNode(new ComponentFather("root"), null);
	}

	public TreeNode getRoot(List<Lote> lotes) {

		leafs = new HashMap<>();
		for (Lote lote : lotes) {
			createComponentFather(lote.getPeixe().getDescricao(), lote);
		}

		return root;
	}

	private void createComponentFather(String key, Lote lote) {

		if (!leafs.containsKey(key)) {

			leafs.put(key, new ComponentFather(lote.getId(), lote.getPeixe(), lote.getFornecedor(), lote.getCompra(), lote.getPeso(), lote.getValor(),
					lote.getQtdCaixas(), lote.getValorUnitarioPeixe(), lote.getSequencia(), lote.getDescontokg(),
					lote.getDesconto(), lote.getAcrescimo(), lote.getPesoCacapa(),
					true, key, root, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, lote.getIsPrecoDiferenciado()));
		}

		TreeNode note = new DefaultTreeNode(lote, leafs.get(lote.getPeixe().getDescricao()).getNode());

		leafs.get(lote.getPeixe().getDescricao()).setTotalValorKG(leafs.get(lote.getPeixe().getDescricao()).getTotalValorKG().add(lote.getValorUnitarioPeixe()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalPesoBruto(leafs.get(lote.getPeixe().getDescricao()).getTotalPesoBruto().add(lote.getPeso()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalPesoLiquido(leafs.get(lote.getPeixe().getDescricao()).getTotalPesoLiquido().add(lote.getPesoLiquido()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalValor(leafs.get(lote.getPeixe().getDescricao()).getTotalValor().add(lote.getValor()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalCaixas(leafs.get(lote.getPeixe().getDescricao()).getTotalCaixas() + lote.getQtdCaixas());

		leafs.get(lote.getPeixe().getDescricao()).setTotalDesconto(leafs.get(lote.getPeixe().getDescricao()).getTotalDesconto().add(lote.getDesconto()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalAcrescimo(leafs.get(lote.getPeixe().getDescricao()).getTotalAcrescimo().add(lote.getAcrescimo()));

		leafs.get(lote.getPeixe().getDescricao()).setTotalValorTabela(leafs.get(lote.getPeixe().getDescricao()).getTotalValorTabela().add(lote.getPeixe().getValor()));
	}

}
