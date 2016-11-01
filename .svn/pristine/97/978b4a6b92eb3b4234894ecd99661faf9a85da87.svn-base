package com.irandubamodulo01.util;

import com.irandubamodulo01.enumerated.TipoPessoa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by henrique on 10/05/2015.
 */
public class LeituraArquivoUtil {

    public static void main(String[] args){

        //gerarScriptFornecedores();
        //gerarScriptPeixes();
        gerarScriptTransportadora();
    }

    public static void gerarScriptFornecedores(){
        String path = "C:\\Users\\henrique\\Desktop\\Ambiente de Desenvolvimento\\Iranduba pescados\\FORNECEDORES.txt";
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File(path)), "Cp1252").useDelimiter("\\||\\n");
            while (scanner.hasNext()){
                String[] linha = scanner.next().split(",");
                if (isInteiro(linha[0].toString().replace("\"", ""))) {
                    String CNPJ = linha[2].replace("\"", "");
                    String CPF = linha[3].replace("\"", "");
                    String nome = linha[4].replace("\"", "");
                    String cidade = "";
                    String endereco = "";
                    if (linha[6].replace("\"", "").toLowerCase().startsWith("rua"))
                        endereco = linha[6].replace("\"", "");
                    else
                        cidade = linha[6].replace("\"", "");
                    String numero = linha[7].replace("\"", "").toLowerCase().contains("s") ? "" : linha[7].replace("\"", "");
                    String cep = linha[9].replace("\"", "");
                    String estado = linha[10].replace("\"", "");
                    String tel1 = linha[14].replace("\"", "");
                    String tel2 = linha[15].replace("\"", "");
                    String rg = linha[22].replace("\"", "").replace("\r","");
                    if (linha[1].toString().replace("\"", "").equals("Fisico")){
                        String insert = "INSERT INTO FORNECEDOR (NOME, CPF, CIDADE, ENDERECO, NUMERO, CEP, ESTADO, TELEFONE1, TELEFONE2, RG, TIPOPESSOA) VALUES ('" + nome + "', '" + CPF + "', '" + cidade + "', '" + endereco + "', '" + numero + "', '" + cep + "', '" + estado + "', '" + tel1 + "', '" + tel2 + "', '" + rg + "', '" + TipoPessoa.FISICA + "');";
                        System.out.println(insert);
                    }else{
                        String insert = "INSERT INTO FORNECEDOR (NOME, CNPJ, CIDADE, ENDERECO, NUMERO, CEP, ESTADO, TELEFONE1, TELEFONE2, RG, TIPOPESSOA) VALUES ('" + nome + "', '" + CNPJ + "', '" + cidade + "', '" + endereco + "', '" + numero + "', '" + cep + "', '" + estado + "', '" + tel1 + "', '" + tel2 + "', '" + rg + "', '" + TipoPessoa.JURIDICA + "');";
                        System.out.println(insert);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void gerarScriptTransportadora(){
        String path = "C:\\Users\\henrique\\Desktop\\Ambiente de Desenvolvimento\\Iranduba pescados\\backup_transp_iranduba.txt";
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File(path)), "Cp1252").useDelimiter("\\||\\n");
            while (scanner.hasNext()){
                String[] linha = scanner.next().split(",");

                String CNPJ = linha[1].replace("\"", "");
                //String CPF = linha[3].replace("\"", "");
                String nome = linha[2].replace("\"", "");
                String cidade = "";
                String endereco = linha[4].replace("\"", "");
                /*if (linha[6].replace("\"", "").toLowerCase().startsWith("rua"))
                    endereco = linha[6].replace("\"", "");
                else
                    cidade = linha[6].replace("\"", "");*/
                String numero = linha[5].replace("\"", "");
                String cep = linha[8].replace("\"", "");
                String estado = linha[10].replace("\"", "");
                String tel1 = linha[13].replace("\"", "");
                String tel2 = linha[15].replace("\"", "");
                //String rg = linha[22].replace("\"", "").replace("\r","");
                if (linha[1].toString().replace("\"", "").equals("Fisico")){
                    String insert = "INSERT INTO TRANSPORTADORA (NOME, CPF, CIDADE, ENDERECO, NUMERO, CEP, ESTADO, TELEFONE1, TELEFONE2, TIPOPESSOA) VALUES ('" + nome + "', '" + CNPJ + "', '" + cidade + "', '" + endereco + "', '" + numero + "', '" + cep + "', '" + estado + "', '" + tel1 + "', '" + tel2 + "', '" + TipoPessoa.FISICA + "');";
                    System.out.println(insert);
                }else{
                    String insert = "INSERT INTO TRANSPORTADORA (NOME, CNPJ, CIDADE, ENDERECO, NUMERO, CEP, ESTADO, TELEFONE1, TELEFONE2, TIPOPESSOA) VALUES ('" + nome + "', '" + CNPJ + "', '" + cidade + "', '" + endereco + "', '" + numero + "', '" + cep + "', '" + estado + "', '" + tel1 + "', '" + tel2 + "', '" + TipoPessoa.JURIDICA + "');";
                    System.out.println(insert);
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void gerarScriptPeixes(){
        String path = "C:\\Users\\henrique\\Desktop\\Ambiente de Desenvolvimento\\Iranduba pescados\\PEIXES.txt";
        try {

            Scanner scanner = new Scanner(new FileInputStream(new File(path)), "Cp1252").useDelimiter("\\||\\n");
            while (scanner.hasNext()){
                String[] linha = scanner.next().split(",");
                if (isInteiro(linha[0].toString().replace("\"", ""))) {
                    System.out.println("INSERT INTO PEIXE (DESCRICAO) VALUES ('" + linha[2].replace("\"", "") + "');");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Boolean isInteiro(String str){
        try {
            Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
