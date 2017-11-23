package br.iesb.appcivicotcu.entities;

/**
 * Created by luan on 20/11/17.
 */

public class Medicamento {
    private String codBarraEan;
    private String cnpj;
    private String laboratorio;
    private String produto;
    private String apresentacao;
    private String principioAtivo;
    private String classeTerapeutica;
    private Double pf0;
    private Double pf12;
    private Double pf17;
    private Double pmc20;

    public Medicamento() {

    }

    public Medicamento(String nome, String apresentacao, Double pmc20) {
        this.produto = nome;
        this.apresentacao = apresentacao;
        this.pmc20 = pmc20;
    }

    public Double getPmc20() {
        return pmc20;
    }

    public void setPmc0(Double pmc20) {
        this.pmc20 = pmc20;
    }

    public String getCodBarraEan() {
        return codBarraEan;
    }

    public void setCodBarraEan(String codBarraEan) {
        this.codBarraEan = codBarraEan;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public String getClasseTerapeutica() {
        return classeTerapeutica;
    }

    public void setClasseTerapeutica(String classeTerapeutica) {
        this.classeTerapeutica = classeTerapeutica;
    }

    public Double getPf0() {
        return pf0;
    }

    public void setPf0(Double pf0) {
        this.pf0 = pf0;
    }

    public Double getPf12() {
        return pf12;
    }

    public void setPf12(Double pf12) {
        this.pf12 = pf12;
    }

    public Double getPf17() {
        return pf17;
    }

    public void setPf17(Double pf17) {
        this.pf17 = pf17;
    }
}