package com.irandubamodulo01.service;



import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Peixe;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import com.irandubamodulo01.model.Lote;

import java.math.BigDecimal;

public class ComponentFather extends Lote{

	private static final long serialVersionUID = 1L;
	
	
	private TreeNode node;

	public ComponentFather(String codigo, TreeNode root){
		setCodigo(codigo);
		this.node = new DefaultTreeNode(this, root);
	}

	public ComponentFather(Long id, Peixe peixe, Fornecedor fornecedor, Compra compra, BigDecimal peso, BigDecimal valor, Integer qtdCaixas,
						   BigDecimal valorUnitarioPeixe, Integer sequencia,
						   BigDecimal descontokg, BigDecimal desconto, BigDecimal acrescimo, BigDecimal pesoCacapa,
						   Boolean nohPai, String codigo, TreeNode root, BigDecimal totalValorKG, BigDecimal totalPesoBruto,
						   BigDecimal totalPesoLiquido, Integer totalCaixas, BigDecimal totalValor, BigDecimal totalDesconto, BigDecimal totalAcrescimo, BigDecimal totalValorTabela, Boolean isPrecoDiferenciado){
		setId(id);
		setPeixe(peixe);
		setFornecedor(fornecedor);
		setCompra(compra);
		setPeso(peso);
		setValor(valor);
		setQtdCaixas(qtdCaixas);
		setValorUnitarioPeixe(valorUnitarioPeixe);
		setSequencia(sequencia);
		setDescontokg(descontokg);
		setDesconto(desconto);
		setAcrescimo(acrescimo);
		setPesoCacapa(pesoCacapa);
		setNohPai(nohPai);
		setCodigo(codigo);
		setTotalValorKG(totalValorKG);
		setTotalPesoBruto(totalPesoBruto);
		setTotalPesoLiquido(totalPesoLiquido);
		setTotalCaixas(totalCaixas);
		setTotalValor(totalValor);
		setTotalDesconto(totalDesconto);
		setTotalAcrescimo(totalAcrescimo);
		setTotalValorTabela(totalValorTabela);
		setIsPrecoDiferenciado(isPrecoDiferenciado);
		this.node = new DefaultTreeNode(this, root);
		this.node.setExpanded(true);
	}

	public ComponentFather(String codigo){
		setCodigo(codigo);
	}
	
	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}
}
