import java.io.Serializable;
public class MensagemServidor implements Serializable {

    private String mensagem;

    public MensagemServidor(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
