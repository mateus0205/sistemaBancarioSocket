import java.io.Serializable;

public class MensagemCliente implements Serializable {

    private String numeroConta;
    private TipoOperacao tipoOperacao;
    private double valor;
    private String destino;

    public MensagemCliente(String numeroConta, TipoOperacao tipoOperacao, double valor) {
        this.numeroConta = numeroConta;
        this.tipoOperacao = tipoOperacao;
        this.valor = valor;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }

    public double getValor() {
        return valor;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
